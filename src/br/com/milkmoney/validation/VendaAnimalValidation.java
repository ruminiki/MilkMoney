package br.com.milkmoney.validation;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.AnimalVendido;
import br.com.milkmoney.model.SituacaoAnimal;
import br.com.milkmoney.model.VendaAnimal;

public class VendaAnimalValidation extends Validator {
	
	public static void validate(VendaAnimal vendaAnimal) {
	
		Validator.validate(vendaAnimal);
		
		if ( vendaAnimal.getDataPrevisaoRecebimento().before(vendaAnimal.getDataVenda()) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de recebimento prevista n�o pode ser menor que a data da venda.");
		}
		
		if ( vendaAnimal.getAnimaisVendidos() == null || 
				vendaAnimal.getAnimaisVendidos().size() <= 0 ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "� necess�rio ter pelo menos um animal na venda. "
					+ "Por favor, adicione os animais vendidos e tente novamente.");
		}
		
	}
	
	
	
	public static void validateAddAnimal(VendaAnimal vendaAnimal, AnimalVendido animalAdicionado) {

		if ( animalAdicionado == null ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "O animal selecionado � inv�lido. Por favor, selecione o animal novamente.");
		}
		
		validaSituacaoAnimal(animalAdicionado.getAnimal());
		
		for (AnimalVendido a : vendaAnimal.getAnimaisVendidos()){
			
			if ( a.getAnimal().getId() == animalAdicionado.getAnimal().getId() ){
				throw new ValidationException(VALIDACAO_FORMULARIO, "O animal selecionado j� est� adicionado na venda. Por favor, selecione outro animal para continuar.");
			}
			
		}
		
	}
	
	public static void validaSituacaoAnimal(Animal animal){
		if ( animal.getSituacaoAnimal() != null ){
			if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.MORTO) ){
				throw new ValidationException(VALIDACAO_FORMULARIO, "O animal selecionado j� teve a morte registrada. Por favor, selecione outro animal para continuar.");
			}
			if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.VENDIDO) ){
				throw new ValidationException(VALIDACAO_FORMULARIO, "O animal selecionado j� teve a venda registrada. Por favor, selecione outro animal para continuar.");
			}
		}
	}
	

}