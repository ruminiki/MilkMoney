package br.com.milkmoney.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class MaskFieldUtil {

    private static List<KeyCode> ignoreKeyCodes;

    static {
        ignoreKeyCodes = new ArrayList<>();
        Collections.addAll(ignoreKeyCodes, new KeyCode[]{KeyCode.F1, KeyCode.F2, KeyCode.F3, KeyCode.F4, KeyCode.F5, KeyCode.F6, KeyCode.F7, KeyCode.F8, KeyCode.F9, KeyCode.F10, KeyCode.F11, KeyCode.F12});
    }

    public static void ignoreKeys(final TextField textField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (ignoreKeyCodes.contains(keyEvent.getCode())) {
                    keyEvent.consume();
                }
            }
        });
    }

    /**
     * Monta a mascara para Data (dd/MM/yyyy).
     *
     * @param textField TextField
     */
    public static void data(final TextField textField) {
        maxField(textField, 10);

        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() < 11) {
                    String value = textField.getText();
                    value = value.replaceAll("[^0-9]", "");
                    value = value.replaceFirst("(\\d{2})(\\d)", "$1/$2");
                    value = value.replaceFirst("(\\d{2})\\/(\\d{2})(\\d)", "$1/$2/$3");
                    textField.setText(value);
                    positionCaret(textField);
                }
            }
        });
    }
    
    /**
     * Monta a mascara para Telefone (##) ####-####.
     *
     * @param textField TextField
     */
    public static void telefone(final TextField textField) {
        maxField(textField, 14);

        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() < 15) {
                    String value = textField.getText();
                    value = value.replaceAll("[^0-9]", "");
                    value = value.replaceFirst("(\\d{0})(\\d)", "($1$2");
                    value = value.replaceFirst("(\\d{2})(\\d)", "$1) $2");
                    value = value.replaceFirst("(\\d{4})(\\d)", "$1-$2");
                    value = value.replaceFirst("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
                    textField.setText(value);
                    positionCaret(textField);
                }
            }
        });
    }
    
    /**
     * Monta a mascara para jornada ##:##-##:## ##:##-##:##.
     *
     * @param textField TextField
     */
    public static void jornada(final TextField textField) {
        maxField(textField, 23);

        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() < 24) {
                    String value = textField.getText();
                    value = value.replaceAll("[^0-9]", "");
                    value = value.replaceFirst("(\\d{2})(\\d)", "$1:$2");
                    value = value.replaceFirst("(\\d{2})(\\d)", "$1-$2");
                    value = value.replaceFirst("(\\d{2})(\\d)", "$1:$2");
                    value = value.replaceFirst("(\\d{2})(\\d)", "$1 $2");
                    value = value.replaceFirst("(\\d{2})(\\d)", "$1:$2");
                    value = value.replaceFirst("(\\d{2})(\\d)", "$1-$2");
                    value = value.replaceFirst("(\\d{2})(\\d)", "$1:$2");
                    value = value.replaceFirst("(\\d{2})", "$1");
                    value = value.replaceFirst("(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})", "$1:$2-$3:$4 $5:$6-$7:$8");
                    textField.setText(value);
                    positionCaret(textField);
                }
            }
        });
    }

    /**
     * Campo que aceita somente numericos.
     *
     * @param textField TextField
     */
    public static void numeroInteiro(final TextField textField) {
    	textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String value = textField.getText();
				value = value.replaceAll("[^0-9]", "");
				textField.setText(value);
				positionCaret(textField);
			}
		});
    }

    /**
     * Monta a mascara para Moeda.
     *
     * @param textField TextField
     */
    public static void decimal(final TextField textField) {
    	
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            	if ( oldValue != null && newValue != null &&
            			!oldValue.replace(",", "").equals(newValue.replace(",", "")) ){
            		String value = textField.getText();
                    value = value.replaceAll("[^0-9]", "");
                    value = value.replaceAll("([0-9]{1})([0-9]{2})$", "$1,$2");
                    textField.setText(value);
                    positionCaret(textField);
                }
            }
                
        });
        
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if ( textField != null && textField.getText() != null ){
					if ( textField.getText().length() == 3 && textField.getText().contains(",") ){
						textField.setText(textField.getText()+"0");
					}
					if ( textField.getText().length() == 2 ){ 
						if ( textField.getText().contains(",") ){
							textField.setText(textField.getText()+"00");
						}else{
							textField.setText(textField.getText()+",00");
						}
					}
					if ( textField.getText().length() == 1 ){
						textField.setText(textField.getText()+",00");
					}
				}
			}

		});
        
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
	
			@Override
			public void handle(KeyEvent event) {
	
				if (event.getCode().equals(KeyCode.BACK_SPACE) || event.getCode().equals(KeyCode.DELETE) ) {
					textField.setText("");
				}
	
			}
	
		});

    }

    /**
     * Monta as mascara para CPF/CNPJ. A mascara eh exibida somente apos o campo perder o foco.
     * CPF  (###.###.###-##)
     * CNPJ (##.###.###/####-##)
     * @param textField TextField
     */
    public static void cpfCnpj(final TextField textField) {
    	
    	 maxField(textField, 18);

         textField.lengthProperty().addListener(new ChangeListener<Number>() {
             @Override
             public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            	 if (newValue.intValue() < 15) {
                     String value = textField.getText();
                     value = value.replaceAll("[^0-9]", "");
                     value = value.replaceFirst("(\\d{3})(\\d)", "$1.$2");
                     value = value.replaceFirst("(\\d{3})(\\d)", "$1.$2");
                     value = value.replaceFirst("(\\d{3})(\\d)", "$1-$2");
                     value = value.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
                     textField.setText(value);
                     positionCaret(textField);
                 }else{
                	 String value = textField.getText();
                     value = value.replaceAll("[^0-9]", "");
                     value = value.replaceFirst("(\\d{2})(\\d)", "$1.$2");
                     value = value.replaceFirst("(\\d{2})\\.(\\d{3})(\\d)", "$1.$2.$3");
                     value = value.replaceFirst("\\.(\\d{3})(\\d)", ".$1/$2");
                     value = value.replaceFirst("(\\d{4})(\\d)", "$1-$2");
                     textField.setText(value);
                     positionCaret(textField);
                 }
             }
         });
    }
    
    /**
     * Máscara ###.###.###-##
     * @param textField TextField
     */
    public static void cpf(final TextField textField) {
    	  maxField(textField, 14);

          textField.lengthProperty().addListener(new ChangeListener<Number>() {
              @Override
              public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                  if (newValue.intValue() < 15) {
                      String value = textField.getText();
                      value = value.replaceAll("[^0-9]", "");
                      value = value.replaceFirst("(\\d{3})(\\d)", "$1.$2");
                      value = value.replaceFirst("(\\d{3})(\\d)", "$1.$2");
                      value = value.replaceFirst("(\\d{3})(\\d)", "$1-$2");
                      value = value.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
                      textField.setText(value);
                      positionCaret(textField);
                  }
              }
          });
    }

    /**
     * Monta a mascara para os campos CNPJ.
     *
     * @param textField TextField
     */
    public static void cnpj(final TextField textField) {
        maxField(textField, 18);

        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                String value = textField.getText();
                value = value.replaceAll("[^0-9]", "");
                value = value.replaceFirst("(\\d{2})(\\d)", "$1.$2");
                value = value.replaceFirst("(\\d{2})\\.(\\d{3})(\\d)", "$1.$2.$3");
                value = value.replaceFirst("\\.(\\d{3})(\\d)", ".$1/$2");
                value = value.replaceFirst("(\\d{4})(\\d)", "$1-$2");
                textField.setText(value);
                positionCaret(textField);
            }
        });

    }

    /**
     * Devido ao incremento dos caracteres das mascaras eh necessario que o cursor sempre se posicione no final da string.
     *
     * @param textField TextField
     */
    private static void positionCaret(final TextField textField) {
        // Posiciona o cursor sempre a direita.
        //textField.positionCaret(textField.getText().length());
    }

    /**
     * @param textField TextField.
     * @param length    Tamanho do campo.
     */
    private static void maxField(final TextField textField, final Integer length) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue.length() > length)
                    textField.setText(oldValue);
            }
        });
    }

	public static void decimalWithoutMask(TextField textField) {
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
				String value = textField.getText();
				value = value.replaceAll("[^0-9,]", "");
				textField.setText(value);
				positionCaret(textField);
			}

		});
		
	}

}