package br.com.milkmoney.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



public class MotivoEncerramentoLactacao{

	public static final String PREPARACAO_PARTO = "PREPARA��O PARA PARTO";
	public static final String DOENCA           = "DOEN�A";
	public static final String ACIDENTE         = "ACIDENTE";
	public static final String ENGORDA          = "ENGORDA";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(PREPARACAO_PARTO, DOENCA, ACIDENTE, ENGORDA);
	}
	
}