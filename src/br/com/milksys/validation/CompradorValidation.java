package br.com.milksys.validation;

import br.com.milksys.model.Fornecedor;


public class CompradorValidation extends Validator {
	
	public static void validate(Fornecedor fornecedor) {
	
		Validator.validate(fornecedor);
		
	}
}
