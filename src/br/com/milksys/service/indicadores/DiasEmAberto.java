package br.com.milksys.service.indicadores;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

/**
 *Para o c�lculo do dias em aberto devemos considerar o n�mero de dias do �ltimo parto at�:
 *A data da concep��o das vacas gestantes
 *A data da �ltima cobertura das vacas ainda n�o confirmadas gestantes
 *Ou a data em que o c�lculo foi realizado.
 *As vacas j� definidas como descarte, mas que ainda est�o em lacta��o, n�o precisam ser inclu�das nos c�lculo
 *O c�lculo da m�dia dos dias em aberto � feito pela soma dos dias em aberto de cada vaca divido pelo n�mero de vacas do rebanho.
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
