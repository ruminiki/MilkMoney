package br.com.milkmoney.validation;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Cria;
import br.com.milkmoney.model.SimNao;
import br.com.milkmoney.model.SituacaoNascimento;

public class CriaValidation extends Validator {
	
	public static void validate(Cria cria) {
	
		Validator.validate(cria);
		
		if ( cria != null ){
			
			if ( cria.getIncorporadoAoRebanho().equals(SimNao.SIM) &&
					cria.getSituacaoNascimento().equals(SituacaoNascimento.NASCIDO_MORTO) ){
				throw new ValidationException(REGRA_NEGOCIO, 
						"O animal nascido morto n�o pode ser incorporado ao rebanho. Por favor, corrija e tente novamente.");
			}
			
			if ( cria.getIncorporadoAoRebanho().equals(SimNao.SIM) &&
					cria.getAnimal() == null ){
				throw new ValidationException(REGRA_NEGOCIO, 
						"Se o animal ser� incorporado ao rebanho, � necess�rio finalizar o seu cadastro para continuar.");
			}

			
		}else{
			throw new ValidationException(VALIDACAO_FORMULARIO, 
					"O objeto est� vazio. Por favor reinicie o processo.");
		}
		
	}
}
