package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.util.DateUtil;

/**
 * A taxa de detecção de cio (TDC) é calculada dividindo o número de vacas inseminadas no período de 21 dias 
 * pelo número de vacas disponíveis para serem inseminadas no mesmo período.
 * 
 * A taxa de concepção (TC) do rebanho, dividindo o número de vacas que ficaram gestantes no período de 21 dias 
 * pelo número de vacas inseminadas. 
 * 
 * Logo precisa ser buscado as coberturas realizadas entre 42 e 21 dias atrás e verificar quantas ficaram prenhas
 * ou não vazias e dividir pelo total de coberturas realizadas naquele período.
 * 
 * A taxa de prenhez representa o número de vacas que ficaram prenhes em relação ao número de vacas APTAS a ficarem prenhes.
 * 
 * **Correção
 * http://rehagro.com.br/plus/modulos/noticias/ler.php?cdnoticia=2394
 * Então, Taxa de prenhez = Taxa de serviço x Taxa de concepção??
 * http://rehagro.com.br/plus/modulos/noticias/ler.php?cdnoticia=1405
 * http://rehagro.com.br/plus/modulos/noticias/ler.php?cdnoticia=1002
 * 
 * 
 * @author ruminiki
 */


@Service
public class TaxaConcepcao extends AbstractCalculadorIndicador{

	@Autowired private CoberturaDao coberturaDao;
	
	@Override
	public BigDecimal getValue() {

		long coberturasRealizadas = 0;
		long concepcoes = 0;
		
		Date dataInicio = DateUtil.asDate(LocalDate.now().minusDays(42));
		Date dataFim = DateUtil.asDate(LocalDate.now().minusDays(21));
		
		List<Cobertura> coberturas = coberturaDao.findCoberturasPeriodo(dataInicio, dataFim); 
		
		for ( Cobertura cobertura : coberturas ){
			if ( cobertura.getSituacaoCobertura().matches(SituacaoCobertura.PRENHA + "|" + SituacaoCobertura.NAO_CONFIRMADA) ){
				concepcoes ++;
			}
		}
		
		coberturasRealizadas = coberturas != null ? coberturas.size() : 0;
		
		return concepcoes > 0 ? BigDecimal.valueOf(concepcoes).divide(BigDecimal.valueOf(coberturasRealizadas), 2, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100)) : BigDecimal.ZERO;
		
	}
	
}
