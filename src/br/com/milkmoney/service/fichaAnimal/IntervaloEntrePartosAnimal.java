package br.com.milkmoney.service.fichaAnimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.service.PartoService;
import br.com.milkmoney.util.DateUtil;

@Service
public class IntervaloEntrePartosAnimal extends AbstractFichaAnimal {

	@Autowired PartoService partoService;
	
	@Override
	public void load(Object ... params) {
		
		FichaAnimal ficha = (FichaAnimal) params[0];
		Animal animal     = (Animal) params[1];
		ficha.setIntervaloEntrePartos(partoService.getIntervaloEntrePartos(animal, DateUtil.today));
		
	}

}
