package br.com.milksys.validation;

import java.math.BigDecimal;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.ProducaoIndividual;

public class ProducaoIndividualValidation extends Validator {
	
	public static void validate(ProducaoIndividual producaoIndividual) {
	
		Validator.validate(producaoIndividual);
		
		if ( producaoIndividual.getPrimeiraOrdenha().compareTo(BigDecimal.ZERO) <= 0 &&
				producaoIndividual.getSegundaOrdenha().compareTo(BigDecimal.ZERO) <= 0 &&
						producaoIndividual.getTerceiraOrdenha().compareTo(BigDecimal.ZERO) <= 0){
			
			throw new ValidationException(CAMPO_OBRIGATORIO, "É necessário informar pelo menos um valor para primeira ordenha, segunda ordenha ou terceira ordenha.");
			
		}
		
	}
}
