package br.com.milkmoney.validation;

import br.com.milkmoney.model.CentroCusto;


public class CentroCustoValidation extends Validator {
	
	public static void validate(CentroCusto centroCusto) {
	
		Validator.validate(centroCusto);
		
	}
}
