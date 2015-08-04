package br.com.milkmoney.validation;

import br.com.milkmoney.model.Lote;


public class LoteValidation extends Validator {
	
	public static void validate(Lote lote) {
	
		Validator.validate(lote);
		
	}
}
