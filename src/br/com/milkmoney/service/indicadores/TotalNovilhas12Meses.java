package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;

/**
 * Calcula o total vacas no rebanho. Para ser considerada vaca, deve
 * ter um parto cadastrado.

 * @author ruminiki
 */


@Service
public class TotalNovilhas12Meses extends AbstractCalculadorIndicador{

	@Autowired private AnimalDao animalDao;
	
	@Override
	public BigDecimal getValue() {

		BigInteger tamanho = animalDao.countAllNovilhasIdadeAteXMeses(12);
		return (tamanho == null ? BigDecimal.ZERO : BigDecimal.valueOf(tamanho.intValue()));
		
	}
	
	@Override
	public String getFormat() {
		return AbstractCalculadorIndicador.INTEIRO_FORMAT;
	}
	
	
}
