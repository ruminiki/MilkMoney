package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.util.DateUtil;

/**
 * A taxa de detec��o de cio (TDC) � calculada dividindo o n�mero de vacas inseminadas no per�odo de 21 dias 
 * pelo n�mero de vacas dispon�veis para serem inseminadas no mesmo per�odo.
 * 
 * A taxa de concep��o (TC) do rebanho, dividindo o n�mero de vacas que ficaram gestantes no per�odo de 21 dias 
 * pelo n�mero de vacas inseminadas. 
 * 
 * A taxa de prenhez representa o n�mero de vacas que ficaram prenhes em rela��o ao n�mero de vacas APTAS a ficarem prenhes.
 * 
 * @author ruminiki
 */


@Service
public class TaxaPrenhes extends AbstractCalculadorIndicador{

	@Autowired private CoberturaDao coberturaDao;
	@Autowired private AnimalDao animalDao;
	@Autowired private AnimalService animalService;
	
	@Override
	public BigDecimal getValue(Date data) {

		BigDecimal concepcoes = BigDecimal.ZERO;
		
		//analisa coberturas do ultimo mes
		Date dataInicio = DateUtil.asDate(LocalDate.of(DateUtil.asLocalDate(data).getYear(), DateUtil.asLocalDate(data).getMonthValue(), 1));
		Date dataFim    = data;
		
		//busca os animais dispon�veis no per�odo
		List<Animal> animaisDisponiveis = animalService.findAnimaisDisponiveisParaCobertura(dataInicio, dataFim);
		
		//busca todas as coberturas realizadas no periodo 
		List<Cobertura> coberturas = coberturaDao.findCoberturasPeriodo(dataInicio, dataFim); 
		
		for ( Cobertura cobertura : coberturas ){
			if ( cobertura.getSituacaoConfirmacaoPrenhes().matches(SituacaoCobertura.PRENHA + "|" + SituacaoCobertura.NAO_CONFIRMADA) ){
				//contabiliza todas as concep��es
				concepcoes = concepcoes.add(BigDecimal.ONE);
			}
		}
		
		BigDecimal disponiveis = animaisDisponiveis != null ? BigDecimal.valueOf(animaisDisponiveis.size()) : BigDecimal.ZERO;
		
		return concepcoes.compareTo(BigDecimal.ZERO) > 0 && disponiveis.compareTo(BigDecimal.ZERO) > 0 ? 
						concepcoes.divide(disponiveis, 2, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100)) : BigDecimal.ZERO;

	}
	
}
