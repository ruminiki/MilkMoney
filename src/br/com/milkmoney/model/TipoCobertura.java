package br.com.milkmoney.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



public class TipoCobertura{

	public static final String MONTA_NATURAL = "MONTA NATURAL";
	public static final String ENSEMINACAO_ARTIFICIAL = "ENSEMINAÇÃO ARTIFICIAL";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(MONTA_NATURAL, ENSEMINACAO_ARTIFICIAL);
	}
	
}