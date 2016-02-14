package br.com.milkmoney.validation;

import br.com.milkmoney.model.Indicador;


public class IndicadorValidation extends Validator {
	
	public static void validate(Indicador indicador) {
	
		Validator.validate(indicador);
		
	}
}
