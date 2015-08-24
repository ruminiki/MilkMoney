package br.com.milkmoney.validation;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.CategoriaDespesa;


public class CategoriaDespesaValidation extends Validator {
	
	public static void validate(CategoriaDespesa categoriaDespesa) {
	
		Validator.validate(categoriaDespesa);
		
		if ( categoriaDespesa.getCategoriaSuperiora() != null &&
				categoriaDespesa.getCategoriaSuperiora().getId() == categoriaDespesa.getId() ){
			throw new ValidationException(REGRA_NEGOCIO, "A categoria n�o pode ser uma subcategoria dela mesma. Por favor, selecione outra categoria.");
		}
		
		//verifica se h� uma refer�ncia ciclica entre dois itens
		//Ex: A categoria superiora de A � B e de B � A
		if ( categoriaDespesa.getCategoriaSuperiora() != null && 
				categoriaDespesa.getCategoriaSuperiora().getCategoriaSuperiora() != null &&
				//verifica se a categoria superiora de A � B e se a categoria superiora de B � A
				categoriaDespesa.getCategoriaSuperiora().getCategoriaSuperiora().getId() == categoriaDespesa.getId() ){
			throw new ValidationException(REGRA_NEGOCIO, "N�o pode haver uma refer�ncia c�clica entre duas categorias, onde uma � superiora da outra. Por favor, selecione outra categoria para ser a superiora.");
		}
		
	}
}
