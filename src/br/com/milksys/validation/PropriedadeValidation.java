package br.com.milksys.validation;

import br.com.milksys.model.Propriedade;


public class PropriedadeValidation extends Validator {
	
	public static void validate(Propriedade propriedade) {
	
		Validator.validate(propriedade);
		
	}
}
