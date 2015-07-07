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
						"Se o animal será incorporado ao rebanho, é necessário finalizar o seu cadastro para continuar.");
			}
			
		}else{
			throw new ValidationException(VALIDACAO_FORMULARIO, 
					"O objeto está vazio. Por favor reinicie o processo.");
		}
		
	}
}
