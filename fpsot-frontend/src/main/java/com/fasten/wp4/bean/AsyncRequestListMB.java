package com.fasten.wp4.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.fasten.wp4.infra.async.AsyncCall;
import com.fasten.wp4.infra.async.AsyncRequestUtils;
import com.fasten.wp4.infra.model.Filter;

@Named
@ViewScoped
public class AsyncRequestListMB implements Serializable {
	
	@Inject
	List<AsyncCall> asyncCalls;
	
	LazyDataModel<AsyncCall> lazyItems;

	Filter<AsyncCall> filter = new Filter<>(new AsyncCall());

	List<AsyncCall> selecteds;
	
	AsyncCall selected;

	List<AsyncCall> filteredValues;

	public void init() {
		if(Faces.isAjaxRequest()){
			return;
		}
		selecteds = new ArrayList<AsyncCall>();
	}

	@PostConstruct
	public void initDataModel() {

		lazyItems = new LazyDataModel<AsyncCall>() {

			@Override
            public List<AsyncCall> load(int first, int pageSize,
                                  String sortField, SortOrder sortOrder,
                                  Map<String, Object> filters) {
                com.fasten.wp4.infra.model.SortOrder order = null;
                if (sortOrder != null) {
                    order = sortOrder.equals(SortOrder.ASCENDING) ? com.fasten.wp4.infra.model.SortOrder.ASCENDING
                            : sortOrder.equals(SortOrder.DESCENDING) ? com.fasten.wp4.infra.model.SortOrder.DESCENDING
                            : com.fasten.wp4.infra.model.SortOrder.UNSORTED;
                }
                filter.setFirst(first).setPageSize(pageSize)
                        .setSortField(sortField).setSortOrder(order)
                        .setParams(filters);
                List<AsyncCall> list = asyncCalls;//paginate(filter);
                setRowCount((int) asyncCalls.size());
                return list;
            }

			//select from list
			@Override
		    public AsyncCall getRowData(String key) {
				return this.getWrappedData().parallelStream().filter(entity -> key.equals(entity.getId().toString())).findAny().orElse(null);
		    }
		};
	}
	
	public String getStudyName(Object study) {
		return AsyncRequestUtils.getName(study);
	}
	
	public void clear() {
        filter = new Filter<AsyncCall>(new AsyncCall());
    }
	
	public List<AsyncCall> getSelecteds() {
		return selecteds;
	}

	public void setSelecteds(List<AsyncCall> selecteds) {
		this.selecteds = selecteds;
	}

	public List<AsyncCall> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<AsyncCall> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public LazyDataModel<AsyncCall> getLazyItems() {
		return lazyItems;
	}

	public void setLazyItems(LazyDataModel<AsyncCall> lazyItems) {
		this.lazyItems = lazyItems;
	}
	
	public AsyncCall getSelected() {
		return selected;
	}

	public void setSelected(AsyncCall selected) {
		this.selected = selected;
	}

	public void cancel() {
		selected.getCall().cancel();
		refresh();
	}
	
	public void refresh() {
		asyncCalls.forEach(asyncCall -> AsyncRequestUtils.update(asyncCall));
		asyncCalls.removeIf(asyncCall -> asyncCall.done());
	}

}
