package br.com.milksys.validation;

import java.math.BigDecimal;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.AnimalVendido;

public class AnimalVendidoValidation extends Validator {
	
	public static void validate(AnimalVendido animalVendido) {
	
		Validator.validate(animalVendido);
	
		if ( animalVendido.getValorAnimal().compareTo(BigDecimal.ZERO) <= 0 ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "O valor do animal deve ser maior que zero.");
		}
		
	}
}
