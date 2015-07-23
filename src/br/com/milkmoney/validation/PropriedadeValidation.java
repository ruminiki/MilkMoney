package br.com.milkmoney.validation;

import br.com.milkmoney.model.Propriedade;


public class PropriedadeValidation extends Validator {
	
	public static void validate(Propriedade propriedade) {
	
		Validator.validate(propriedade);
		
	}
}
