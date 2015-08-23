package br.com.milkmoney.validation;

import br.com.milkmoney.model.Compra;


public class CompraValidation extends Validator {
	
	public static void validate(Compra aquisicaoInsumo) {
	
		Validator.validate(aquisicaoInsumo);
		
	}
}
