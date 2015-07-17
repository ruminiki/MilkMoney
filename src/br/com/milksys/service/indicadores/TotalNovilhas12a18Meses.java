package br.com.milksys.service.indicadores;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.AnimalDao;

/**
 * Calcula o total vacas no rebanho. Para ser considerada vaca, deve
 * ter um parto cadastrado.

 * @author ruminiki
 */


@Service
public class TotalNovilhas12a18Meses extends AbstractCalculadorIndicador{

	@Autowired AnimalDao animalDao;
	
	@Override
	public String getValue() {

		BigInteger tamanho = animalDao.countAllNovilhasIdadeEntreMeses(12,18);
		return (tamanho == null ? String.valueOf(BigDecimal.ZERO) : String.valueOf(tamanho));
		
	}
	
}
