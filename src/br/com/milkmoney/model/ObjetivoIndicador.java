package br.com.milkmoney.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ObjetivoIndicador {
	
	public static final String DENTRO_OU_ACIMA_DO_INTERVALO_IDEAL  = "DENTRO OU ACIMA DO INTERVALO IDEAL";
	public static final String DENTRO_OU_ABAIXO_DO_INTERVALO_IDEAL = "DENTRO OU ABAIXO DO INTERVALO IDEAL";
	
	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(DENTRO_OU_ACIMA_DO_INTERVALO_IDEAL, DENTRO_OU_ABAIXO_DO_INTERVALO_IDEAL);
	}

}