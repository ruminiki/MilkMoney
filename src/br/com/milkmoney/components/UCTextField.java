package br.com.milkmoney.components;

import java.util.HashSet;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class UCTextField extends TextField {

	String _mask;
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
	
	/**
	 * Adds a static mask to the specified text field.
	 * @param tf  the text field.
	 * @param mask  the mask to apply.
	 * Example of usage: addMask(txtDate, "  /  /    ");
	 */
	public void addMask(final String mask) {
	    this._mask = mask;
		setText(mask);
	    addTextLimiter(mask.length());

	    textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            String value = stripMask(getText(), mask);
	            setText(merge(value, mask));
	        }
	    });

	    setOnKeyPressed(new EventHandler<KeyEvent>() {
	        @Override
	        public void handle(final KeyEvent e) {
	            int caretPosition = getCaretPosition();
	            if (caretPosition < _mask.length()-1 && _mask.charAt(caretPosition) != ' ' && e.getCode() != KeyCode.BACK_SPACE && e.getCode() != KeyCode.LEFT) {
	                positionCaret(caretPosition + 1);
	            }
	        }
	    });
	    
	}

	private String merge(final String value, final String mask) {
	    final StringBuilder sb = new StringBuilder(mask);
	    int k = 0;
	    for (int i = 0; i < mask.length(); i++) {
	        if (mask.charAt(i) == ' ' && k < value.length()) {
	            sb.setCharAt(i, value.charAt(k));
	            k++;
	        }
	    }
	    return sb.toString();
	}

	private String stripMask(String text, final String mask) {
	    final Set<String> maskChars = new HashSet<>();
	    for (int i = 0; i < mask.length(); i++) {
	        char c = mask.charAt(i);
	        if (c != ' ') {
	            maskChars.add(String.valueOf(c));
	        }
	    }
	    for (String c : maskChars) {
	        text = text.replace(c, "");
	    }
	    return text;
	}

	public void addTextLimiter(final int maxLength) {
	    textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (getText().length() > maxLength) {
	                String s = getText().substring(0, maxLength);
	                setText(s);
	            }
	        }
	    });
	}
	
}
