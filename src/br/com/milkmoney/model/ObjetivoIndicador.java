package br.com.milkmoney.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ObjetivoIndicador {
	
	public static final String ACIMA_VALOR_REFERENCIA  = "ACIMA VALOR REFERÊNCIA";
	public static final String ABAIXO_VALOR_REFERENCIA = "ABAIXO VALOR REFERÊNCIA";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(ACIMA_VALOR_REFERENCIA, ABAIXO_VALOR_REFERENCIA);
	}

}