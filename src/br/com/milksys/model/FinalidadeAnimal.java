package br.com.milksys.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



public class FinalidadeAnimal {
	//finalidades
	public static final String PRODUCAO_LEITE = "PRODU��O DE LEITE";
	public static final String RECRIA_ENGORDA = "RECRIA/ENGORDA";
	public static final String REPRODUCAO = "REPRODU��O";
	public static final String SERVICO = "SERVI�O";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(PRODUCAO_LEITE, RECRIA_ENGORDA, REPRODUCAO, SERVICO);
	}

}