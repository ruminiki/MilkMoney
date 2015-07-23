package br.com.milkmoney.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class SituacaoNascimento{
	
	public static final String NASCIDO_VIVO = "NASCIDO VIVO";
	public static final String NASCIDO_MORTO = "NASCIDO MORTO";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(NASCIDO_VIVO, NASCIDO_MORTO);
	}
	
}