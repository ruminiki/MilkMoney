package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;

/**
 * Calcula o tamanho total do rebanho indepentede de machos e fêmeas, vacas, novilhas...
 * Considera apenas animais ativos.
 * @author ruminiki
 */


@Service
public class TamanhoRebanho extends AbstractCalculadorIndicador{

	@Autowired AnimalDao animalDao;
	
	@Override
	public BigDecimal getValue(Date data) {
		
		BigInteger tamanho = animalDao.countAllAtivos(data);
		return (tamanho == null ? BigDecimal.ZERO : BigDecimal.valueOf(tamanho.intValue()));
		
	}
	
}
