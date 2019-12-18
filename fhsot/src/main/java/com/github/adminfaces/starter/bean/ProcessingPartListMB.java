package com.github.adminfaces.starter.bean;

import static com.github.adminfaces.starter.util.LazyDataModelUtils.toApiFilters;
import static com.github.adminfaces.starter.util.LazyDataModelUtils.toApiPage;
import static com.github.adminfaces.starter.util.LazyDataModelUtils.toApiSize;
import static com.github.adminfaces.starter.util.LazyDataModelUtils.toApiSort;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.fasten.wp4.database.client.api.PartControllerApi;
import com.fasten.wp4.database.client.api.ProcessingPartControllerApi;
import com.fasten.wp4.database.client.api.SramControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.PageOfProcessingPart;
import com.fasten.wp4.database.client.model.ProcessingPart;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class ProcessingPartListMB implements Serializable {

	@Inject
	private transient ProcessingPartControllerApi processingPartControllerApi;

	@Inject
	private transient PartControllerApi partControllerApi;

	@Inject
	private transient SramControllerApi sramControllerApi;
	
	Long id;
	
	List<String> parts;
	
	List<String> srams;

	LazyDataModel<ProcessingPart> processingParts;

	// datatable filteredValue attribute (column filters)
	List<ProcessingPart> filteredValue;

	List<ProcessingPart> selectedProcessingParts;

	public void init() {
		if(Faces.isAjaxRequest()){
			return;
		}
		selectedProcessingParts = new ArrayList<ProcessingPart>();
		
		try {
			parts= partControllerApi.retrieveAllDistinctByName();
			srams= sramControllerApi.retrieveAllDistinctByCode();
		} catch (ApiException e) {
			throw new BusinessException("Could not retrive list");
		}finally{
		}
	}

	@PostConstruct
	public void initDataModel() {

		processingParts = new LazyDataModel<ProcessingPart>() {

			@Override
			public List<ProcessingPart> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {

				if(has(id))
					filters.put("id",id+"");
				if(has(multiSortMeta))
					multiSortMeta.forEach(sm -> {if(sm.getSortField().contentEquals("sram.code")){sm.setSortField("SRAM.code");}});
				if(has(filters) && filters.containsKey("sram.code")) {
					filters = filters.entrySet().stream().collect(Collectors.toMap(
							key -> { return (key.getKey().equals("sram.code"))?"SRAM.code":key.getKey();},
		                    Map.Entry::getValue
					));
				}

				
				List<ProcessingPart> list = Arrays.asList();
				try {
					PageOfProcessingPart page = processingPartControllerApi.retrieveProcessingPartFilteredAndPaged(toApiFilters(filters), toApiPage(first, pageSize), toApiSize(pageSize), toApiSort( multiSortMeta));
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
			public ProcessingPart getRowData(String key) {
				try {
					ProcessingPart processingPart = processingPartControllerApi.retrieveProcessingPart(new Long(key));
					selectedProcessingParts.add(processingPart);
					return processingPart;
				} catch (ApiException e) {
					throw  new BusinessException("Not found with id " + id);
				}finally{
				}
			}
		};
	}

	public List<String> getParts() {
		return parts;
	}

	public List<String> getSrams() {
		return srams;
	}

	public void delete() {
		int numProcessingPart = 0;
		for (ProcessingPart selectedProcessingPart : selectedProcessingParts) {
			numProcessingPart++;
			try {
				processingPartControllerApi.deleteProcessingPart(selectedProcessingPart.getId());
			} catch (ApiException e) {
				throw  new BusinessException("Processing part not found with id " + id);
			} finally {}
		}
		selectedProcessingParts.clear();
		addDetailMessage(numProcessingPart + " processing part deleted successfully!");
	}

	public List<ProcessingPart> getSelectedProcessingParts() {
		return selectedProcessingParts;
	}

	public List<ProcessingPart> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<ProcessingPart> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public void setSelectedProcessingParts(List<ProcessingPart> selectedProcessingParts) {
		this.selectedProcessingParts = selectedProcessingParts;
	}

	public LazyDataModel<ProcessingPart> getProcessingParts() {
		return processingParts;
	}

	public void setProcessingParts(LazyDataModel<ProcessingPart> processingParts) {
		this.processingParts = processingParts;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
