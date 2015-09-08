package br.com.milkmoney.validation;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Indicador;


public class IndicadorValidation extends Validator {
	
	public static void validate(Indicador indicador) {
	
		Validator.validate(indicador);
		
		if ( indicador.getMaiorValorIdeal().compareTo(indicador.getMenorValorIdeal()) < 0 ){
			throw new ValidationException(REGRA_NEGOCIO, "O segundo valor do intervalo ideal deve ser igual ou maior ao primeiro valor. Por favor, corrija e tente novamente.");
		}
		
	}
}
