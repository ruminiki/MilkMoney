package br.com.milksys.validation;

import java.util.Date;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Animal;

public class AnimalValidation extends Validator {
	
	public static void validate(Animal animal) {
	
		Validator.validate(animal);
		
		if (animal.getDataNascimento().after(new Date())){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de nascimento do animal não pode ser maior que a data atual");
		}
		
	}
}
