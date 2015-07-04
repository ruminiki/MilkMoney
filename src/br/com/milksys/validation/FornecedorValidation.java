package br.com.milksys.validation;

import br.com.milksys.model.Fornecedor;


public class FornecedorValidation extends Validator {
	
	public static void validate(Fornecedor fornecedor) {
	
		Validator.validate(fornecedor);
		
	}
}
