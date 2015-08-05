package br.com.milkmoney.validation;

import br.com.milkmoney.model.TipoProcedimento;


public class TipoProcedimentoValidation extends Validator {
	
	public static void validate(TipoProcedimento tipoProcedimento) {
	
		Validator.validate(tipoProcedimento);
		
	}
}
