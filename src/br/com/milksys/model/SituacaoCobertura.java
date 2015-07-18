package br.com.milksys.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SituacaoCobertura {
	
	/**
	 * A vaca está prenha.
	 */
	public static final String PRENHA     = "PRENHA";
	/**
	 * A cobertura não foi bem sucedida e o animal deve repetir o cio.
	 */
	public static final String VAZIA      = "VAZIA";
	/**
	 * Não é possível afirmar se a cobertura foi bem sucedida e a vaca está prenha.
	 */
	public static final String INDEFINIDA = "INDEFINIDA";
	/**
	 * Teve o parto registrado.
	 */
	public static final String PARIDA     = "PARIDA";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(INDEFINIDA, PRENHA, VAZIA);
	}
	
}