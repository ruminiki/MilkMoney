package br.com.milkmoney.validation;

import java.time.temporal.ChronoUnit;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.ConfirmacaoPrenhes;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.util.DateUtil;

public class ConfirmacaoPrenhesValidation extends Validator{

	public static void validate(ConfirmacaoPrenhes confirmacaoPrenhes){
		Validator.validate(confirmacaoPrenhes);
		
		if ( confirmacaoPrenhes.getCobertura().getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
			throw new ValidationException(REGRA_NEGOCIO, 
					"A cobertura selecionada j� teve parto cadastrado n�o sendo poss�vel executar essa opera��o.");
		}
		
		if ( confirmacaoPrenhes.getData().before(confirmacaoPrenhes.getCobertura().getData()) ||
				DateUtil.isSameDate(confirmacaoPrenhes.getData(), confirmacaoPrenhes.getCobertura().getData()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da confirma��o n�o pode ser menor ou igual a data da cobertura.");
		}
		
		long mesesCobertura = ChronoUnit.MONTHS.between(DateUtil.asLocalDate(confirmacaoPrenhes.getCobertura().getData()), DateUtil.asLocalDate(confirmacaoPrenhes.getData()));
		
		if ( mesesCobertura > 4 ){
			throw new ValidationException(REGRA_NEGOCIO, "A data do diagn�stico � incompat�vel com a data da cobertura. Os diagn�sticos devem ser realizados "
					+ "at� 120 dias ap�s a cobertura. Por favor, corrija a data e tente novamente.");
		}
		
	}
	
}
