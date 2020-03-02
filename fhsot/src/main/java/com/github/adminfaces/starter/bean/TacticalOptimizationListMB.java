package com.github.adminfaces.starter.bean;

import static com.github.adminfaces.starter.util.LazyDataModelUtils.toApiFilters;
import static com.github.adminfaces.starter.util.LazyDataModelUtils.toApiPage;
import static com.github.adminfaces.starter.util.LazyDataModelUtils.toApiSize;
import static com.github.adminfaces.starter.util.LazyDataModelUtils.toApiSort;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.fasten.wp4.database.client.api.TacticalOptimizationControllerApi;
import com.fasten.wp4.database.client.api.TacticalOptimizationResultControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.PageOfTacticalOptimization;
import com.fasten.wp4.database.client.model.TacticalOptimization;
import com.fasten.wp4.email.client.api.EmailControllerApi;
import com.fasten.wp4.email.client.model.Email;
import com.fasten.wp4.optimizator.tactical.client.invoker.ApiCallback;
import com.fasten.wp4.optimizator.tactical.client.model.TacticalOptimizationResult;
import com.github.adminfaces.starter.infra.async.AsyncCall;
import com.github.adminfaces.starter.infra.async.AsyncRequestUtils;
import com.github.adminfaces.starter.infra.security.LogonMB;
import com.github.adminfaces.template.exception.BusinessException;
import com.squareup.okhttp.Call;

@Named
@ViewScoped
public class TacticalOptimizationListMB implements Serializable {
	

	@Inject
	private transient TacticalOptimizationControllerApi tacticalOptimizationControllerApi;
	
	@Inject
	private transient TacticalOptimizationResultControllerApi tacticalOptimizationResultControllerApi;
	
	@Inject
	LogonMB logonMB;
	
//	@Inject
//    AdminSession adminSession;

	Long id;
	
	LazyDataModel<TacticalOptimization> lazyItems;

	// datatable filteredValue attribute (column filters)
	List<TacticalOptimization> filteredValues;

	List<TacticalOptimization> selecteds;
	
	@Inject
	private transient com.fasten.wp4.optimizator.tactical.client.api.TacticalOptimizationControllerApi tacticalOptimizationApi;
	
	@Inject
	List<AsyncCall> asyncCalls;
	
	@Inject
	AsyncNotifyMB asyncNotifyMB;

	public void init() {
		if(Faces.isAjaxRequest()){
			return;
		}
		selecteds = new ArrayList<TacticalOptimization>();
	}

	@PostConstruct
	public void initDataModel() {

		lazyItems = new LazyDataModel<TacticalOptimization>() {

			@Override
			@SuppressWarnings("finally")
			public List<TacticalOptimization> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {

				if(has(id))
					filters.put("id",id+"");
				
				List<TacticalOptimization> list = Arrays.asList();
				try {
					PageOfTacticalOptimization page = tacticalOptimizationControllerApi.retrieveTacticalOptimizationFilteredAndPaged(toApiFilters(filters), toApiPage(first, pageSize), toApiSize(pageSize), toApiSort( multiSortMeta));
					list=page.getContent();
					setRowCount(page.getTotalElements().intValue());
					return list;
				} catch (ApiException e) {
					throw new BusinessException("Could not retrive list"); 
				}finally{
					return list;
				}
			}

			@Override
			public TacticalOptimization getRowData(String key) {
				try {
					TacticalOptimization tacticalOptimization = tacticalOptimizationControllerApi.retrieveTacticalOptimization(new Long(key));
					selecteds.add(tacticalOptimization);
					return tacticalOptimization;
				} catch (ApiException e) {
					throw  new BusinessException("Not found with id " + id);
				}finally{
				}
			}
		};
	}
	
	public boolean isValidable() {
	    return (selecteds.size()==1 && selecteds.get(0).getStatus().getValue().equals(TacticalOptimization.StatusEnum.INVALID.getValue()));
	}
	
	public boolean isExecutable() {
	    return (selecteds.size()==1 && selecteds.get(0).getStatus().getValue().equals(TacticalOptimization.StatusEnum.VALID.getValue()));
	}
	
	public void redirectToValidationPage() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("tactical-optimization-validate-form.xhtml?id="+selecteds.get(0).getId());
	}

	public void delete() {
		int numTacticalOptimization = 0;
		for (TacticalOptimization selectedTacticalOptimization : selecteds) {
			numTacticalOptimization++;
			try {
				tacticalOptimizationControllerApi.deleteTacticalOptimization(selectedTacticalOptimization.getId());
			} catch (ApiException e) {
				throw  new BusinessException("Not found with id " + id);
			} finally {}
		}
		selecteds.clear();
		addDetailMessage(numTacticalOptimization + " processing part deleted successfully!");
	}
	
	public boolean hasResult() {
		if(selecteds.size()!=1) {
			return false;
		}else {
			TacticalOptimization selected = selecteds.get(0);
			try{
				return tacticalOptimizationResultControllerApi.existsByTacticalOptimization(selected.getId());
			}catch(ApiException e) {
				throw  new BusinessException("Could not verify if this study has any results");
			}
		}
	}
	
	public void execute(TacticalOptimization entity) throws ApiException {
		try {
			String requestId = UUID.randomUUID().toString();
			Call c = tacticalOptimizationApi.executeTacticalOptimizationAsync(requestId,entity.getId(),logonMB.getEmail(), new ApiCallback<TacticalOptimizationResult>() {
				
				@Override
				public void onSuccess(TacticalOptimizationResult tacticalOptimizationResult, int statusCode, Map<String, List<String>> responseHeaders) {
					asyncNotifyMB.sendPushMessage("tactical_optimization_execution");
				}
				
				@Override
				public void onFailure(com.fasten.wp4.optimizator.tactical.client.invoker.ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
					asyncNotifyMB.sendPushMessage("error_tactical_optimization");
					throw  new BusinessException("Could not execute tactical optimization study");
				}
				
				@Override
				public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {}
				@Override
				public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {}
			});
			
			AsyncCall asyncCall = AsyncRequestUtils.getAsyncCallFrom(tacticalOptimizationApi.getApiClient().getHttpClient().getDispatcher(), c,requestId);
			asyncCall.setStudy(entity);
			asyncCalls.add(asyncCall);
			
		} catch (com.fasten.wp4.optimizator.tactical.client.invoker.ApiException e) {
			e.printStackTrace();
			throw  new BusinessException("Could not execute tactical optimization study");
		} 
		
		

		selecteds.clear();
		addDetailMessage("Execution of tactical optimization study requested successfully!");
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<TacticalOptimization> getSelecteds() {
		return selecteds;
	}

	public void setSelecteds(List<TacticalOptimization> selecteds) {
		this.selecteds = selecteds;
	}

	public List<TacticalOptimization> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TacticalOptimization> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public LazyDataModel<TacticalOptimization> getLazyItems() {
		return lazyItems;
	}

	public void setLazyItems(LazyDataModel<TacticalOptimization> lazyItems) {
		this.lazyItems = lazyItems;
	}

}
