package br.com.milkmoney.model;

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
	public static final String NAO_CONFIRMADA = "NÃO CONFIRMADA";
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