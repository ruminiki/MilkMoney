package br.com.milkmoney.validation;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.ConfiguracaoIndicador;


public class ConfiguracaoIndicadorValidation extends Validator {
	
	public static void validate(ConfiguracaoIndicador configuracaoIndicador) {
	
		Validator.validate(configuracaoIndicador);
		
		if ( configuracaoIndicador.getMaiorValorIdeal().compareTo(configuracaoIndicador.getMenorValorIdeal()) < 0 ){
			throw new ValidationException(REGRA_NEGOCIO, "O segundo valor do intervalo ideal deve ser igual ou maior ao primeiro valor. Por favor, corrija e tente novamente.");
		}
		
	}
}
