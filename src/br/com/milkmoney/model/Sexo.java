package br.com.milkmoney.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Sexo {

	public static final String FEMEA      = "F�MEA";
	public static final String MACHO      = "MACHO";
	public static final String INDEFINIDO = "INDEFINIDO";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(FEMEA, MACHO);
	}
	
	public static ObservableList<String> getAllItems(){
		return FXCollections.observableArrayList(FEMEA, MACHO, INDEFINIDO);
	}
}
