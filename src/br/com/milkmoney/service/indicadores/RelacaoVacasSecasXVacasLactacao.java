package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;

/**
 * Calcula o total vacas no rebanho. Para ser considerada vaca, deve
 * ter um parto cadastrado.

 * @author ruminiki
 */


@Service
public class RelacaoVacasSecasXVacasLactacao extends AbstractCalculadorIndicador{

	@Autowired AnimalDao animalDao;
	
	@Override
	public BigDecimal getValue() {

		BigDecimal vacasSecas    = BigDecimal.valueOf(animalDao.countAllFemeasSecas().longValue());
		BigDecimal vacasLactacao = BigDecimal.valueOf(animalDao.countAllFemeasEmLactacao().longValue());
		BigDecimal relacao       = BigDecimal.ZERO;
				
		if ( vacasSecas.compareTo(BigDecimal.ZERO) > 0 ){
			if ( vacasLactacao.compareTo(BigDecimal.ZERO) > 0 ){
				relacao = vacasSecas.divide(vacasLactacao, 2, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100));
			}else{
				relacao = vacasSecas.multiply(BigDecimal.valueOf(100));
			}
		}
		
		return relacao;
		
	}
	
}
