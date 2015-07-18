package br.com.milksys.validation;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.ConfirmacaoPrenhez;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.util.DateUtil;

public class ConfirmacaoPrenhezValidation extends Validator{

	public static void validate(ConfirmacaoPrenhez confirmacaoPrenhez){
		Validator.validate(confirmacaoPrenhez);
		
		if ( confirmacaoPrenhez.getCobertura().getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
			throw new ValidationException(REGRA_NEGOCIO, 
					"A cobertura selecionada já teve parto cadastrado não sendo possível executar essa operação.");
		}
		
		if ( confirmacaoPrenhez.getData().before(confirmacaoPrenhez.getCobertura().getData()) ||
				DateUtil.isSameDate(confirmacaoPrenhez.getData(), confirmacaoPrenhez.getCobertura().getData()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da confirmação não pode ser menor ou igual a data da cobertura.");
		}
		
	}
	
}
