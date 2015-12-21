package br.com.milkmoney.service.fichaAnimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.model.Procedimento;
import br.com.milkmoney.service.ProcedimentoService;

@Service
public class UltimoProcedimento extends AbstractFichaAnimal {

	@Autowired ProcedimentoService procedimentoService;
	
	@Override
	public void load(Object ... params) {
		
		FichaAnimal ficha = (FichaAnimal) params[0];
		Animal animal     = (Animal) params[1];
		
		Procedimento procedimento = procedimentoService.getUltimoTratamento(animal);
		ficha.setUltimoTratamento(procedimento != null ? procedimento.getDescricao() : "--");
		
	}

}
