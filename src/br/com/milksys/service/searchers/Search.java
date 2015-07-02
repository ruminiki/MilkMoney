package br.com.milksys.service.searchers;

import javafx.collections.ObservableList;

public abstract class Search<K, E> {
	
	public abstract ObservableList<E> doSearch();
	
}
