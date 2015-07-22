package br.com.milksys.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.PartoDao;
import br.com.milksys.model.Parto;
import br.com.milksys.service.PartoService;

/**
 * Dias em Lactação
 * 1. Buscar todos os partos ocorridos até a data atual
 * 2. Conta os dias de lactação de cada um deles, considerando o fim da lactação como
 * 1. Registro de encerramento da lactação;
 * 2. Morte do animal;
 * 3. Venda do animal.
 * 3. Divide o total de dias de lactação pelo número de lactações.
 * 
 * @author ruminiki
 *
 */


@Service
public class DiasEmLactacao extends AbstractCalculadorIndicador{

	@Autowired private PartoDao partoDao;
	@Autowired private PartoService partoService;
	
	@Override
	public String getValue() {
		BigDecimal diasEmLactacao = BigDecimal.ZERO;
		int        totalPartos    = 0;
		
		List<Parto> partos = partoDao.findAllOrderByDataDesc();
		
		for ( Parto parto : partos ){
			
			BigDecimal diasLactacaoParto = BigDecimal.valueOf(partoService.contaDiasLactacaoParto(parto));
			diasEmLactacao = diasEmLactacao.add(diasLactacaoParto);
			totalPartos++;
			
		}
		
		if ( diasEmLactacao.compareTo(BigDecimal.ZERO) > 0 && totalPartos > 0 ){
			diasEmLactacao = diasEmLactacao.divide(new BigDecimal(totalPartos), 2, RoundingMode.HALF_UP);
		}
		
		return String.valueOf(diasEmLactacao);
		
	}
	
}
