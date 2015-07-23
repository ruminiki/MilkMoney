package br.com.milkmoney.validation;

import java.math.BigDecimal;
import java.util.Date;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.ProducaoIndividual;
import br.com.milkmoney.model.Sexo;

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
		
		if ( producaoIndividual.getData().after(new Date()) ){
			throw new ValidationException(REGRA_NEGOCIO, "A data do lan�amento da produ��o n�o pode ser maior que a data atual. Por favor, corrija e tente novamente.");
		}
		 
		
		
	}

	public static void validateAnimal(Animal animal, Boolean animalEstaEmLactacao) {
		if ( !animalEstaEmLactacao ){
			throw new ValidationException(REGRA_NEGOCIO, "O animal " + animal.getNumeroNome() + " n�o estava em lacta��o na data selecionada. "
					+ "Por favor, selecione outro animal, ou verifique se a data de lan�amento est� correta.");
		}
		
	}
}
