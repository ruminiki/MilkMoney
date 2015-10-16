package br.com.milkmoney.model;

import br.com.milkmoney.service.searchers.Search;
@SuppressWarnings("rawtypes")
public class OptionChoiceFilter {

	private String filterName;
	private Object[] params;
	private Search search;
	
	public OptionChoiceFilter(String filterName, Search search, Object ...params) {
		this.filterName = filterName;
		this.params = params;
		this.search = search;
	}
	
	
	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}
	
	@Override
	public String toString() {
		return filterName;
	}
	
}
