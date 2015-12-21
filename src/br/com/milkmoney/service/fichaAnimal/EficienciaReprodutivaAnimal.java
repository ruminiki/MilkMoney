package br.com.milkmoney.service.fichaAnimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;

@Service
public class EficienciaReprodutivaAnimal extends AbstractFichaAnimal {

	@Autowired br.com.milkmoney.service.indicadores.EficienciaReprodutiva eficienciaReprodutiva;
	
	@Override
	public void load(Object ... params) {
		
		FichaAnimal ficha = (FichaAnimal) params[0];
		Animal animal     = (Animal) params[1];
		
		ficha.setEficienciaReprodutiva(eficienciaReprodutiva.getValue(animal));
		
	}

}
