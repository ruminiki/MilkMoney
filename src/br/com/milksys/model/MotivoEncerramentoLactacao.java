package br.com.milksys.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



public class MotivoEncerramentoLactacao{

	public static final String PREPARACAO_PARTO = "PREPARAÇÃO PARA PARTO";
	public static final String DOENCA = "DOENÇA";
	public static final String ACIDENTE = "ACIDENTE";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(PREPARACAO_PARTO, DOENCA, ACIDENTE);
	}
	
}