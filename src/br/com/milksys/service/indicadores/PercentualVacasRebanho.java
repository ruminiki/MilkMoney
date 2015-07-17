package br.com.milksys.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.AnimalDao;

/**
 * Calcula o percentual de vacas no rebanho:
 * Total de vacas dividido pelo Total de animais ativos (não mortos, não vendidos)
 * @author ruminiki
 */


@Service
public class PercentualVacasRebanho extends AbstractCalculadorIndicador{

	@Autowired AnimalDao animalDao;
	
	@Override
	public String getValue() {

		BigDecimal rebanho = BigDecimal.valueOf(animalDao.countAllAtivos().longValue());
		BigDecimal vacas   = BigDecimal.valueOf(animalDao.countAllVacasAtivas().longValue());
		BigDecimal result  = BigDecimal.ZERO;
		
		if ( rebanho.compareTo(BigDecimal.ZERO) > 0 && vacas.compareTo(BigDecimal.ZERO) > 0 ){
			result = vacas.divide(rebanho,2,RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100));
		}
		
		return String.valueOf(result);
		
	}
	
}
