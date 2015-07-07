package br.com.milksys.validation;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Cria;
import br.com.milksys.model.SimNao;

public class CriaValidation extends Validator {
	
	public static void validate(Cria cria) {
	
		Validator.validate(cria);
		
		if ( cria != null ){
			
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
