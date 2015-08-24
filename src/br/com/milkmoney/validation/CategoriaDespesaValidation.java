package br.com.milkmoney.validation;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.CategoriaDespesa;


public class CategoriaDespesaValidation extends Validator {
	
	public static void validate(CategoriaDespesa categoriaDespesa) {
	
		Validator.validate(categoriaDespesa);
		
		if ( categoriaDespesa.getCategoriaSuperiora() != null &&
				categoriaDespesa.getCategoriaSuperiora().getId() == categoriaDespesa.getId() ){
			throw new ValidationException(REGRA_NEGOCIO, "A categoria não pode ser uma subcategoria dela mesma. Por favor, selecione outra categoria.");
		}
		
		//verifica se há uma referência ciclica entre dois itens
		//Ex: A categoria superiora de A é B e de B é A
		if ( categoriaDespesa.getCategoriaSuperiora() != null && 
				categoriaDespesa.getCategoriaSuperiora().getCategoriaSuperiora() != null &&
				//verifica se a categoria superiora de A é B e se a categoria superiora de B é A
				categoriaDespesa.getCategoriaSuperiora().getCategoriaSuperiora().getId() == categoriaDespesa.getId() ){
			throw new ValidationException(REGRA_NEGOCIO, "Não pode haver uma referência cíclica entre duas categorias, onde uma é superiora da outra. Por favor, selecione outra categoria para ser a superiora.");
		}
		
	}
}
