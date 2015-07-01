package br.com.milksys.validation;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.PrecoLeite;

public class PrecoLeiteValidation extends Validator {
	
	public static void validate(PrecoLeite precoLeite) {
	
		Validator.validate(precoLeite);
		
		//===============PREÇO RECEBIDO MAIOR QUE VALOR MÁXIMO========================
		if ( precoLeite.getValorRecebido().compareTo(precoLeite.getValorMaximoPraticado()) > 0 ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"O preço recebido não pode ser maior que o valor máximo praticado pelo laticínio.");
		}
		
	}
}
