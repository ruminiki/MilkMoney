package br.com.milksys.service.indicadores;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.AnimalDao;

/**
 * Calcula o tamanho total do rebanho indepentede de machos e fêmeas, vacas, novilhas...
 * Considera apenas animais ativos.
 * @author ruminiki
 */


@Service
public class TamanhoRebanho extends AbstractCalculadorIndicador{

	@Autowired AnimalDao animalDao;
	
	@Override
	public String getValue() {
		
		BigInteger tamanho = animalDao.countAllAtivos();
		return (tamanho == null ? String.valueOf(BigDecimal.ZERO) : String.valueOf(tamanho));
		
	}
	
}
