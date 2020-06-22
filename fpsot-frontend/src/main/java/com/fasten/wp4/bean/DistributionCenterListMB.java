package com.fasten.wp4.bean;

import static com.fasten.wp4.util.LazyDataModelUtils.toApiFilters;
import static com.fasten.wp4.util.LazyDataModelUtils.toApiPage;
import static com.fasten.wp4.util.LazyDataModelUtils.toApiSize;
import static com.fasten.wp4.util.LazyDataModelUtils.toApiSort;
import static com.fasten.wp4.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.ors.geocode.client.api.GeocodeApi;
import org.ors.geocode.client.model.AddressResponse;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.fasten.wp4.database.client.api.DistributionCenterControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.DistributionCenter;
import com.fasten.wp4.database.client.model.PageOfDistributionCenter;
import com.fasten.wp4.infra.async.AsyncCall;
import com.fasten.wp4.infra.client.OpenRouteServiceGeocodeClient;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class DistributionCenterListMB implements Serializable {

	@Inject
	private transient DistributionCenterControllerApi distributionCenterControllerApi;

	@Inject
	private transient GeocodeApi geocodeApi;

	Long id;

	LazyDataModel<DistributionCenter> distributionCenters;

	// datatable filteredValue attribute (column filters)
	List<DistributionCenter> filteredValue;

	List<DistributionCenter> selectedDistributionCenters;

	DistributionCenter selected;
	
	@Inject
	List<AsyncCall> asyncCalls;

	@SuppressWarnings("unchecked")
	public void init() {
		if(Faces.isAjaxRequest()){
			return;
		}
		selectedDistributionCenters = new ArrayList<DistributionCenter>(); 
	}

	@PostConstruct
	public void initDataModel() {

		distributionCenters = new LazyDataModel<DistributionCenter>() {

			@Override
			public List<DistributionCenter> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {

				if(has(id))
					filters.put("id",id+"");

				List<DistributionCenter> list = Arrays.asList();
				try {
					PageOfDistributionCenter page = distributionCenterControllerApi.retrieveDistributionCenterFilteredAndPaged(toApiFilters(filters), toApiPage(first, pageSize), toApiSize(pageSize), toApiSort( multiSortMeta));
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
			public DistributionCenter getRowData(String key) {
				//TODO trecho de codigo para demonstrar varias requisicoes asyncronas
//				try {
//					Call c = distributionCenterControllerApi.retrieveDistributionCenterAsync(new Long(key) ,new ApiCallback<DistributionCenter>() {
//
//						@Override
//						public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {}
//
//						@Override
//						public void onSuccess(DistributionCenter result, int statusCode, Map<String, List<String>> responseHeaders) {}
//
//						@Override
//						public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {}
//
//						@Override
//						public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {}
//					});
//
//					AsyncCall asyncCall = AsyncRequestUtils.getAsyncCallFrom(distributionCenterControllerApi.getApiClient().getHttpClient().getDispatcher(), c);
//					asyncCall.setStudy(this.getWrappedData().parallelStream().filter(entity -> key.equals(entity.getId().toString())).findAny().orElse(null));
//					asyncCalls.add(asyncCall);
//					
//				} catch (ApiException e) {
//					e.printStackTrace();
//				} 


				return this.getWrappedData().parallelStream().filter(entity -> key.equals(entity.getId().toString())).findAny().orElse(null);
			}
		};
	}
	
	
	

	public void getCoordinates(DistributionCenter distributionCenter) {
		String apiKey = OpenRouteServiceGeocodeClient.ORS_API_KEY;
		String text = distributionCenter.getAddress().getCity().getName()+", "+distributionCenter.getAddress().getCity().getState().getName()+", Brazil";
		Float focusPointLon = null;
		Float focusPointLat = null;
		Float boundaryRectMinLon = null;
		Float boundaryRectMinLat = null;
		Float boundaryRectMaxLon = null;
		Float boundaryRectMaxLat = null;
		Float boundaryCircleLon = null;
		Float boundaryCircleLat = null;
		Float boundaryCircleRadius = null;
		String boundaryCountry = "BRA";
		List<String> sources = null;
		List<String> layers = null;
		Integer size = null;
		AddressResponse response;

		try {
			response = geocodeApi.geocodeSearchGet(apiKey, text, focusPointLon, focusPointLat, boundaryRectMinLon, boundaryRectMinLat, boundaryRectMaxLon, boundaryRectMaxLat, boundaryCircleLon, boundaryCircleLat, boundaryCircleRadius, boundaryCountry, sources, layers, size);
			response.getFeatures().stream().forEach(f->{distributionCenter.getAddress().setLongitude(f.getGeometry().getCoordinates().get(0)); distributionCenter.getAddress().setLatitude(f.getGeometry().getCoordinates().get(1));});
		} catch (Exception e) {
			throw new BusinessException("Could not retrive Geolocation (lon, lat) for "+ distributionCenter.getAddress().getCity());
		}

		try {
			distributionCenterControllerApi.updateDistributionCenter(distributionCenter.getId(), distributionCenter);
		} catch (ApiException e) {
			throw new BusinessException("Could not save Geolocation (lon, lat) for "+ distributionCenter.getAddress().getCity());
		}	

		selectedDistributionCenters.clear();
		addDetailMessage(" Coordinates updated successfully!");
	}

	public void delete() {
		int numDistributionCenter = 0;
		for (DistributionCenter selectedDistributionCenter : selectedDistributionCenters) {
			numDistributionCenter++;
			try {
				distributionCenterControllerApi.deleteDistributionCenter(selectedDistributionCenter.getId());
			} catch (ApiException e) {
				throw  new BusinessException("DistributionCenter not found with id " + id);
			} finally {}
		}
		selectedDistributionCenters.clear();
		addDetailMessage(numDistributionCenter + " distributionCenter deleted successfully!");
	}

	public List<DistributionCenter> getSelectedDistributionCenters() {
		return selectedDistributionCenters;
	}

	public List<DistributionCenter> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<DistributionCenter> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public void setSelectedDistributionCenters(List<DistributionCenter> selectedDistributionCenters) {
		this.selectedDistributionCenters = selectedDistributionCenters;
	}

	public LazyDataModel<DistributionCenter> getDistributionCenters() {
		return distributionCenters;
	}

	public void setDistributionCenters(LazyDataModel<DistributionCenter> distributionCenters) {
		this.distributionCenters = distributionCenters;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DistributionCenter getSelected() {
		return selected;
	}

	public void setSelected(DistributionCenter selected) {
		this.selected = selected;
	}

}
