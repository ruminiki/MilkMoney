package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

/**
 * Calcula o total de fêmeas ativas no rebanho.

 * @author ruminiki
 */


@Service
public class TaxaPrenhez extends AbstractCalculadorIndicador{

	@Override
	public String getValue() {

		return String.valueOf(BigDecimal.ZERO);
		
	}
	
}
