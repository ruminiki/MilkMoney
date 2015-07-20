package br.com.milksys.validation;

import java.util.Date;
import java.util.function.Function;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Lactacao;
import br.com.milksys.model.MotivoEncerramentoLactacao;
import br.com.milksys.model.Sexo;
import br.com.milksys.util.DateUtil;

public class LactacaoValidation extends Validator {
	
	public static void validate(Lactacao lactacao) {
	
		Validator.validate(lactacao);
		
		if ( lactacao.getDataInicio().after(new Date()) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de início da lactação não pode ser maior que a data atual.");
		}
		
		if ( lactacao.getDataInicio().after(lactacao.getDataFim()) || 
				DateUtil.isSameDate(lactacao.getDataInicio(), lactacao.getDataFim()) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de encerramento deve ser maior que a data de início da lactação.");
		}
		
		if ( !lactacao.getAnimal().getSexo().equals(Sexo.FEMEA) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "Somente animais fêmeas podem ter lactações cadastradas. "
					+ "Por favor, selecione outro animal e tente novamente.");
		}
		
	}
	
	public static void validaFemeaCoberta(Lactacao lactacao, Function<Animal, Boolean> isFemeaCoberta){
		//verificar se femea possui cobertura cadastrada
		if ( lactacao.getMotivoEncerramentoLactacao() != null &&
				lactacao.getMotivoEncerramentoLactacao().equals(MotivoEncerramentoLactacao.PREPARACAO_PARTO) ){
			if ( isFemeaCoberta != null && !isFemeaCoberta.apply(lactacao.getAnimal()) ){
				throw new ValidationException(REGRA_NEGOCIO, "Quando o motivo do encerramento for, previsão para o parto é necessário "
						+ "que a fêmea selecionada possua cobertura cadastrada em aberto (INDEFINIDA ou PRENHA). "
						+ "Por favor, cadastre a cobertura para então ser possível fazer o encerramento da lactação.");
			}else{
				/*if ( lactacao.getAnimal().getDataUltimaCobertura() != null ){
					long diasCobertura = ChronoUnit.DAYS.between(DateUtil.asLocalDate(lactacao.getAnimal().getDataUltimaCobertura()), DateUtil.asLocalDate(lactacao.getDataFim()));
					if ( diasCobertura < 210 ){//a vaca é seca com 210 dias após a cobertura
						throw new ValidationException(REGRA_NEGOCIO, "A data do encerramento da lactação é incompatível com a data da última cobertura ("
												+ DateUtil.format(lactacao.getAnimal().getDataUltimaCobertura()) + "). Por favor, verifique a data e tente novamente.");
					}
				}*/
			}
		}
		
	}

}
