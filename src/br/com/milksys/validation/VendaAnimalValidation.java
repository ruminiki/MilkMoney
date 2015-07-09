package br.com.milksys.validation;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.AnimalVendido;
import br.com.milksys.model.SituacaoAnimal;
import br.com.milksys.model.VendaAnimal;

public class VendaAnimalValidation extends Validator {
	
	public static void validate(VendaAnimal vendaAnimal) {
	
		Validator.validate(vendaAnimal);
		
		if ( vendaAnimal.getDataPrevisaoRecebimento().before(vendaAnimal.getDataVenda()) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de recebimento prevista não pode ser menor que a data da venda.");
		}
		
		if ( vendaAnimal.getAnimaisVendidos() == null || 
				vendaAnimal.getAnimaisVendidos().size() <= 0 ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "É necessário ter pelo menos um animal na venda. "
					+ "Por favor, adicione os animais vendidos e tente novamente.");
		}
		
	}
	
	public static void validateAddAnimal(VendaAnimal vendaAnimal, AnimalVendido animalAdicionado) {

		if ( animalAdicionado == null ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "O animal selecionado é inválido. Por favor, selecione o animal novamente.");
		}
		
		if ( animalAdicionado.getAnimal().getSituacaoAnimal() != null ){
			if ( animalAdicionado.getAnimal().getSituacaoAnimal().equals(SituacaoAnimal.MORTO) ){
				throw new ValidationException(VALIDACAO_FORMULARIO, "O animal selecionado já teve a morte registrada. Por favor, selecione outro animal para continuar.");
			}
			if ( animalAdicionado.getAnimal().getSituacaoAnimal().equals(SituacaoAnimal.VENDIDO) ){
				throw new ValidationException(VALIDACAO_FORMULARIO, "O animal selecionado já teve a venda registrada. Por favor, selecione outro animal para continuar.");
			}
		}
		
		for (AnimalVendido a : vendaAnimal.getAnimaisVendidos()){
			
			if ( a.getAnimal().getId() == animalAdicionado.getAnimal().getId() ){
				throw new ValidationException(VALIDACAO_FORMULARIO, "O animal selecionado já está adicionado na venda. Por favor, selecione outro animal para continuar.");
			}
			
		}
		
	}

}
