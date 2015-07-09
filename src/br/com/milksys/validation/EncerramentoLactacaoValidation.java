package br.com.milksys.validation;

import java.util.Date;
import java.util.function.Function;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Animal;
import br.com.milksys.model.EncerramentoLactacao;
import br.com.milksys.model.MotivoEncerramentoLactacao;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.SituacaoAnimal;

public class EncerramentoLactacaoValidation extends Validator {
	
	public static void validate(EncerramentoLactacao encerramentoLactacao) {
	
		Validator.validate(encerramentoLactacao);
		
		validaSituacaoAnimal(encerramentoLactacao.getAnimal());
		
		if ( encerramentoLactacao.getData().after(new Date()) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de encerramento da lactação não pode ser maior que a data atual.");
		}
		
		if ( encerramentoLactacao.getDataPrevisaoParto() != null 
				&& encerramentoLactacao.getData().after(encerramentoLactacao.getDataPrevisaoParto()) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de encerramento deve ser menor que a data prevista para o parto.");
		}
		
		if ( !encerramentoLactacao.getAnimal().getSexo().equals(Sexo.FEMEA) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "Somente animais fêmeas podem ter a lactação encerrada. "
					+ "Por favor, selecione outro animal e tente novamente.");
		}
		
	}
	
	public static void validaFemeaCoberta(EncerramentoLactacao encerramentoLactacao, Function<Animal, Boolean> isFemeaCoberta){
		//verificar se femea possui cobertura cadastrada
		if ( encerramentoLactacao.getMotivoEncerramentoLactacao().equals(MotivoEncerramentoLactacao.PREPARACAO_PARTO) ){
			if ( isFemeaCoberta != null && !isFemeaCoberta.apply(encerramentoLactacao.getAnimal()) ){
				throw new ValidationException(REGRA_NEGOCIO, "A fêmea selecionada não possui cobertura em aberto (situação INDEFINIDA ou PRENHA). "
						+ "Por favor, cadastre a cobertura para então, ser possível fazer o encerramento da lactação.");
			}	
		}
		
	}

	public static void validaSituacaoAnimal(Animal animal){
		
		if ( animal.getSituacaoAnimal() != null ){
			if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.MORTO) ){
				throw new ValidationException(VALIDACAO_FORMULARIO, "O animal selecionado teve a morte registrada. Por favor, selecione outro animal para continuar.");
			}
			if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.VENDIDO) ){
				throw new ValidationException(VALIDACAO_FORMULARIO, "O animal selecionado teve a venda registrada. Por favor, selecione outro animal para continuar.");
			}
		}
		
	}
	
}
