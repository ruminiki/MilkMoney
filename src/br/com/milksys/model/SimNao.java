package br.com.milksys.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class SimNao{
	
	public static final String SIM = "SIM";
	public static final String NAO = "NÃO";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(SIM, NAO);
	}
	
	
}