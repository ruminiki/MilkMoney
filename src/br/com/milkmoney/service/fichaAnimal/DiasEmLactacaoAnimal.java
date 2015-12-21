package br.com.milkmoney.service.fichaAnimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.service.PartoService;

@Service
public class DiasEmLactacaoAnimal extends AbstractFichaAnimal {

	@Autowired PartoService partoService;
	
	@Override
	public void load(Object ... params) {
		
		FichaAnimal ficha = (FichaAnimal) params[0];
		Animal animal     = (Animal) params[1];
		ficha.setDiasEmLactacao(partoService.getDiasEmLactacao(animal));
		
	}

}
