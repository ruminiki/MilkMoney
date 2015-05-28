package br.com.milksys.components;

import javafx.scene.control.TextField;

public class UCTextField extends TextField {

	public UCTextField() {
		
		this.textProperty().addListener((event) -> {this.setText(this.getText().toUpperCase());});
				
	}


}
