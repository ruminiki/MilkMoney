package br.com.milkmoney.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



public class TipoLancamentoFinanceiro{

	public static final String RECEITA = "RECEITA";
	public static final String DESPESA = "DESPESA";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(RECEITA, DESPESA);
	}
	
}