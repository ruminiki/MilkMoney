package br.com.milkmoney.validation;

import java.math.BigDecimal;
import java.util.Date;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.ProducaoIndividual;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.util.DateUtil;

public class ProducaoIndividualValidation extends Validator {
	
	public static void validate(ProducaoIndividual producaoIndividual) {
	
		Validator.validate(producaoIndividual);
		
		if ( producaoIndividual.getPrimeiraOrdenha().compareTo(BigDecimal.ZERO) <= 0 &&
				producaoIndividual.getSegundaOrdenha().compareTo(BigDecimal.ZERO) <= 0 &&
						producaoIndividual.getTerceiraOrdenha().compareTo(BigDecimal.ZERO) <= 0){
			
			throw new ValidationException(CAMPO_OBRIGATORIO, "É necessário informar pelo menos um valor para primeira ordenha, segunda ordenha ou terceira ordenha.");
			
		}
		
		if ( !producaoIndividual.getAnimal().getSexo().equals(Sexo.FEMEA) ){
			throw new ValidationException(REGRA_NEGOCIO, "Somente pode ser registrada a produção de animais fêmeas.");
		}
		
		if ( producaoIndividual.getData().after(new Date()) ){
			throw new ValidationException(REGRA_NEGOCIO, "A data do lançamento da produção não pode ser maior que a data atual. Por favor, corrija e tente novamente.");
		}
		
		if ( producaoIndividual.getLactacao() != null ){
			
			Date dataFinal = producaoIndividual.getLactacao().getDataFim() != null ? producaoIndividual.getLactacao().getDataFim() : new Date();
			
			if ( !(producaoIndividual.getData().compareTo(producaoIndividual.getLactacao().getDataInicio()) >= 0 &&
					producaoIndividual.getData().compareTo(dataFinal) <= 0) ){
				throw new ValidationException(REGRA_NEGOCIO, "O lançamento da produção deve ocorrer dentro do período de lactação do animal. "
						+ "Verifique se o animal selecionado está em lactação no dia " + DateUtil.format(producaoIndividual.getData()) + ".");
			}
			
		}else{
			throw new ValidationException(REGRA_NEGOCIO, "Para registrar a produção do animal é necessário selecionar uma lactação. Por favor, selecione a lactação e tente novamente.");
		}
		 
	}

}
