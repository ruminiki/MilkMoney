package br.com.milkmoney.validation;

import br.com.milkmoney.model.Fornecedor;


public class FornecedorValidation extends Validator {
	
	public static void validate(Fornecedor fornecedor) {
	
		Validator.validate(fornecedor);
		
	}
}
