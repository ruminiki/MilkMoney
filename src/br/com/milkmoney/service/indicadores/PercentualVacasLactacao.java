package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;

/**
 * Calcula o total vacas no rebanho. Para ser considerada vaca, deve
 * ter um parto cadastrado.

 * @author ruminiki
 */


@Service
public class PercentualVacasLactacao extends AbstractCalculadorIndicador{

	@Autowired AnimalDao animalDao;
	
	@Override
	public BigDecimal getValue(Date data) {

		BigDecimal rebanho       = BigDecimal.valueOf(animalDao.countAllVacasAtivas(data).longValue());
		BigDecimal vacasLactacao = BigDecimal.valueOf(animalDao.countAllFemeasEmLactacao(data).longValue());
		BigDecimal relacao       = BigDecimal.ZERO;
				
		if ( rebanho.compareTo(BigDecimal.ZERO) > 0 ){
			if ( vacasLactacao.compareTo(BigDecimal.ZERO) > 0 ){
				relacao = rebanho.divide(vacasLactacao, 2, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100));
			}else{
				relacao = rebanho.multiply(BigDecimal.valueOf(100));
			}
		}
		
		return relacao;
		
	}
	
}
