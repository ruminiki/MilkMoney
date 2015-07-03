package br.com.milksys.validation;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Semen;

public class SemenValidation extends Validator {
	
	public static void validate(Semen semen) {
	
		Validator.validate(semen);
		
		if ( semen.getQuantidade() <= 0 ){
			throw new ValidationException(CAMPO_OBRIGATORIO, "A quantidade adquirida deve ser maior que zero. Por favor, corrija o campo e tente novamente.");
		}
		
	}
	
	
	public static void validadeQuantidadeUtilizada(Semen semen, int quantidadeUtilizada){
		if ( semen.getQuantidade() < quantidadeUtilizada ){
			throw new ValidationException(CAMPO_OBRIGATORIO, "Não é possível reduzir a quantidade cadastrada pois ela ficará menor que a quantidade "
					+ "já utilizada que é de " + quantidadeUtilizada + ".");
		}
	}
	
}
