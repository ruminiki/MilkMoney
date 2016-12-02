package br.com.milkmoney.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MetodoConfirmacaoPrenhez {
	
	public static final String ULTRASSONOGRAFIA = "ULTRASSONOGRAFIA";
	public static final String TOQUE            = "TOQUE";
	public static final String OBSERVACAO       = "OBSERVA��O";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(ULTRASSONOGRAFIA, TOQUE, OBSERVACAO);
	}
	
	
}