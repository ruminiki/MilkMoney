package br.com.milkmoney.validation;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.CategoriaLancamentoFinanceiro;


public class CategoriaLancamentoFinanceiroValidation extends Validator {
	
	public static void validate(CategoriaLancamentoFinanceiro categoriaLancamentoFinanceiro) {
	
		Validator.validate(categoriaLancamentoFinanceiro);
		
		if ( categoriaLancamentoFinanceiro.getCategoriaSuperiora() != null &&
				categoriaLancamentoFinanceiro.getCategoriaSuperiora().getId() == categoriaLancamentoFinanceiro.getId() ){
			throw new ValidationException(REGRA_NEGOCIO, "A categoria não pode ser uma subcategoria dela mesma. Por favor, selecione outra categoria.");
		}
		
		//verifica se há uma referência ciclica entre dois itens
		//Ex: A categoria superiora de A é B e de B é A
		if ( categoriaLancamentoFinanceiro.getCategoriaSuperiora() != null && 
				categoriaLancamentoFinanceiro.getCategoriaSuperiora().getCategoriaSuperiora() != null &&
				//verifica se a categoria superiora de A é B e se a categoria superiora de B é A
				categoriaLancamentoFinanceiro.getCategoriaSuperiora().getCategoriaSuperiora().getId() == categoriaLancamentoFinanceiro.getId() ){
			throw new ValidationException(REGRA_NEGOCIO, "Não pode haver uma referência cíclica entre duas categorias, onde uma é superiora da outra. Por favor, selecione outra categoria para ser a superiora.");
		}
		
	}
}
