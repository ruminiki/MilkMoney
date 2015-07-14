package br.com.milksys.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ComplicacaoParto{
	
	public static final String DESLOCAMENTO_ABOMASSO = "DESLOCAMENTO DE ABOMASSO";
	public static final String RETENCAO_PLACENTA = "RETENÇÃO DE PLACENTA";
	public static final String NENHUMA = "NENHUMA";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(NENHUMA, RETENCAO_PLACENTA, DESLOCAMENTO_ABOMASSO);
	}
	
}