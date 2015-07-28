package br.com.milkmoney.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MetodoConfirmacaoPrenhes {
	
	public static final String ULTRASONOGRAFIA = "ULTRASONOGRAFIA";
	public static final String TOQUE           = "TOQUE";
	public static final String OBSERVACAO      = "OBSERVA��O";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(ULTRASONOGRAFIA, TOQUE, OBSERVACAO);
	}
	
	
}