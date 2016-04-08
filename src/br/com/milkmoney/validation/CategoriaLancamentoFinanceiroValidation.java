package br.com.milkmoney.validation;

import br.com.milkmoney.model.CategoriaLancamentoFinanceiro;


public class CategoriaLancamentoFinanceiroValidation extends Validator {
	
	public static void validate(CategoriaLancamentoFinanceiro categoriaLancamentoFinanceiro) {
	
		Validator.validate(categoriaLancamentoFinanceiro);
		
	}
}
