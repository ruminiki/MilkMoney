package br.com.milksys.components;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class UCTextField extends TextField {

	public UCTextField() {
		
		this.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,	String oldValue, String newValue) {
	                if ( newValue != null && 	(oldValue == null || !(newValue.toUpperCase().equals(oldValue.toUpperCase()))) ){
	                	setText(newValue.toUpperCase());
	            }
			}
            
		});
				
	}
	
}
