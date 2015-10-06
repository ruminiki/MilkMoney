package br.com.milkmoney.validation;

import java.util.Date;
import java.util.function.Function;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.model.MotivoEncerramentoLactacao;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.util.DateUtil;

public class LactacaoValidation extends Validator {
	
	public static void validate(Lactacao lactacao) {
	
		Validator.validate(lactacao);
		
		if ( lactacao.getDataInicio().after(new Date()) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de in�cio da lacta��o n�o pode ser maior que a data atual.");
		}
		
		if ( lactacao.getMotivoEncerramentoLactacao() != null ){
			if ( lactacao.getDataFim() == null ){
				throw new ValidationException(CAMPO_OBRIGATORIO, "� necess�rio informar a data de encerramento da lacta��o. Por favor, tente novamente.");
			}
		}
		
		if ( lactacao.getDataInicio().after(lactacao.getDataFim()) || 
				DateUtil.isSameDate(lactacao.getDataInicio(), lactacao.getDataFim()) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de encerramento deve ser maior que a data de in�cio da lacta��o.");
		}
		
		if ( lactacao.getDataFim().after(new Date()) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de encerramento n�o pode ser maior que a data atual.");
		}
		
		if ( !lactacao.getAnimal().getSexo().equals(Sexo.FEMEA) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "Somente animais f�meas podem ter lacta��es cadastradas. "
					+ "Por favor, selecione outro animal e tente novamente.");
		}
		
	}
	
	public static void validaFemeaCoberta(Lactacao lactacao, Function<Animal, Boolean> isFemeaCoberta){
		//verificar se femea possui cobertura cadastrada
		if ( lactacao.getMotivoEncerramentoLactacao() != null &&
				lactacao.getMotivoEncerramentoLactacao().equals(MotivoEncerramentoLactacao.PREPARACAO_PARTO) ){
			if ( isFemeaCoberta != null && !isFemeaCoberta.apply(lactacao.getAnimal()) ){
				throw new ValidationException(REGRA_NEGOCIO, "Quando o motivo do encerramento for previs�o para o parto � necess�rio "
						+ "que a f�mea selecionada possua cobertura cadastrada em aberto (INDEFINIDA ou PRENHA). "
						+ "Por favor, cadastre a cobertura para ent�o ser poss�vel fazer o encerramento da lacta��o.");
			}else{
				/*if ( lactacao.getAnimal().getDataUltimaCobertura() != null ){
					long diasCobertura = ChronoUnit.DAYS.between(DateUtil.asLocalDate(lactacao.getAnimal().getDataUltimaCobertura()), DateUtil.asLocalDate(lactacao.getDataFim()));
					if ( diasCobertura < 210 ){//a vaca � seca com 210 dias ap�s a cobertura
						throw new ValidationException(REGRA_NEGOCIO, "A data do encerramento da lacta��o � incompat�vel com a data da �ltima cobertura ("
												+ DateUtil.format(lactacao.getAnimal().getDataUltimaCobertura()) + "). Por favor, verifique a data e tente novamente.");
					}
				}*/
			}
		}
		
	}

}
