package br.com.milksys.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Platform;
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
        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    char ch = textField.getText().charAt(oldValue.intValue());
                    if (!(ch >= '0' && ch <= '9')) {
                        textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
                    }
                }
            }
        });
    }

    /**
     * Monta a mascara para Moeda.
     *
     * @param textField TextField
     */
    public static void moeda(final TextField textField) {
        //textField.setAlignment(Pos.CENTER_RIGHT);
        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                String value = textField.getText();
                value = value.replaceAll("[^0-9]", "");
                value = value.replaceAll("([0-9]{1})([0-9]{14})$", "$1.$2");
                value = value.replaceAll("([0-9]{1})([0-9]{11})$", "$1.$2");
                value = value.replaceAll("([0-9]{1})([0-9]{8})$", "$1.$2");
                value = value.replaceAll("([0-9]{1})([0-9]{5})$", "$1.$2");
                value = value.replaceAll("([0-9]{1})([0-9]{2})$", "$1,$2");
                textField.setText(value);
                positionCaret(textField);

                textField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (newValue.length() > 17)
                            textField.setText(oldValue);
                    }
                });
            }
        });

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean fieldChange) {
                if (!fieldChange) {
                    final int length = textField.getText().length();
                    if (length > 0 && length < 3) {
                        textField.setText(textField.getText() + "00");
                    }
                }
            }
        });
    }

    /**
     * Monta as mascara para CPF/CNPJ. A mascara eh exibida somente apos o campo perder o foco.
     *
     * @param textField TextField
     */
    public static void cpfCnpjField(final TextField textField) {

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean fieldChange) {
                String value = textField.getText();
                if (!fieldChange) {
                    if (textField.getText().length() == 11) {
                        value = value.replaceAll("[^0-9]", "");
                        value = value.replaceFirst("([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})$", "$1.$2.$3-$4");
                    }
                    if (textField.getText().length() == 14) {
                        value = value.replaceAll("[^0-9]", "");
                        value = value.replaceFirst("([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})$", "$1.$2.$3/$4-$5");
                    }
                }
                textField.setText(value);
                if (textField.getText() != value) {
                    textField.setText("");
                    textField.insertText(0, value);
                }

            }
        });

        maxField(textField, 18);
    }

    /**
     * Monta a mascara para os campos CNPJ.
     *
     * @param textField TextField
     */
    public static void cnpjField(final TextField textField) {
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Posiciona o cursor sempre a direita.
                textField.positionCaret(textField.getText().length());
            }
        });
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

}