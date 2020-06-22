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
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.fasten.wp4.database.client.api.DemandControllerApi;
import com.fasten.wp4.database.client.api.PartControllerApi;
import com.fasten.wp4.database.client.api.DistributionCenterControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.Demand;
import com.fasten.wp4.database.client.model.PageOfDemand;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class DemandListMB implements Serializable {

	@Inject
	private transient DemandControllerApi demandControllerApi;
	
	@Inject
	private transient PartControllerApi partControllerApi;

	@Inject
	private transient DistributionCenterControllerApi distributionCenterControllerApi;

	Long id;
	
	LazyDataModel<Demand> demands;

	// datatable filteredValue attribute (column filters)
	List<Demand> filteredValue;

	List<Demand> selectedDemands;
	
	List<String> distributionCenters;
	
	List<String> parts;

	public void init() {
		if(Faces.isAjaxRequest()){
			return;
		}
		selectedDemands = new ArrayList<Demand>();
		
		try {
			parts= partControllerApi.retrieveAllDistinctByName();
			distributionCenters= distributionCenterControllerApi.retrieveAllDistributionCenterByName();
		} catch (ApiException e) {
			throw new BusinessException("Could not retrive list");
		}finally{
		}
	}

	@PostConstruct
	public void initDataModel() {

		demands = new LazyDataModel<Demand>() {

			@Override
			public List<Demand> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {

				if(has(id))
					filters.put("id",id+"");

				List<Demand> list = Arrays.asList();
				try {
					PageOfDemand page = demandControllerApi.retrieveDemandFilteredAndPaged(toApiFilters(filters), toApiPage(first, pageSize), toApiSize(pageSize), toApiSort( multiSortMeta));
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
			public Demand getRowData(String key) {
				try {
					Demand demand = demandControllerApi.retrieveDemand(new Long(key));
					selectedDemands.add(demand);
					return demand;
				} catch (ApiException e) {
					throw  new BusinessException("Not found with id " + id);
				}finally{
				}
			}
		};
	}

	public void delete() {
		int numDemand = 0;
		for (Demand selectedDemand : selectedDemands) {
			numDemand++;
			try {
				demandControllerApi.deleteDemand(selectedDemand.getId());
			} catch (ApiException e) {
				throw  new BusinessException("Demand not found with id " + id);
			} finally {}
		}
		selectedDemands.clear();
		addDetailMessage(numDemand + " demand deleted successfully!");
	}

	public List<Demand> getSelectedDemands() {
		return selectedDemands;
	}

	public List<Demand> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Demand> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public void setSelectedDemands(List<Demand> selectedDemands) {
		this.selectedDemands = selectedDemands;
	}

	public LazyDataModel<Demand> getDemands() {
		return demands;
	}

	public void setDemands(LazyDataModel<Demand> demands) {
		this.demands = demands;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<String> getDistributionCenters() {
		return distributionCenters;
	}

	public List<String> getParts() {
		return parts;
	}
	
}
