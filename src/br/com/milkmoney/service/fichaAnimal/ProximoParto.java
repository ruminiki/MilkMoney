package br.com.milkmoney.service.fichaAnimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.service.CoberturaService;

@Service
public class ProximoParto extends AbstractFichaAnimal {

	@Autowired CoberturaService coberturaService;
	
	@Override
	public void load(Object ... params) {
		
		FichaAnimal ficha = (FichaAnimal) params[0];
		Animal animal     = (Animal) params[1];
		
		Cobertura cobertura = coberturaService.findCoberturaAtivaByAnimal(animal);
		ficha.setDataProximoParto(cobertura != null ? cobertura.getPrevisaoParto() : null);
		
	}

}
