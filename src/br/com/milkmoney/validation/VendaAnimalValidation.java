package br.com.milkmoney.validation;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.SituacaoAnimal;
import br.com.milkmoney.model.VendaAnimal;

public class VendaAnimalValidation extends Validator {
	
	public static void validate(VendaAnimal vendaAnimal) {
		Validator.validate(vendaAnimal);
	}
	
	public static void validateAddAnimal(VendaAnimal vendaAnimal, Animal animalAdicionado) {

		if ( animalAdicionado == null ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "O animal selecionado é inválido. Por favor, selecione o animal novamente.");
		}
		
		validaSituacaoAnimal(animalAdicionado);
		
	}
	
	public static void validaSituacaoAnimal(Animal animal){
		if ( animal.getSituacaoAnimal() != null ){
			if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.MORTO) ){
				throw new ValidationException(VALIDACAO_FORMULARIO, "O animal selecionado já teve a morte registrada. Por favor, selecione outro animal para continuar.");
			}
			if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.VENDIDO) ){
				throw new ValidationException(VALIDACAO_FORMULARIO, "O animal selecionado já teve a venda registrada. Por favor, selecione outro animal para continuar.");
			}
		}
	}
	

}
