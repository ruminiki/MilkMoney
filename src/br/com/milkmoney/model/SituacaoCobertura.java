package br.com.milkmoney.model;

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
	public static final String NAO_CONFIRMADA = "N�O CONFIRMADA";
	/**
	 * Teve o parto registrado.
	 */
	public static final String PARIDA     = "PARIDA";
	/**
	 * Teve o parto registrado.
	 */
	public static final String ABORTADA     = "ABORTADA";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(NAO_CONFIRMADA, PRENHA, VAZIA);
	}
	
	public static ObservableList<String> getAllItems(){
		return FXCollections.observableArrayList(NAO_CONFIRMADA, PRENHA, VAZIA, PARIDA, ABORTADA);
	}
	
}