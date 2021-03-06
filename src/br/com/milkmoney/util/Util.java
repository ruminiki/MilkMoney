package br.com.milkmoney.util;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Util {

	public static ObservableList<Number> generateListNumbers(int start, int end){
		
		List<Number> list = new ArrayList<Number>();
		
		for (int i = start; i <= end; i++) {
			list.add(i);
		}
		
		return FXCollections.observableArrayList(list);
		
	}
	
	public static ObservableList<String> generateListMonths(){
		ObservableList<String> optionsMesReferencia = FXCollections.observableArrayList();
		optionsMesReferencia.add("JANEIRO");
		optionsMesReferencia.add("FEVEREIRO");
		optionsMesReferencia.add("MAR�O");
		optionsMesReferencia.add("ABRIL");
		optionsMesReferencia.add("MAIO");
		optionsMesReferencia.add("JUNHO");
		optionsMesReferencia.add("JULHO");
		optionsMesReferencia.add("AGOSTO");
		optionsMesReferencia.add("SETEMBRO");
		optionsMesReferencia.add("OUTUBRO");
		optionsMesReferencia.add("NOVEMBRO");
		optionsMesReferencia.add("DEZEMBRO");
		return optionsMesReferencia;
	}
	
	public static ObservableList<String> generateListMonthsAbrev(){
		ObservableList<String> optionsMesReferencia = FXCollections.observableArrayList();
		optionsMesReferencia.add("JAN");
		optionsMesReferencia.add("FEV");
		optionsMesReferencia.add("MAR");
		optionsMesReferencia.add("ABR");
		optionsMesReferencia.add("MAI");
		optionsMesReferencia.add("JUN");
		optionsMesReferencia.add("JUL");
		optionsMesReferencia.add("AGO");
		optionsMesReferencia.add("SET");
		optionsMesReferencia.add("OUT");
		optionsMesReferencia.add("NOV");
		optionsMesReferencia.add("DEZ");
		return optionsMesReferencia;
	}
	
}
