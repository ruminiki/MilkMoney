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
					"A cobertura selecionada já teve parto cadastrado não sendo possível executar essa operação.");
		}
		
		if ( confirmacaoPrenhes.getData().before(confirmacaoPrenhes.getCobertura().getData()) ||
				DateUtil.isSameDate(confirmacaoPrenhes.getData(), confirmacaoPrenhes.getCobertura().getData()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da confirmação não pode ser menor ou igual a data da cobertura.");
		}
		
		long mesesCobertura = ChronoUnit.MONTHS.between(DateUtil.asLocalDate(confirmacaoPrenhes.getCobertura().getData()), DateUtil.asLocalDate(confirmacaoPrenhes.getData()));
		
		if ( mesesCobertura > 4 ){
			throw new ValidationException(REGRA_NEGOCIO, "A data do diagnóstico é incompatível com a data da cobertura. Os diagnósticos devem ser realizados "
					+ "até 120 dias após a cobertura. Por favor, corrija a data e tente novamente.");
		}
		
	}
	
}
