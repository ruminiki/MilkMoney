package br.com.milkmoney.validation;

import br.com.milkmoney.model.Fornecedor;


public class CompradorValidation extends Validator {
	
	public static void validate(Fornecedor fornecedor) {
	
		Validator.validate(fornecedor);
		
	}
}
