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
			throw new ValidationException(VALIDACAO_FORMULARIO, "A data de nascimento do animal não pode ser maior que a data atual.");
		}
		
		if ( animal.getSexo().equals(Sexo.MACHO) && animal.getDataUltimoParto() != null ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "Animais machos não podem ter registrado a data de parto.");
		}
		
		if ( animal.getPaiMontaNatural() != null && animal.getPaiEnseminacaoArtificial() != null ){
			throw new ValidationException("PAI", "Não é possível registrar o pai como monta natural e inseminação artificial ao mesmo tempo. Por favor, selecione apenas um deles.");
		}
		
		if ( animal.getPaiMontaNatural() != null && 
				(!animal.getPaiMontaNatural().getSexo().equals(Sexo.MACHO) || !animal.getPaiMontaNatural().getFinalidadeAnimal().equals(FinalidadeAnimal.REPRODUCAO)) ){
			throw new ValidationException("PAI MONTA NATURAL", "Deve ser selecionado um animal macho com finalidade reprodução para o campo pai monta natural.");
		}
		
		if ( animal.getMae() != null && animal.getMae().getId() == animal.getId() ){
			throw new ValidationException("MÃE", "O animal que está sendo cadastrado não pode ser mãe dele mesmo. Por favor selecione outro animal.");
		}
		
		if ( animal.getMae() != null && animal.getMae().getMae() != null &&
				animal.getId() == animal.getMae().getMae().getId() ){
			throw new ValidationException("MÃE", "O animal não pode ser mãe de sua própria mãe. Por favor, corrija e tente novamente.");
		}
		
		if ( animal.getPaiMontaNatural() != null && animal.getPaiMontaNatural().getId() == animal.getId() ){
			throw new ValidationException("PAI", "O animal que está sendo cadastrado não pode ser pai dele mesmo. Por favor selecione outro animal.");
		}
		
		if ( animal.getSexo().equals(Sexo.MACHO) && animal.getFinalidadeAnimal().equals(FinalidadeAnimal.PRODUCAO_LEITE) ){
			throw new ValidationException(VALIDACAO_FORMULARIO, "Animais machos não podem ter finalidade para a " + FinalidadeAnimal.PRODUCAO_LEITE + ". "
					+ "Por favor, corrija e tente novamente.");
		}
		
		//se o animal tiver nascido de um parto cadastrado, não pode alterar pai e mãe nem data de nascimento
	}
	
}
