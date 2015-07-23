package br.com.milkmoney.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DestinacaoAnimal {

	public static final String ABATE    = "ABATE";
	public static final String CRIA     = "CRIA";
	public static final String RECRIA   = "RECRIA";
	public static final String PRODUCAO = "PRODUÇÃO";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(ABATE, CRIA, RECRIA, PRODUCAO);
	}
	
}
