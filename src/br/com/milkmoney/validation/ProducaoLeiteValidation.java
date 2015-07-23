package br.com.milkmoney.validation;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.ProducaoLeite;

public class ProducaoLeiteValidation extends Validator {
	
	public static void validate(ProducaoLeite producaoLeite) {
	
		Validator.validate(producaoLeite);
		
		//===============QTDE ENTREGUE MAIOR QUE QTDE PRODUZIDA========================
		if ( producaoLeite.getVolumeEntregue().compareTo(producaoLeite.getVolumeProduzido()) > 0 ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"O volume de leite entregue não pode ser maior que o volume produzido no dia.");
		}
		
	}
}
