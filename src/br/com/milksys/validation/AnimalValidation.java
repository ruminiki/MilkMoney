package br.com.milksys.validation;

import br.com.milksys.model.Animal;

public class AnimalValidation extends Validator {
	
	public static void validate(Animal animal) {
	
		Validator.validate(animal);
		
	}
}
