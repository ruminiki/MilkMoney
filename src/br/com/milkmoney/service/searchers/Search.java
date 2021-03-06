package br.com.milkmoney.service.searchers;

import javafx.collections.ObservableList;

public abstract class Search<K, E> {
	
	public abstract ObservableList<E> doSearch(Object ...objects);
	
}
