package br.com.milksys.validation;

import br.com.milksys.model.Indicador;


public class IndicadorValidation extends Validator {
	
	public static void validate(Indicador indicador) {
	
		Validator.validate(indicador);
		
	}
}
