package br.com.milksys.validation;

import java.util.Date;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Sexo;

public class AnimalValidation extends Validator {
	
	public static void validate(Animal animal) {
	
		Validator.validate(animal);
		
		if (animal.getDataNascimento().after(new Date())){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de nascimento do animal não pode ser maior que a data atual.");
		}
		
		if ( animal.getSexo().equals(Sexo.MACHO) && animal.getDataUltimoParto() != null ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "Animais machos não podem ter registrada a data do último parto.");
		}
		
	}
}
