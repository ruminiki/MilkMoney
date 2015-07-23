package br.com.milkmoney.validation;

import java.math.BigDecimal;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.AnimalVendido;

public class AnimalVendidoValidation extends Validator {
	
	public static void validate(AnimalVendido animalVendido) {
	
		Validator.validate(animalVendido);
	
		if ( animalVendido.getValorAnimal().compareTo(BigDecimal.ZERO) <= 0 ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "O valor do animal deve ser maior que zero.");
		}
		
	}
}
