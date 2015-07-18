package br.com.milksys.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SituacaoCobertura {
	
	/**
	 * A vaca est� prenha.
	 */
	public static final String PRENHA     = "PRENHA";
	/**
	 * A cobertura n�o foi bem sucedida e o animal deve repetir o cio.
	 */
	public static final String VAZIA      = "VAZIA";
	/**
	 * N�o � poss�vel afirmar se a cobertura foi bem sucedida e a vaca est� prenha.
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