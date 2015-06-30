package br.com.milksys.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SituacaoCobertura {
	
	public static final String PRENHA = "PRENHA";
	public static final String VAZIA = "VAZIA";
	public static final String INDEFINIDA = "INDEFINIDA";
	public static final String PARIDA = "PARIDA";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(INDEFINIDA, PRENHA, VAZIA);
	}
	
}