package br.com.milkmoney.validation;

import br.com.milkmoney.model.Insumo;


public class InsumoValidation extends Validator {
	
	public static void validate(Insumo insumo) {
	
		Validator.validate(insumo);
		
	}
}
