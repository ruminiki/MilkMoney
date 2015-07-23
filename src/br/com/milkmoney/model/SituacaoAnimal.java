package br.com.milkmoney.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SituacaoAnimal{
	
	public static final String EM_LACTACAO = "EM LACTAÇÃO";
	public static final String SECO = "SECA";
	public static final String VENDIDO = "VENDIDA";
	public static final String MORTO = "MORTA";
	public static final String NAO_DEFINIDA = "NÃO DEFINIDA";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(EM_LACTACAO, SECO, VENDIDO, MORTO, NAO_DEFINIDA);
	}
	
}

