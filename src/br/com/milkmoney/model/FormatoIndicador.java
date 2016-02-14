package br.com.milkmoney.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class FormatoIndicador {
	
	public static final String INTEIRO_FORMAT            = "NÚMERO INTEIRO";
	public static final String DECIMAL_FORMAT_UMA_CASA   = "DECIMAL UMA CASA";
	public static final String DECIMAL_FORMAT_DUAS_CASAS = "DECIMAL DUAS_CASAS";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(INTEIRO_FORMAT,
												 DECIMAL_FORMAT_UMA_CASA,
												 DECIMAL_FORMAT_DUAS_CASAS);
	}

}