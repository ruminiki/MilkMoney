package br.com.milksys.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class SituacaoAnimal{
	
	public static final String EM_LACTACAO = "EM LACTA��O";
	public static final String SECO = "SECO";
	public static final String EM_CRIACAO = "EM CRIA��O";
	public static final String VENDIDO = "VENDIDO";
	public static final String MORTO = "MORTO";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(EM_LACTACAO, VENDIDO, MORTO);
	}
	
	
}