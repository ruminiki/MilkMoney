package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;

/**
 * Calcula o percentual de vacas no rebanho:
 * Total de vacas dividido pelo Total de animais ativos (não mortos, não vendidos)
 * @author ruminiki
 */


@Service
public class PercentualVacasRebanho extends AbstractCalculadorIndicador{

	@Autowired AnimalDao animalDao;
	
	@Override
	public BigDecimal getValue(Date data) {

		BigDecimal rebanho = BigDecimal.valueOf(animalDao.countAllAtivos(data).longValue());
		BigDecimal vacas   = BigDecimal.valueOf(animalDao.countAllVacasAtivas(data).longValue());
		BigDecimal result  = BigDecimal.ZERO;
		
		if ( rebanho.compareTo(BigDecimal.ZERO) > 0 && vacas.compareTo(BigDecimal.ZERO) > 0 ){
			result = vacas.divide(rebanho,2,RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100));
		}
		
		return result;
		
	}
	
}
