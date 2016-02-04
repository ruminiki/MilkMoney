package br.com.milkmoney.validation;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Cria;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.util.DateUtil;

public class PartoValidation extends Validator {
	
	public static void validate(Parto parto) {
	
		Validator.validate(parto);
		
		if ( parto.getData().after(new Date()) ){
			throw new ValidationException(REGRA_NEGOCIO, 
					"A data do parto não pode ser maior que a data atual.");
		}
		
		if ( parto.getData().before(parto.getCobertura().getData()) ){
			throw new ValidationException(REGRA_NEGOCIO, 
					"A data do parto não pode ser menor que a data da cobertura.");
		}
		
		if ( parto.getCobertura().getSituacaoCobertura() != null ){
			if ( !parto.getCobertura().getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) &&
					!parto.getCobertura().getSituacaoCobertura().equals(SituacaoCobertura.NAO_CONFIRMADA) ){
				throw new ValidationException(REGRA_NEGOCIO, "A cobertura selecionada tem situação igual a " + parto.getCobertura().getSituacaoCobertura() + 
						" não sendo possível o registro do parto.");
			}
		}
		
		long mesesPartoAposCobertura = ChronoUnit.MONTHS.between(DateUtil.asLocalDate(parto.getCobertura().getData()), DateUtil.asLocalDate(parto.getData()));
		if ( mesesPartoAposCobertura > 10 ){
			throw new ValidationException(REGRA_NEGOCIO, "A data informada " + DateUtil.format(parto.getData()) + " é incompatível \ncom a data da cobertura e previsão do parto. \nPor favor, atualize a data do parto e tente novamente.");
		}
		
		if ( parto.getCrias() == null || parto.getCrias().isEmpty() ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"O parto deve ter pelo menos uma cria registrada.");
		}
		
		for ( Cria a : parto.getCrias() ){
			
			if ( a.getAnimal() != null &&
					!DateUtil.isSameDate(a.getAnimal().getDataNascimento(), parto.getData()) ){
				throw new ValidationException(REGRA_NEGOCIO, 
						"A data de nascimento do animal [" + a.getAnimal().getNumeroNome() + "] deve ser igual a data do parto.");
			}
			
		}
		
	}
	
	public static void validaEncerramentoLactacao(Parto parto, Lactacao lactacao) {
		if ( lactacao != null ){
			
			if ( lactacao.getDataFim() == null ){
				throw new ValidationException(REGRA_NEGOCIO, "O animal não teve a última lactação encerrada. \nPor favor, registre o encerramento "
						+ "para então registrar o novo parto e \niniciar uma nova lactação.");	
			}
			
			if ( parto.getData().compareTo(lactacao.getDataFim()) <= 0 ){
				throw new ValidationException(REGRA_NEGOCIO, "O animal estava em lactação no dia " + DateUtil.format(parto.getData()) + ". \nPor favor, corrija a data do parto ou a data de encerramento da lactação.");	
			}
			
		}
	}
	
	
}
