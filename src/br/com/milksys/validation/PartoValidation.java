package br.com.milksys.validation;

import java.util.Date;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Parto;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.util.DateUtil;

public class PartoValidation extends Validator {
	
	public static void validate(Parto parto) {
	
		Validator.validate(parto);
		
		if ( parto.getData().after(new Date()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data do parto não pode ser maior que a data atual.");
		}
		
		if ( parto.getCrias() == null || parto.getCrias().isEmpty() ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"O parto deve ter pelo menos uma cria registrada.");
		}
		
		if ( parto.getData().before(parto.getCobertura().getData()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data do parto não pode ser menor que a data da cobertura.");
		}
		
		if ( parto.getCobertura().getSituacaoCobertura() != null ){
			if ( !parto.getCobertura().getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) &&
					!parto.getCobertura().getSituacaoCobertura().equals(SituacaoCobertura.INDEFINIDA) ){
				throw new ValidationException(REGRA_NEGOCIO, "A cobertura selecionada tem situação igual a " + parto.getCobertura().getSituacaoCobertura() + 
						" não sendo possível o registro do parto.");
			}
		}
		
		for ( Animal a : parto.getCrias() ){
			
			if ( !DateUtil.isSameDate(a.getDataNascimento(), parto.getData()) ){
				throw new ValidationException(CAMPO_OBRIGATORIO, 
						"A data de nascimento do animal [" + a.getNumeroNome() + "] deve ser igual a data do parto.");
			}
			
		}
		
	}
}
