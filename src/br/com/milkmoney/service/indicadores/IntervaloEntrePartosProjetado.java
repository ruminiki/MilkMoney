package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * O objetivo dessa compara��o � saber se a efici�ncia reprodutiva da fazenda nos �ltimos 9 meses esta melhorando, se mantendo ou piorando.
 * O c�lculo exato do intervalo entre parto projetado � feito da seguinte forma:
 * Dias em aberto mais o per�odo de gesta��o m�dio da ra�a do rebanho (Holandesa 279 dias, Jersey 279, Gir 290 dias), 
 * dividido por 30,25 dias/m�s (para se corrigir a diferen�a entre meses com 28, 30 e 31 dias).
 * Exemplo:
 * Dias em aberto = 123 dias
 * Per�odo de gesta��o = 279 dias
 * Intervalo entre parto projetado (IEPP) = (123 + 279)/30,25 = 13,3 meses
 * http://www.milkpoint.com.br/radar-tecnico/reproducao/interpretacao-dos-indices-da-eficiencia-reprodutiva-41269n.aspx
 * 
 * @author ruminiki
 */


@Service
public class IntervaloEntrePartosProjetado extends AbstractCalculadorIndicador{

	@Autowired private PeriodoServico diasEmAbertoService;
	
	@Override
	public BigDecimal getValue() {

		BigDecimal diasEmAberto = diasEmAbertoService.getValue();
		//TODO solicitar essa informa��o do usu�rio
		BigDecimal periodoGestacaoRaca = BigDecimal.valueOf(282);
		
		return diasEmAberto.add(periodoGestacaoRaca).divide(BigDecimal.valueOf(30.5),2,RoundingMode.HALF_EVEN);
		
	}
	
	@Override
	public String getFormat() {
		return AbstractCalculadorIndicador.INTEIRO_FORMAT;
	}
	
	
}
