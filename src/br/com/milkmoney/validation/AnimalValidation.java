package br.com.milkmoney.validation;

import java.util.Date;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FinalidadeAnimal;
import br.com.milkmoney.model.Sexo;

public class AnimalValidation extends Validator {
	
	public static void validate(Animal animal) {
	
		Validator.validate(animal);
		
		if (animal.getDataNascimento().after(new Date())){
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de nascimento do animal n�o pode ser maior que a data atual.");
		}
		
		if ( animal.getSexo().equals(Sexo.MACHO) && animal.getDataUltimoParto() != null ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "Animais machos n�o podem ter registrado a data de parto.");
		}
		
		if ( animal.getPaiMontaNatural() != null && animal.getPaiEnseminacaoArtificial() != null ){
			throw new ValidationException("PAI", "N�o � poss�vel registrar o pai como monta natural e insemina��o artificial ao mesmo tempo. Por favor, selecione apenas um deles.");
		}
		
		if ( animal.getPaiMontaNatural() != null && 
				(!animal.getPaiMontaNatural().getSexo().equals(Sexo.MACHO) || !animal.getPaiMontaNatural().getFinalidadeAnimal().equals(FinalidadeAnimal.REPRODUCAO)) ){
			throw new ValidationException("PAI MONTA NATURAL", "Deve ser selecionado um animal macho com finalidade reprodu��o para o campo pai monta natural.");
		}
		
		if ( animal.getMae() != null && animal.getMae().getId() == animal.getId() ){
			throw new ValidationException("M�E", "O animal que est� sendo cadastrado n�o pode ser m�e dele mesmo. Por favor selecione outro animal.");
		}
		
		if ( animal.getMae() != null && animal.getMae().getMae() != null &&
				animal.getId() == animal.getMae().getMae().getId() ){
			throw new ValidationException("M�E", "O animal n�o pode ser m�e de sua pr�pria m�e. Por favor, corrija e tente novamente.");
		}
		
		if ( animal.getPaiMontaNatural() != null && animal.getPaiMontaNatural().getId() == animal.getId() ){
			throw new ValidationException("PAI", "O animal que est� sendo cadastrado n�o pode ser pai dele mesmo. Por favor selecione outro animal.");
		}
		
		if ( animal.getSexo().equals(Sexo.MACHO) && animal.getFinalidadeAnimal().equals(FinalidadeAnimal.PRODUCAO_LEITE) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "Animais machos n�o podem ter finalidade para a " + FinalidadeAnimal.PRODUCAO_LEITE + ". "
					+ "Por favor, corrija e tente novamente.");
		}
		
		//se o animal tiver nascido de um parto cadastrado, n�o pode alterar pai e m�e nem data de nascimento
	}
	
}
