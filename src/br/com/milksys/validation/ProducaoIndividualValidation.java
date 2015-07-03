package br.com.milksys.validation;

import java.math.BigDecimal;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.ProducaoIndividual;
import br.com.milksys.model.Sexo;

public class ProducaoIndividualValidation extends Validator {
	
	public static void validate(ProducaoIndividual producaoIndividual) {
	
		Validator.validate(producaoIndividual);
		
		if ( producaoIndividual.getPrimeiraOrdenha().compareTo(BigDecimal.ZERO) <= 0 &&
				producaoIndividual.getSegundaOrdenha().compareTo(BigDecimal.ZERO) <= 0 &&
						producaoIndividual.getTerceiraOrdenha().compareTo(BigDecimal.ZERO) <= 0){
			
			throw new ValidationException(CAMPO_OBRIGATORIO, "� necess�rio informar pelo menos um valor para primeira ordenha, segunda ordenha ou terceira ordenha.");
			
		}
		
		if ( !producaoIndividual.getAnimal().getSexo().equals(Sexo.FEMEA) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, "Somente pode ser registrada a produ��o de animais f�meas.");
		}
		
	}
}
