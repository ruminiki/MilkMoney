package br.com.milkmoney.service.fichaAnimal;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.service.LactacaoService;
import br.com.milkmoney.service.ProducaoIndividualService;

@Service
public class MediaProducaoUltimaLactacao extends AbstractFichaAnimal {

	@Autowired LactacaoService lactacaoService;
	@Autowired ProducaoIndividualService producaoIndividualService;
	
	@Override
	public void load(Object ... params) {
		
		FichaAnimal ficha = (FichaAnimal) params[0];
		Animal animal     = (Animal) params[1];
		
		Lactacao lactacao = lactacaoService.findUltimaLactacaoAnimal(animal);
		//media de produção da última lactação
		if ( lactacao != null ){
			ficha.setMediaProducao(BigDecimal.valueOf(producaoIndividualService.getMediaAnimalPeriodo(animal, lactacao.getDataInicio(), lactacao.getDataFim())));
			ficha.setMediaProducao(ficha.getMediaProducao().setScale(2, RoundingMode.HALF_EVEN));
		}

	}

}
