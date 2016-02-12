package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;

/**
 * Calcula o total de fêmeas ativas no rebanho.

 * @author ruminiki
 */


@Service
public class TotalFemeas extends AbstractCalculadorIndicador{

	@Autowired private AnimalDao animalDao;
	
	@Override
	public BigDecimal getValue(Date data) {

		BigInteger tamanho = animalDao.countAllFemeasAtivas(data);
		return (tamanho == null ? BigDecimal.ZERO : BigDecimal.valueOf(tamanho.intValue()));
		
	}
	
}
