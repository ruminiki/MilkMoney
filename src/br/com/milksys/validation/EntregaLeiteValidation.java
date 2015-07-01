package br.com.milksys.validation;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.EntregaLeite;

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
