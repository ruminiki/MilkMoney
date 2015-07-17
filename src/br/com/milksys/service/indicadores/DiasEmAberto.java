package br.com.milksys.service.indicadores;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

/**
 *Para o cálculo do dias em aberto devemos considerar o número de dias do último parto até:
 *A data da concepção das vacas gestantes
 *A data da última cobertura das vacas ainda não confirmadas gestantes
 *Ou a data em que o cálculo foi realizado.
 *As vacas já definidas como descarte, mas que ainda estão em lactação, não precisam ser incluídas nos cálculo
 *O cálculo da média dos dias em aberto é feito pela soma dos dias em aberto de cada vaca divido pelo número de vacas do rebanho.
 * 
 * @author ruminiki
 */


@Service
public class DiasEmAberto extends AbstractCalculadorIndicador{

	@Override
	public String getValue() {
		BigDecimal diasEmAberto = BigDecimal.ZERO;
		
		return String.valueOf(diasEmAberto);
		
	}
	
}
