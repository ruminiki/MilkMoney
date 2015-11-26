package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.service.AnimalService;

/**
 * Calcula o total de fêmeas ativas no rebanho.

 * @author ruminiki
 */


@Service
public class TotalNovilhas extends AbstractCalculadorIndicador{

	@Autowired private AnimalService animalService;
	
	@Override
	public BigDecimal getValue() {

		BigInteger tamanho = animalService.countAllNovilhas();
		return (tamanho == null ? BigDecimal.ZERO : BigDecimal.valueOf(tamanho.intValue()));
		
	}
	
}
