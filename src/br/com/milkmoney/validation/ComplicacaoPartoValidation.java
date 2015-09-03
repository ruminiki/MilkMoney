package br.com.milkmoney.validation;

import br.com.milkmoney.model.ComplicacaoParto;


public class ComplicacaoPartoValidation extends Validator {
	
	public static void validate(ComplicacaoParto complicacaoParto) {
	
		Validator.validate(complicacaoParto);
		
	}
}
