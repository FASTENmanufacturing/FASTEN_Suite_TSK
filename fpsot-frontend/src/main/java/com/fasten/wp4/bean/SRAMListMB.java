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

import com.fasten.wp4.database.client.api.SramControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.PageOfSRAM;
import com.fasten.wp4.database.client.model.SRAM;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class SRAMListMB implements Serializable {

	@Inject
	private transient SramControllerApi sramControllerApi;

	Long id;
	
	LazyDataModel<SRAM> srams;

	// datatable filteredValue attribute (column filters)
	List<SRAM> filteredValue;

	List<SRAM> selectedSRAMs;

	@SuppressWarnings("unchecked")
	public void init() {
		if(Faces.isAjaxRequest()){
			return;
		}
		selectedSRAMs = new ArrayList<SRAM>(); 
	}

	@PostConstruct
	public void initDataModel() {

		srams = new LazyDataModel<SRAM>() {

			@Override
			public List<SRAM> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {

				if(has(id))
					filters.put("id",id+"");

				List<SRAM> list = Arrays.asList();
				try {
					PageOfSRAM page = sramControllerApi.retrieveSRAMFilteredAndPaged(toApiFilters(filters), toApiPage(first, pageSize), toApiSize(pageSize), toApiSort( multiSortMeta));
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
			public SRAM getRowData(String key) {
					return this.getWrappedData().parallelStream().filter(entity -> key.equals(entity.getId().toString())).findAny().orElse(null);
			}
		};
	}

	public void delete() {
		int numSRAM = 0;
		for (SRAM selectedSRAM : selectedSRAMs) {
			numSRAM++;
			try {
				sramControllerApi.deleteSRAM(selectedSRAM.getId());
			} catch (ApiException e) {
				throw  new BusinessException("SRAM not found with id " + id);
			} finally {}
		}
		selectedSRAMs.clear();
		addDetailMessage(numSRAM + " sram deleted successfully!");
	}

	public List<SRAM> getSelectedSRAMs() {
		return selectedSRAMs;
	}

	public List<SRAM> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<SRAM> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public void setSelectedSRAMs(List<SRAM> selectedSRAMs) {
		this.selectedSRAMs = selectedSRAMs;
	}

	public LazyDataModel<SRAM> getSRAMs() {
		return srams;
	}

	public void setSRAMs(LazyDataModel<SRAM> srams) {
		this.srams = srams;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LazyDataModel<SRAM> getSrams() {
		return srams;
	}
	
}
