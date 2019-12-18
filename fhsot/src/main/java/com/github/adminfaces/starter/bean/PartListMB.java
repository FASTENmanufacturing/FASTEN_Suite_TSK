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
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.PageOfPart;
import com.fasten.wp4.database.client.model.Part;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class PartListMB implements Serializable {

	@Inject
	private transient PartControllerApi partControllerApi;

	Long id;
	
	LazyDataModel<Part> parts;

	// datatable filteredValue attribute (column filters)
	List<Part> filteredValue;

	List<Part> selectedParts;

	@SuppressWarnings("unchecked")
	public void init() {
		if(Faces.isAjaxRequest()){
			return;
		}
		selectedParts = new ArrayList<Part>();
		
//		try {
//			parts= (LazyDataModel<Part>) partControllerApi.retrieveAllPart();
//		} catch (ApiException e) {
//			throw new BusinessException("Could not retrive list");
//		}finally{
//		}
	}

	@PostConstruct
	public void initDataModel() {

		parts = new LazyDataModel<Part>() {

			@Override
			public List<Part> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {

				if(has(id))
					filters.put("id",id+"");

				List<Part> list = Arrays.asList();
				try {
					PageOfPart page = partControllerApi.retrievePartFilteredAndPaged(toApiFilters(filters), toApiPage(first, pageSize), toApiSize(pageSize), toApiSort( multiSortMeta));
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
			public Part getRowData(String key) {
				try {
					Part part = partControllerApi.retrievePart(new Long(key));
					selectedParts.add(part);
					return part;
				} catch (ApiException e) {
					throw  new BusinessException("Not found with id " + id);
				}finally{
				}
			}
		};
	}

	public void delete() {
		int numPart = 0;
		for (Part selectedPart : selectedParts) {
			numPart++;
			try {
				partControllerApi.deletePart(selectedPart.getId());
			} catch (ApiException e) {
				throw  new BusinessException("Part not found with id " + id);
			} finally {}
		}
		selectedParts.clear();
		addDetailMessage(numPart + " part deleted successfully!");
	}

	public List<Part> getSelectedParts() {
		return selectedParts;
	}

	public List<Part> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Part> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public void setSelectedParts(List<Part> selectedParts) {
		this.selectedParts = selectedParts;
	}

	public LazyDataModel<Part> getParts() {
		return parts;
	}

	public void setParts(LazyDataModel<Part> parts) {
		this.parts = parts;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
