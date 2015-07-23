package br.com.milkmoney.validation;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.PrecoLeite;

public class PrecoLeiteValidation extends Validator {
	
	public static void validate(PrecoLeite precoLeite) {
	
		Validator.validate(precoLeite);
		
		//===============PRE�O RECEBIDO MAIOR QUE VALOR M�XIMO========================
		if ( precoLeite.getValorRecebido().compareTo(precoLeite.getValorMaximoPraticado()) > 0 ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"O pre�o recebido n�o pode ser maior que o valor m�ximo praticado pelo latic�nio.");
		}
		
	}
}
