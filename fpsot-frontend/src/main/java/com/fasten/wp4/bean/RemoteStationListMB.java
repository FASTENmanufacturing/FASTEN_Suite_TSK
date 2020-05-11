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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.ors.geocode.client.api.GeocodeApi;
import org.ors.geocode.client.model.AddressResponse;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.fasten.wp4.database.client.api.RemoteStationControllerApi;
import com.fasten.wp4.database.client.invoker.ApiCallback;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.PageOfRemoteStation;
import com.fasten.wp4.database.client.model.RemoteStation;
import com.fasten.wp4.infra.async.AsyncCall;
import com.fasten.wp4.infra.async.AsyncRequestUtils;
import com.github.adminfaces.template.exception.BusinessException;
import com.squareup.okhttp.Call;

@Named
@ViewScoped
public class RemoteStationListMB implements Serializable {

	@Inject
	private transient RemoteStationControllerApi remoteStationControllerApi;

	@Inject
	private transient GeocodeApi geocodeApi;

	Long id;

	LazyDataModel<RemoteStation> remoteStations;

	// datatable filteredValue attribute (column filters)
	List<RemoteStation> filteredValue;

	List<RemoteStation> selectedRemoteStations;

	RemoteStation selected;
	
	@Inject
	List<AsyncCall> asyncCalls;

	@SuppressWarnings("unchecked")
	public void init() {
		if(Faces.isAjaxRequest()){
			return;
		}
		selectedRemoteStations = new ArrayList<RemoteStation>(); 
	}

	@PostConstruct
	public void initDataModel() {

		remoteStations = new LazyDataModel<RemoteStation>() {

			@Override
			public List<RemoteStation> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {

				if(has(id))
					filters.put("id",id+"");

				List<RemoteStation> list = Arrays.asList();
				try {
					PageOfRemoteStation page = remoteStationControllerApi.retrieveRemoteStationFilteredAndPaged(toApiFilters(filters), toApiPage(first, pageSize), toApiSize(pageSize), toApiSort( multiSortMeta));
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
			public RemoteStation getRowData(String key) {
				//TODO trecho de codigo para demonstrar varias requisicoes asyncronas
//				try {
//					Call c = remoteStationControllerApi.retrieveRemoteStationAsync(new Long(key) ,new ApiCallback<RemoteStation>() {
//
//						@Override
//						public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {}
//
//						@Override
//						public void onSuccess(RemoteStation result, int statusCode, Map<String, List<String>> responseHeaders) {}
//
//						@Override
//						public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {}
//
//						@Override
//						public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {}
//					});
//
//					AsyncCall asyncCall = AsyncRequestUtils.getAsyncCallFrom(remoteStationControllerApi.getApiClient().getHttpClient().getDispatcher(), c);
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
	
	
	

	public void getCoordinates(RemoteStation remoteStation) {
		String apiKey = "5b3ce3597851110001cf6248f61f0e8e01c1439b977e4410e5cd757a";
		String text = remoteStation.getAddress().getCity().getName()+", "+remoteStation.getAddress().getCity().getState().getName()+", Brazil";
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
			response.getFeatures().stream().forEach(f->{remoteStation.getAddress().setLongitude(f.getGeometry().getCoordinates().get(0)); remoteStation.getAddress().setLatitude(f.getGeometry().getCoordinates().get(1));});
		} catch (Exception e) {
			throw new BusinessException("Could not retrive Geolocation (lon, lat) for "+ remoteStation.getAddress().getCity());
		}

		try {
			remoteStationControllerApi.updateRemoteStation(remoteStation.getId(), remoteStation);
		} catch (ApiException e) {
			throw new BusinessException("Could not save Geolocation (lon, lat) for "+ remoteStation.getAddress().getCity());
		}	

		selectedRemoteStations.clear();
		addDetailMessage(" Coordinates updated successfully!");
	}

	public void delete() {
		int numRemoteStation = 0;
		for (RemoteStation selectedRemoteStation : selectedRemoteStations) {
			numRemoteStation++;
			try {
				remoteStationControllerApi.deleteRemoteStation(selectedRemoteStation.getId());
			} catch (ApiException e) {
				throw  new BusinessException("RemoteStation not found with id " + id);
			} finally {}
		}
		selectedRemoteStations.clear();
		addDetailMessage(numRemoteStation + " remoteStation deleted successfully!");
	}

	public List<RemoteStation> getSelectedRemoteStations() {
		return selectedRemoteStations;
	}

	public List<RemoteStation> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<RemoteStation> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public void setSelectedRemoteStations(List<RemoteStation> selectedRemoteStations) {
		this.selectedRemoteStations = selectedRemoteStations;
	}

	public LazyDataModel<RemoteStation> getRemoteStations() {
		return remoteStations;
	}

	public void setRemoteStations(LazyDataModel<RemoteStation> remoteStations) {
		this.remoteStations = remoteStations;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RemoteStation getSelected() {
		return selected;
	}

	public void setSelected(RemoteStation selected) {
		this.selected = selected;
	}

}
