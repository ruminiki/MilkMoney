package br.com.milkmoney.service.fichaAnimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.service.PartoService;

@Service
public class DataUltimoParto extends AbstractFichaAnimal {

	@Autowired PartoService partoService;
	
	@Override
	public void load(Object ... params) {
		
		FichaAnimal ficha = (FichaAnimal) params[0];
		Animal animal     = (Animal) params[1];
		
		Parto parto = partoService.findLastParto(animal);
		ficha.setDataUltimoParto(parto != null ? parto.getData() : null);
		
	}

}
