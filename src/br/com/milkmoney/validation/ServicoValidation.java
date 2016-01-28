package br.com.milkmoney.validation;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Servico;
import br.com.milkmoney.util.DateUtil;


public class ServicoValidation extends Validator {
	
	public static void validate(Servico servico) {
	
		Validator.validate(servico);
		
		if ( DateUtil.after(servico.getData(), servico.getDataVencimento()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, "A data de vencimento n�o pode ser menor que a data de realiza��o do servi�o. Por favor, informe uma nova data de vencimento.");
		}
		
		
	}
}
