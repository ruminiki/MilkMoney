package br.com.milksys.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class TipoParto{
	
	public static final String CESARIA = "CESÁRIA";
	public static final String PARTO_NORMAL = "PARTO NORMAL";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(PARTO_NORMAL, CESARIA);
	}
	
}