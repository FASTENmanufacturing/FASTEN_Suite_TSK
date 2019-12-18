package com.github.adminfaces.starter.bean;

import static com.github.adminfaces.starter.util.ApiUtils.getIdFromLocationHeader;
import static com.github.adminfaces.template.util.Assert.has;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Faces;
import org.ors.matrix.client.api.MatrixApi;
import org.ors.matrix.client.model.JSONMatrixResponse;
import org.ors.matrix.client.model.MatrixRequest;
import org.ors.matrix.client.model.MatrixRequest.MetricsEnum;
import org.ors.matrix.client.model.MatrixRequest.UnitsEnum;
import org.ors.geocode.client.api.GeocodeApi;
import org.ors.geocode.client.model.AddressResponse;
import org.primefaces.PrimeFaces;

import com.fasten.wp4.database.client.api.DeliveryControllerApi;
import com.fasten.wp4.database.client.api.RemoteStationControllerApi;
import com.fasten.wp4.database.client.api.TacticalOptimizationControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.Delivery;
import com.fasten.wp4.database.client.model.RemoteStation;
import com.fasten.wp4.database.client.model.TacticalOptimization;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class TacticalOptimizationValidateFormMB implements Serializable {

	private Long id;

	private TacticalOptimization tacticalOptimization;

	@Inject
	private transient TacticalOptimizationControllerApi tacticalOptimizationControllerApi;

	@Inject
	private transient RemoteStationControllerApi remoteStationControllerApi;
	
	@Inject
	private transient DeliveryControllerApi deliveryControllerApi;

	@Inject
	private transient GeocodeApi geocodeApi;
	
	@Inject
	private transient MatrixApi matrixApi;

	public void init() {
		if(Faces.isAjaxRequest()){
			return;
		}
		if (has(id)) {
			try {
				tacticalOptimization = tacticalOptimizationControllerApi.retrieveTacticalOptimization(id);
				
				getRemoteStationByTacticalOptimization();
				calculateTotalCandidates();
				calculateTotalCandidatesWithCoordinates();
				calculateTotalCandidatesWithoutCoordinates();
				calculateGeolocateStatus();
				
				getDeliveriesByTacticalOptimization();
				calculateTotalMatrix();
				calculateTotalMatrixWithDelivery();
				calculateTotalMatrixWithoutDelivery();
				calculateMatrixStatus();
				
			} catch (ApiException e) {
				throw new BusinessException("Could not retrive tactical optimization study");
			}
		} else {
			tacticalOptimization = new TacticalOptimization();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TacticalOptimization getTacticalOptimization() {
		return tacticalOptimization;
	}
	public void setTacticalOptimization(TacticalOptimization tacticalOptimization) {
		this.tacticalOptimization = tacticalOptimization;
	}

	public String getBreadcum() {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle admin = context.getApplication().evaluateExpressionGet(context, "#{adm}", ResourceBundle.class);
		String entityName = "Tactical Optimization Validation";//admin.getString("tacticalOptimization");
		String newExpression = admin.getString("breadcum.new");
		if(tacticalOptimization.getId() == null) {
			return MessageFormat.format(newExpression, entityName);
		}else {
			return StringUtils.capitalize(entityName).concat(" "+tacticalOptimization.getId());
		}
	}

	private List<RemoteStation> tacticalOptimizationLocations;

	public void getRemoteStationByTacticalOptimization() {
		try {
			tacticalOptimizationLocations = remoteStationControllerApi.retrieveRemoteStationByTacticalOptimization(tacticalOptimization.getId());
		} catch (ApiException e) {
			throw new BusinessException("Could not retrive tactical optimization locations");
		}finally {
		}
	}

	private Long candidates;
	private Long candidatesWithoutCoordinates;
	private Long candidatesWithCoordinates;
	private Integer progressGeolocateRemoteStations;
	private String geolocateStatus;

	public void calculateTotalCandidates() {
		candidates = new Long(tacticalOptimizationLocations.size());
	}

	public void calculateTotalCandidatesWithoutCoordinates() {
		candidatesWithoutCoordinates = tacticalOptimizationLocations.stream().filter(t -> t.getLatitude()==null || t.getLongitude()==null).count();
	}

	public void calculateTotalCandidatesWithCoordinates() {
		candidatesWithCoordinates = tacticalOptimizationLocations.stream().filter(t -> t.getLatitude()!=null && t.getLongitude()!=null).count();
	}

	public void calculateProgressGeolocateRemoteStations() {
		double result = ((double)(candidatesWithCoordinates)/(double)(candidates))*100;
		progressGeolocateRemoteStations = (int) result;
	}

	public void calculateGeolocateStatus() {
		if(has(progressGeolocateRemoteStations) && progressGeolocateRemoteStations>=100) {
			geolocateStatus = "success";
		}else if(has(progressGeolocateRemoteStations) && progressGeolocateRemoteStations<100){
			geolocateStatus = "fail";
		}
	}

	public Integer getProgressGeolocateRemoteStations() {
		if(has(geolocateStatus) && geolocateStatus.contentEquals("proceeding")) {
			processGeolocate();
			calculateTotalCandidatesWithCoordinates();
			calculateTotalCandidatesWithoutCoordinates();
		}
		calculateProgressGeolocateRemoteStations();
		return progressGeolocateRemoteStations;
	}

	private int qpm = 5; //100 geolocate by min default interval 3000milis
	private Collection<List<RemoteStation>> requestGroups;
	private Iterator<List<RemoteStation>> requestGroupsIterator;

	public Collection<List<RemoteStation>> groupRemoteStationsWithoutCoordinateToRequest(){
		AtomicInteger counter = new AtomicInteger();
		Collection<List<RemoteStation>> requestGroup = tacticalOptimizationLocations.stream()
				.filter(t -> t.getLatitude()==null || t.getLongitude()==null)
				.collect(Collectors.groupingBy(it -> counter.getAndIncrement() / qpm))
				.values();
		return requestGroup;
	}

	public void processGeolocate() {

		if(requestGroupsIterator.hasNext()) {
			List<RemoteStation> requestGroup = requestGroupsIterator.next();
			for (RemoteStation r : requestGroup) {

				String apiKey = "5b3ce3597851110001cf6248f61f0e8e01c1439b977e4410e5cd757a";
				String text = r.getCity().getName()+", "+r.getCity().getState().getName()+", Brazil";
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
					response.getFeatures().stream().forEach(f->{r.setLongitude(f.getGeometry().getCoordinates().get(0)); r.setLatitude(f.getGeometry().getCoordinates().get(1));});
				} catch (Exception e) {
					throw new BusinessException("Could not retrive Geolocation (lon, lat) for "+ r.getCity());
				}
				try {
					remoteStationControllerApi.updateRemoteStation(r.getId(), r);
				} catch (ApiException e) {
					throw new BusinessException("Could not save Geolocation (lon, lat) for "+ r.getCity());
				}	
			}
		}
	}

	public void onCompleteGeolocate() {
		calculateGeolocateStatus();
	}

	public void onClickGeolocate() {
		geolocateStatus="proceeding";
		requestGroups=groupRemoteStationsWithoutCoordinateToRequest();
		requestGroupsIterator=requestGroups.iterator();
	}

	public void setProgressGeolocateRemoteStations(Integer progressGeolocateRemoteStations) {
		this.progressGeolocateRemoteStations = progressGeolocateRemoteStations;
	}

	public String getGeolocateStatus() {
		return geolocateStatus;
	}

	public void setGeolocateStatus(String geolocateStatus) {
		this.geolocateStatus = geolocateStatus;
	}

	public Long getCandidates() {
		return candidates;
	}

	public void setCandidates(Long candidates) {
		this.candidates = candidates;
	}

	public Long getCandidatesWithoutCoordinates() {
		return candidatesWithoutCoordinates;
	}

	public void setCandidatesWithoutCoordinates(Long candidatesWithoutCoordinates) {
		this.candidatesWithoutCoordinates = candidatesWithoutCoordinates;
	}

	public Long getCandidatesWithCoordinates() {
		return candidatesWithCoordinates;
	}

	public void setCandidatesWithCoordinates(Long candidatesWithCoordinates) {
		this.candidatesWithCoordinates = candidatesWithCoordinates;
	}

	public List<RemoteStation> getTacticalOptimizationLocations() {
		return tacticalOptimizationLocations;
	}

	public void setTacticalOptimizationLocations(List<RemoteStation> tacticalOptimizationLocations) {
		this.tacticalOptimizationLocations = tacticalOptimizationLocations;
	}
	
	/** novo */
	
	private List<Delivery> deliveries;

	public void getDeliveriesByTacticalOptimization() {
		try {
			deliveries = deliveryControllerApi.retrieveDeliveryMatrixByTacticalOptimization(tacticalOptimization.getId());
		} catch (ApiException e) {
			throw new BusinessException("Could not retrive tactical optimization deliveries");
		}finally {
		}
	}
	
	private Long matrix;
	private Long matrixWithout;
	private Long matrixWith;
	private Integer progressMatrixRemoteStations;
	private String matrixStatus;

	public void calculateTotalMatrix() {
		matrix = new Long(tacticalOptimizationLocations.size()*tacticalOptimizationLocations.size());
	}

	public void calculateTotalMatrixWithoutDelivery() {
		matrixWithout = deliveries.stream().filter(d -> d.getDistance()==null || d.getTime()==null).count();
	}

	public void calculateTotalMatrixWithDelivery() {
		matrixWith = deliveries.stream().filter(d -> d.getDistance()!=null && d.getTime()!=null).count();
	}

	public void calculateProgressMatrixRemoteStations() {
		double result = ((double)(matrixWith)/(double)(matrix))*100;
		progressMatrixRemoteStations = (int) result;
	}

	public void calculateMatrixStatus() {
		if(has(progressMatrixRemoteStations) && progressMatrixRemoteStations>=100) {
			matrixStatus = "success";
		}else if(has(progressMatrixRemoteStations) && progressMatrixRemoteStations<100){
			matrixStatus = "fail";
		}
	}

	public Integer getProgressMatrixRemoteStations() {
		if(has(matrixStatus) && matrixStatus.contentEquals("proceeding")) {
			processMatrix();
			calculateTotalMatrixWithDelivery();
			calculateTotalMatrixWithoutDelivery();
			//only execute one time because ask all matrix at once
			completeProgressBarMatrix();
		}
		calculateProgressMatrixRemoteStations();
		return progressMatrixRemoteStations;
	}

//	//	<f:event listener="#{tacticalOptimizationValidateFormMB.logJs}" type="postValidate"/>
//	public void logJs() {
//		PrimeFaces.current().executeScript("console.log('"+candidatesWithCoordinates+"/"+candidates+"');");
//	}
	
	public void completeProgressBarMatrix() {
		PrimeFaces.current().executeScript("PF('progressBarMatrix').fireCompleteEvent();");
	}


	private Delivery retrieveOrCreateBy(RemoteStation origin, RemoteStation destination) throws Exception {
		Delivery delivery = null; 
		try {
			delivery = deliveries.stream().filter(d-> ( d.getOrigin().getId().equals(origin.getId()) && d.getDestination().getId().equals(destination.getId()))).findFirst().get();
		}catch(Exception e) {
			delivery = new Delivery();
			delivery.setOrigin(origin);
			delivery.setDestination(destination);
			delivery.setId(getIdFromLocationHeader(deliveryControllerApi.createDeliveryWithHttpInfo(delivery)));
			deliveries.add(delivery);
		}
		return delivery;
	}
	
	private List<List<Double>> getCoordinatesFromTacticalOptimizationLocations(){
		return tacticalOptimizationLocations.stream().map(r->{return Arrays.asList(r.getLongitude(),r.getLatitude());}).collect(Collectors.toList());
	}
	
	public void processMatrix() {
		MatrixRequest request = new MatrixRequest();
		request.setLocations(getCoordinatesFromTacticalOptimizationLocations());
		request.addMetricsItem(MetricsEnum.DISTANCE).addMetricsItem(MetricsEnum.DURATION).resolveLocations(false).units(UnitsEnum.M);
		try {
			JSONMatrixResponse response = matrixApi.getDefaultUsingPOST("driving-car", request);
			List<List<Double>> distances = response.getDistances();
			List<List<Double>> durations = response.getDurations();
			//convert response to List<Delivery> deliveries
			for (int i = 0; i < distances.size(); i++) {
				List<Double> coluna = distances.get(i);
				RemoteStation origin = tacticalOptimizationLocations.get(i);
				for(int j=0; j<coluna.size();j++) {
					RemoteStation destination = tacticalOptimizationLocations.get(j);
					Delivery delivery = retrieveOrCreateBy(origin, destination);
					delivery.setDistance(coluna.get(j).intValue());
					delivery.setTime(durations.get(i).get(j).intValue());
				}
			}
		} catch (Exception e) {
			throw new BusinessException("Could not retrive Delivery Matrix");
		}
		//update List<Delivery> deliveries
		try {
			deliveryControllerApi.updateDeliveries(deliveries);
		} catch (Exception e) {
			throw new BusinessException("Could not batch update Deliveries");
		}
	}

	public void onCompleteMatrix() {
		calculateMatrixStatus();
	}

	public void onClickMatrix() {
		matrixStatus="proceeding";
	}

	public Long getMatrix() {
		return matrix;
	}

	public void setMatrix(Long matrix) {
		this.matrix = matrix;
	}

	public Long getMatrixWithout() {
		return matrixWithout;
	}

	public void setMatrixWithout(Long matrixWithout) {
		this.matrixWithout = matrixWithout;
	}

	public Long getMatrixWith() {
		return matrixWith;
	}

	public void setMatrixWith(Long matrixWith) {
		this.matrixWith = matrixWith;
	}

	public String getMatrixStatus() {
		return matrixStatus;
	}

	public void setMatrixStatus(String matrixStatus) {
		this.matrixStatus = matrixStatus;
	}

	public void setProgressMatrixRemoteStations(Integer progressMatrixRemoteStations) {
		this.progressMatrixRemoteStations = progressMatrixRemoteStations;
	}

}
