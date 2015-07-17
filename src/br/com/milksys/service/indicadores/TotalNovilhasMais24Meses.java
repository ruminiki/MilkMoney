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
public class TotalNovilhasMais24Meses extends AbstractCalculadorIndicador{

	@Autowired AnimalDao animalDao;
	
	@Override
	public String getValue() {

		BigInteger tamanho = animalDao.countAllNovilhasIdadeAcimaXMeses(24);
		return (tamanho == null ? String.valueOf(BigDecimal.ZERO) : String.valueOf(tamanho));
		
	}
	
}
