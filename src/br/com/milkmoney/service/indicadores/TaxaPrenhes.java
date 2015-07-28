package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.service.CoberturaService;

/**
 * O intervalo da primeira IA até a concepção representa a taxa de prenhes, ou seja, 
 * a velocidade em que a vaca fica gestante. Esse intervalo varia muito entre vacas do mesmo rebanho.
 * Dois fatores determinam a taxa de prenhes:
 * 1 - Taxa de concepção;
 * 2 - Taxa de detecção de cio ou taxa de serviço. 
 * http://www.milkpoint.com.br/radar-tecnico/reproducao/estrategias-de-manejo-para-aumentar-a-eficiencia-reprodutiva-de-vacas-de-leite-28283n.aspx
 * 
 * @author ruminiki
 */


@Service
public class TaxaPrenhes extends AbstractCalculadorIndicador{

	@Autowired AnimalDao animalDao;
	@Autowired CoberturaService coberturaService;
	
	@Override
	public String getValue() {

		List<Animal> animais = animalDao.findAnimaisComCobertura(); 
		BigDecimal somaMediaIntervaloPrimeiraCoberturaAtePrenhes = BigDecimal.ZERO;
		
		for ( Animal animal : animais ){
			somaMediaIntervaloPrimeiraCoberturaAtePrenhes = somaMediaIntervaloPrimeiraCoberturaAtePrenhes.add(coberturaService.getMediaIntervaloPrimeiraCoberturaAtePrenhes(animal));
		}
		
		if ( animais != null && animais.size() > 0 && somaMediaIntervaloPrimeiraCoberturaAtePrenhes.compareTo(BigDecimal.ZERO) > 0 ){
			return String.valueOf(somaMediaIntervaloPrimeiraCoberturaAtePrenhes.divide(BigDecimal.valueOf(animais.size()), 2, RoundingMode.HALF_EVEN));
		}
		
		return String.valueOf(BigDecimal.ZERO);
		
	}
	
}
