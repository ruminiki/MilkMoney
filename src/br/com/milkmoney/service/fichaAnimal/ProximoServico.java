package br.com.milkmoney.service.fichaAnimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.service.CoberturaService;

@Service
public class ProximoServico extends AbstractFichaAnimal {

	@Autowired CoberturaService coberturaService;
	
	@Override
	public void load(Object ... params) {
		
		FichaAnimal ficha = (FichaAnimal) params[0];
		Animal animal     = (Animal) params[1];
		ficha.setProximoServico(coberturaService.getProximoServico(animal));
		
	}

}
