package br.com.milkmoney.validation;

import br.com.milkmoney.model.TipoInsumo;


public class TipoInsumoValidation extends Validator {
	
	public static void validate(TipoInsumo tipoInsumo) {
	
		Validator.validate(tipoInsumo);
		
	}
}
