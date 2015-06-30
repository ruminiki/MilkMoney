package br.com.milksys.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ResponsavelServico {

	public static final String PROPRIETARIO = "PROPRIETÁRIO";
	public static final String FUNCIONARIO = "FUNCIONÁRIO";
	public static final String PRESTADOR_SERVICO = "PRESTADOR SERVIÇO";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(PROPRIETARIO, FUNCIONARIO, PRESTADOR_SERVICO);
	}
	
}
