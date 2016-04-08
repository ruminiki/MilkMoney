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
 * A taxa de detec��o de cio (TDC) � calculada dividindo o n�mero de vacas inseminadas no per�odo de 21 dias 
 * pelo n�mero de vacas dispon�veis para serem inseminadas no mesmo per�odo.
 * 
 * A taxa de concep��o (TC) do rebanho, dividindo o n�mero de vacas que ficaram gestantes no per�odo de 21 dias 
 * pelo n�mero de vacas inseminadas. 
 * 
 * Logo precisa ser buscado as coberturas realizadas entre 42 e 21 dias atr�s e verificar quantas ficaram prenhas
 * ou n�o vazias e dividir pelo total de coberturas realizadas naquele per�odo.
 * 
 * A taxa de prenhez representa o n�mero de vacas que ficaram prenhez em rela��o ao n�mero de vacas APTAS a ficarem prenhez.
 * 
 * **Corre��o
 * http://rehagro.com.br/plus/modulos/noticias/ler.php?cdnoticia=2394
 * Ent�o, Taxa de prenhez = Taxa de servi�o x Taxa de concep��o??
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
	public BigDecimal getValue(Date data) {

		long coberturasRealizadas = 0;
		long concepcoes = 0;
		
		//analisa coberturas do ultimo mes
		//Date dataInicio = DateUtil.asDate(LocalDate.of(DateUtil.asLocalDate(data).minusMonths(1).getYear(), DateUtil.asLocalDate(data).minusMonths(1).getMonthValue(), 01));
		//Date dataFim    = DateUtil.asDate(DateUtil.asLocalDate(data).minusMonths(1));
		
		Date dataInicio = DateUtil.asDate(LocalDate.of(DateUtil.asLocalDate(data).getYear(), DateUtil.asLocalDate(data).getMonthValue(), 1));
		Date dataFim    = data;
		
		List<Cobertura> coberturas = coberturaDao.findCoberturasPeriodoVacasAtivas(dataInicio, dataFim); 
		
		for ( Cobertura cobertura : coberturas ){
			if ( cobertura.getSituacaoConfirmacaoPrenhez().matches(SituacaoCobertura.PRENHA + "|" + SituacaoCobertura.NAO_CONFIRMADA) ){
				
				if ( cobertura.getAborto() != null ){
					if ( cobertura.getAborto().getData().compareTo(data) > 0 ){
						concepcoes ++;
					}
				}
				
				if ( cobertura.getParto() != null ){
					if ( cobertura.getParto().getData().compareTo(data) > 0 ){
						concepcoes ++;
					}
				}
				
				if ( cobertura.getParto() == null && cobertura.getAborto() == null ){
					concepcoes ++;
				}
				
			}
		}
		
		coberturasRealizadas = coberturas != null ? coberturas.size() : 0;
		
		return concepcoes > 0 ? BigDecimal.valueOf(concepcoes).divide(BigDecimal.valueOf(coberturasRealizadas), 2, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100)) : BigDecimal.ZERO;
		
	}
	
}
