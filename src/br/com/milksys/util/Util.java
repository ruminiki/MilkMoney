package br.com.milksys.util;

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
	
	
}
