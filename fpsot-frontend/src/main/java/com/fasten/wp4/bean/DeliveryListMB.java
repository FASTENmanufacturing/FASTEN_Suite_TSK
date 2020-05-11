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
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.fasten.wp4.database.client.api.DeliveryControllerApi;
import com.fasten.wp4.database.client.api.PartControllerApi;
import com.fasten.wp4.database.client.api.RemoteStationControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.Delivery;
import com.fasten.wp4.database.client.model.PageOfDelivery;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class DeliveryListMB implements Serializable { 

	@Inject
	private transient DeliveryControllerApi deliveryControllerApi;

	@Inject
	private transient RemoteStationControllerApi remoteStationControllerApi;

	Long id;
	
	LazyDataModel<Delivery> deliverys;

	// datatable filteredValue attribute (column filters)
	List<Delivery> filteredValue;

	List<Delivery> selectedDeliverys;
	
	List<String> remoteStations; 

	public void init() {
		if(Faces.isAjaxRequest()){
			return;
		}
		selectedDeliverys = new ArrayList<Delivery>();
		
		try {
			remoteStations= remoteStationControllerApi.retrieveAllByName();
		} catch (ApiException e) {
			throw new BusinessException("Could not retrive list");
		}finally{
		}
	}

	@PostConstruct
	public void initDataModel() {

		deliverys = new LazyDataModel<Delivery>() {

			@Override
			public List<Delivery> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {

				if(has(id))
					filters.put("id",id+"");

				List<Delivery> list = Arrays.asList();
				try {
					PageOfDelivery page = deliveryControllerApi.retrieveDeliveryFilteredAndPaged(toApiFilters(filters), toApiPage(first, pageSize), toApiSize(pageSize), toApiSort( multiSortMeta));
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
			public Delivery getRowData(String key) {
				try {
					Delivery delivery = deliveryControllerApi.retrieveDelivery(new Long(key));
					selectedDeliverys.add(delivery);
					return delivery;
				} catch (ApiException e) {
					throw  new BusinessException("Not found with id " + id);
				}finally{
				}
			}
		};
	}

	public void delete() {
		int numDelivery = 0;
		for (Delivery selectedDelivery : selectedDeliverys) {
			numDelivery++;
			try {
				deliveryControllerApi.deleteDelivery(selectedDelivery.getId());
			} catch (ApiException e) {
				throw  new BusinessException("Delivery not found with id " + id);
			} finally {}
		}
		selectedDeliverys.clear();
		addDetailMessage(numDelivery + " delivery deleted successfully!");
	}
	
	public boolean filterByPrice(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if(filterText == null||filterText.equals("")) {
            return true;
        }
         
        if(value == null) {
            return false;
        }
         
        return false;
    }

	public List<Delivery> getSelectedDeliverys() {
		return selectedDeliverys;
	}

	public List<Delivery> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Delivery> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public void setSelectedDeliverys(List<Delivery> selectedDeliverys) {
		this.selectedDeliverys = selectedDeliverys;
	}

	public LazyDataModel<Delivery> getDeliverys() {
		return deliverys;
	}

	public void setDeliverys(LazyDataModel<Delivery> deliverys) {
		this.deliverys = deliverys;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<String> getRemoteStations() {
		return remoteStations;
	}
 
	
}
