package br.com.milksys.validation;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Cria;
import br.com.milksys.model.Parto;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.model.SituacaoNascimento;
import br.com.milksys.model.TipoParto;
import br.com.milksys.util.DateUtil;

public class PartoValidation extends Validator {
	
	public static void validate(Parto parto) {
	
		Validator.validate(parto);
		
		if ( parto.getData().after(new Date()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data do parto n�o pode ser maior que a data atual.");
		}
		
		if ( parto.getCrias() == null || parto.getCrias().isEmpty() ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"O parto deve ter pelo menos uma cria registrada.");
		}
		
		if ( parto.getData().before(parto.getCobertura().getData()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data do parto n�o pode ser menor que a data da cobertura.");
		}
		
		if ( parto.getCobertura().getSituacaoCobertura() != null ){
			if ( !parto.getCobertura().getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) &&
					!parto.getCobertura().getSituacaoCobertura().equals(SituacaoCobertura.INDEFINIDA) ){
				throw new ValidationException(REGRA_NEGOCIO, "A cobertura selecionada tem situa��o igual a " + parto.getCobertura().getSituacaoCobertura() + 
						" n�o sendo poss�vel o registro do parto.");
			}
		}
		
		long mesesPartoAposCobertura = ChronoUnit.MONTHS.between(DateUtil.asLocalDate(parto.getCobertura().getData()), DateUtil.asLocalDate(parto.getData()));
		if ( mesesPartoAposCobertura > 10 ){
			throw new ValidationException(REGRA_NEGOCIO, "A data do parto � incompat�vel com a data da cobertura. Por favor, verifique a data e tente novamente.");
		}
		
		if ( parto.getTipoParto().equals(TipoParto.ABORTO) ){
			for ( Cria a : parto.getCrias() ){
				
				if ( a.getSituacaoNascimento().equals(SituacaoNascimento.NASCIDO_VIVO) ){
					throw new ValidationException(REGRA_NEGOCIO, 
							"Quando ocorre aborto, n�o podem ter animais nascidos vivos. Por favor, corrija a situa��o do nascimento "
							+ "da cria e tente novamente.");
				}
				
			}
		}
		
		for ( Cria a : parto.getCrias() ){
			
			if ( a.getAnimal() != null &&
					!DateUtil.isSameDate(a.getAnimal().getDataNascimento(), parto.getData()) ){
				throw new ValidationException(CAMPO_OBRIGATORIO, 
						"A data de nascimento do animal [" + a.getAnimal().getNumeroNome() + "] deve ser igual a data do parto.");
			}
			
		}
		
	}
}
