package br.com.milkmoney.service.fichaAnimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.service.AnimalService;

@Service
public class IdadePrimeiroParto extends AbstractFichaAnimal {

	@Autowired AnimalService animalService;
	
	@Override
	public void load(Object ... params) {
		
		FichaAnimal ficha = (FichaAnimal) params[0];
		Animal animal     = (Animal) params[1];
		ficha.setIdadePrimeiroParto(animalService.getIdadePrimeiroParto(animal));
		
	}

}
