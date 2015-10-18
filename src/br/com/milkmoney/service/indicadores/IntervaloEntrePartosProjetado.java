package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * O objetivo dessa comparação é saber se a eficiência reprodutiva da fazenda nos últimos 9 meses esta melhorando, se mantendo ou piorando.
 * O cálculo exato do intervalo entre parto projetado é feito da seguinte forma:
 * Dias em aberto mais o período de gestação médio da raça do rebanho (Holandesa 279 dias, Jersey 279, Gir 290 dias), 
 * dividido por 30,25 dias/mês (para se corrigir a diferença entre meses com 28, 30 e 31 dias).
 * Exemplo:
 * Dias em aberto = 123 dias
 * Período de gestação = 279 dias
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
		//TODO solicitar essa informação do usuário
		BigDecimal periodoGestacaoRaca = BigDecimal.valueOf(282);
		
		return diasEmAberto.add(periodoGestacaoRaca).divide(BigDecimal.valueOf(30.5),2,RoundingMode.HALF_EVEN);
		
	}
	
	@Override
	public String getFormat() {
		return AbstractCalculadorIndicador.INTEIRO_FORMAT;
	}
	
	
}
