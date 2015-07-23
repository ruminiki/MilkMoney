package br.com.milkmoney.validation;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.EntregaLeite;

public class EntregaLeiteValidation extends Validator {
	
	public static void validate(EntregaLeite entregaLeite) {
	
		Validator.validate(entregaLeite);
		
		//===============DATA FIM MENOR DATA INICIO========================
		if ( entregaLeite.getDataFim() != null && entregaLeite.getDataInicio() != null ){
			if ( entregaLeite.getDataFim().before(entregaLeite.getDataInicio()) ){
				throw new ValidationException(CAMPO_OBRIGATORIO, 
						"A data final do período de produção para entrega não pode ser menor que a data inicial.");
			}
		}
		
	}
}
