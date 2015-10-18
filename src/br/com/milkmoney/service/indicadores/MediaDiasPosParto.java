package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.model.Animal;

/**
 * Calcula a média de dias pós parto.
 * @author ruminiki
 */


@Service
public class MediaDiasPosParto extends AbstractCalculadorIndicador{

	@Autowired private AnimalDao animalDao;
	
	@Override
	public BigDecimal getValue() {

		BigInteger dias = BigInteger.ZERO;
		//vacas em lactação
		List<Animal> femeas = animalDao.findAllFemeasEmLactacao();
		
		for ( Animal femea : femeas ){
			dias = dias.add(animalDao.countDiasLactacao(femea));
		}
		
		if ( dias.compareTo(BigInteger.ZERO) > 0 ){
			return BigDecimal.valueOf(dias.divide(BigInteger.valueOf(femeas.size())).longValue());
		}
		
		return BigDecimal.ZERO;
		
	}
	
	@Override
	public String getFormat() {
		return AbstractCalculadorIndicador.INTEIRO_FORMAT;
	}
	
	
}
