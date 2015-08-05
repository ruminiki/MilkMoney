package br.com.milkmoney.validation;

import br.com.milkmoney.model.Procedimento;


public class ProcedimentoValidation extends Validator {
	
	public static void validate(Procedimento procedimento) {
	
		Validator.validate(procedimento);
		
	}
}
