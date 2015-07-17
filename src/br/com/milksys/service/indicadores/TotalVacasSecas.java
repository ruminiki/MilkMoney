package br.com.milksys.service.indicadores;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.AnimalDao;

/**
 * Calcula o total de f�meas ativas no rebanho.

 * @author ruminiki
 */


@Service
public class TotalVacasSecas extends AbstractCalculadorIndicador{

	@Autowired AnimalDao animalDao;
	
	@Override
	public String getValue() {

		BigInteger tamanho = animalDao.countAllFemeasSecas();
		return (tamanho == null ? String.valueOf(BigDecimal.ZERO) : String.valueOf(tamanho));
		
	}
	
}
