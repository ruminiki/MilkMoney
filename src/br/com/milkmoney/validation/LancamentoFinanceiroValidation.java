package br.com.milkmoney.validation;

import br.com.milkmoney.model.LancamentoFinanceiro;


public class LancamentoFinanceiroValidation extends Validator {
	
	public static void validate(LancamentoFinanceiro lancamentoFinanceiro) {
	
		Validator.validate(lancamentoFinanceiro);
		
	}
}
