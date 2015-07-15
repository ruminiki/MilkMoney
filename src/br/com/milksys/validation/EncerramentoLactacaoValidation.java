package br.com.milksys.validation;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Animal;
import br.com.milksys.model.EncerramentoLactacao;
import br.com.milksys.model.MotivoEncerramentoLactacao;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.SituacaoAnimal;
import br.com.milksys.util.DateUtil;

public class EncerramentoLactacaoValidation extends Validator {
	
	public static void validate(EncerramentoLactacao encerramentoLactacao) {
	
		Validator.validate(encerramentoLactacao);
		
		validaSituacaoAnimal(encerramentoLactacao.getAnimal());
		
		if ( encerramentoLactacao.getData().after(new Date()) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de encerramento da lacta��o n�o pode ser maior que a data atual.");
		}
		
		if ( encerramentoLactacao.getDataPrevisaoParto() != null 
				&& encerramentoLactacao.getData().after(encerramentoLactacao.getDataPrevisaoParto()) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de encerramento deve ser menor que a data prevista para o parto.");
		}
		
		if ( !encerramentoLactacao.getAnimal().getSexo().equals(Sexo.FEMEA) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "Somente animais f�meas podem ter a lacta��o encerrada. "
					+ "Por favor, selecione outro animal e tente novamente.");
		}
		
		if ( encerramentoLactacao.getAnimal().getDataUltimaCobertura() != null ){
			long mesesAposCobertura = ChronoUnit.MONTHS.between(DateUtil.asLocalDate(encerramentoLactacao.getAnimal().getDataUltimaCobertura()), DateUtil.asLocalDate(encerramentoLactacao.getData()));
			if ( mesesAposCobertura > 9 ){
				throw new ValidationException(REGRA_NEGOCIO, "A data do encerramento da lacta��o � incompat�vel com a data da �ltima cobertura. Por favor, verifique a data e tente novamente.");
			}
		}
		
	}
	
	public static void validaFemeaCoberta(EncerramentoLactacao encerramentoLactacao, Function<Animal, Boolean> isFemeaCoberta){
		//verificar se femea possui cobertura cadastrada
		if ( encerramentoLactacao.getMotivoEncerramentoLactacao().equals(MotivoEncerramentoLactacao.PREPARACAO_PARTO) ){
			if ( isFemeaCoberta != null && !isFemeaCoberta.apply(encerramentoLactacao.getAnimal()) ){
				throw new ValidationException(REGRA_NEGOCIO, "Quando o motivo do encerramento for, previs�o para o parto � necess�rio "
						+ "que a f�mea selecionada possua cobertura cadastrada em aberto (INDEFINIDA ou PRENHA). "
						+ "Por favor, cadastre a cobertura para ent�o ser poss�vel fazer o encerramento da lacta��o.");
			}	
		}
		
	}

	public static void validaSituacaoAnimal(Animal animal){
		
		if ( animal.getSituacaoAnimal() != null && !animal.getSituacaoAnimal().equals(SituacaoAnimal.EM_LACTACAO) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "Para secar a vaca � necess�rio que ela esteja em lacta��o.");
		}
		
	}
	
}
