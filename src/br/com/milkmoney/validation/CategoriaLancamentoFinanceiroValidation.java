package br.com.milkmoney.validation;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.CategoriaLancamentoFinanceiro;


public class CategoriaLancamentoFinanceiroValidation extends Validator {
	
	public static void validate(CategoriaLancamentoFinanceiro categoriaLancamentoFinanceiro) {
	
		Validator.validate(categoriaLancamentoFinanceiro);
		
		if ( categoriaLancamentoFinanceiro.getCategoriaSuperiora() != null &&
				categoriaLancamentoFinanceiro.getCategoriaSuperiora().getId() == categoriaLancamentoFinanceiro.getId() ){
			throw new ValidationException(REGRA_NEGOCIO, "A categoria n�o pode ser uma subcategoria dela mesma. Por favor, selecione outra categoria.");
		}
		
		//verifica se h� uma refer�ncia ciclica entre dois itens
		//Ex: A categoria superiora de A � B e de B � A
		if ( categoriaLancamentoFinanceiro.getCategoriaSuperiora() != null && 
				categoriaLancamentoFinanceiro.getCategoriaSuperiora().getCategoriaSuperiora() != null &&
				//verifica se a categoria superiora de A � B e se a categoria superiora de B � A
				categoriaLancamentoFinanceiro.getCategoriaSuperiora().getCategoriaSuperiora().getId() == categoriaLancamentoFinanceiro.getId() ){
			throw new ValidationException(REGRA_NEGOCIO, "N�o pode haver uma refer�ncia c�clica entre duas categorias, onde uma � superiora da outra. Por favor, selecione outra categoria para ser a superiora.");
		}
		
	}
}
