package br.com.milksys.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ResponsavelServico {

	public static final String PROPRIETARIO = "PROPRIET�RIO";
	public static final String FUNCIONARIO = "FUNCION�RIO";
	public static final String PRESTADOR_SERVICO = "PRESTADOR SERVI�O";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(PROPRIETARIO, FUNCIONARIO, PRESTADOR_SERVICO);
	}
	
}
