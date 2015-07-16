package br.com.milksys.validation;

import br.com.milksys.model.Parametro;


public class ParametroValidation extends Validator {
	
	public static void validate(Parametro parametro) {
	
		Validator.validate(parametro);
		
	}
}
