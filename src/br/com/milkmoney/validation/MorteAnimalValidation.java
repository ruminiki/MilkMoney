package br.com.milkmoney.validation;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.MorteAnimal;
import br.com.milkmoney.model.SituacaoAnimal;

public class MorteAnimalValidation extends Validator {
	
	public static void validate(MorteAnimal morteAnimal) {
	
		Validator.validate(morteAnimal);
		
		if ( morteAnimal.getDataMorte().before(morteAnimal.getAnimal().getDataNascimento())){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data da morte não pode ser menor que a data de nascimento do animal.");
		}
		
		/*if ( morteAnimal.getAnimal().getDataUltimaCobertura() != null &&
				morteAnimal.getDataMorte().before(morteAnimal.getAnimal().getDataUltimaCobertura())){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data da morte não pode ser menor que a data da última cobertura do animal.");
		}
		
		if ( morteAnimal.getAnimal().getDataUltimoParto() != null &&
				morteAnimal.getDataMorte().before(morteAnimal.getAnimal().getDataUltimoParto())){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data da morte não pode ser menor que a data do último parto do animal.");
		}*/
		
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
