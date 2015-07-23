package br.com.milkmoney.validation;

import br.com.milkmoney.model.Parametro;


public class ParametroValidation extends Validator {
	
	public static void validate(Parametro parametro) {
	
		Validator.validate(parametro);
		
	}
}
