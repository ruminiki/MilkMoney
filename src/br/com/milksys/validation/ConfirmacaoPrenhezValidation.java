package br.com.milksys.validation;

import java.time.temporal.ChronoUnit;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.ConfirmacaoPrenhez;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.util.DateUtil;

public class ConfirmacaoPrenhezValidation extends Validator{

	public static void validate(ConfirmacaoPrenhez confirmacaoPrenhez){
		Validator.validate(confirmacaoPrenhez);
		
		if ( confirmacaoPrenhez.getCobertura().getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
			throw new ValidationException(REGRA_NEGOCIO, 
					"A cobertura selecionada j� teve parto cadastrado n�o sendo poss�vel executar essa opera��o.");
		}
		
		if ( confirmacaoPrenhez.getData().before(confirmacaoPrenhez.getCobertura().getData()) ||
				DateUtil.isSameDate(confirmacaoPrenhez.getData(), confirmacaoPrenhez.getCobertura().getData()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da confirma��o n�o pode ser menor ou igual a data da cobertura.");
		}
		
		long mesesCobertura = ChronoUnit.MONTHS.between(DateUtil.asLocalDate(confirmacaoPrenhez.getCobertura().getData()), DateUtil.asLocalDate(confirmacaoPrenhez.getData()));
		
		if ( mesesCobertura > 4 ){
			throw new ValidationException(REGRA_NEGOCIO, "A data do diagn�stico � incompat�vel com a data da cobertura. Os diagn�sticos devem ser realizados "
					+ "at� 120 dias ap�s a cobertura. Por favor, corrija a data e tente novamente.");
		}
		
	}
	
}
