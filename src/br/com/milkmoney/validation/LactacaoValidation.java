package br.com.milkmoney.validation;

import java.util.Date;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.util.DateUtil;

public class LactacaoValidation extends Validator {
	
	public static void validate(Lactacao lactacao) {
	
		Validator.validate(lactacao);
		
		if ( lactacao.getDataInicio().after(new Date()) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de início da lactação não pode ser maior que a data atual.");
		}
		
		if ( lactacao.getMotivoEncerramentoLactacao() != null ){
			if ( lactacao.getDataFim() == null ){
				throw new ValidationException(CAMPO_OBRIGATORIO, "É necessário informar a data de encerramento da lactação. Por favor, tente novamente.");
			}
		}
		
		if ( lactacao.getDataInicio().after(lactacao.getDataFim()) || 
				DateUtil.isSameDate(lactacao.getDataInicio(), lactacao.getDataFim()) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de encerramento deve ser maior que a data de início da lactação.");
		}
		
		if ( lactacao.getDataFim().after(new Date()) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de encerramento não pode ser maior que a data atual.");
		}
		
		if ( lactacao.getDataFim() != null ){
			if ( lactacao.getMotivoEncerramentoLactacao() == null ){
				throw new ValidationException(VALIDACAO_FORMULARIO, "É necessário informar o motivo do encerramento da lactação. Por favor, tente novamente.");				
			}
		}
		
		if ( !lactacao.getAnimal().getSexo().equals(Sexo.FEMEA) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "Somente animais fêmeas podem ter lactações cadastradas. "
					+ "Por favor, selecione outro animal e tente novamente.");
		}
		
	}

}
