package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.util.DateUtil;

/**
 * Taxa de serviço é definida como a porcentagem de vacas aptas a serem inseminadas no período de 21 dias que são realmente inseminadas. 
 * A taxa de serviço é reflexo da taxa de detecção de cio, pois para a vaca ser inseminada, tem que ser detectada em cio antes. 
 * 
 * TDC - dividindo o número de vacas inseminadas no período de 21 dias pelo número de vacas 
 * disponíveis para serem inseminadas no mesmo período.
 * 
 * http://www.milkpoint.com.br/radar-tecnico/reproducao/manejo-reprodutivo-do-rebanho-leiteiro-26245n.aspx
 * 
 * http://www.milkpoint.com.br/radar-tecnico/reproducao/estrategias-de-manejo-para-aumentar-a-eficiencia-reprodutiva-de-vacas-de-leite-28283n.aspx
 * 
 * PVE - Periodo voluntário de espera (dias após o parto em que a vaca não deve ser inseminada)
 * 
 * http://rehagro.com.br/plus/modulos/noticias/ler.php?cdnoticia=1002
 */

@Service
public class TaxaServico extends AbstractCalculadorIndicador{

	@Autowired private CoberturaDao coberturaDao;
	@Autowired private AnimalService animalService;
	
	@Override
	public BigDecimal getValue(Date data) {
		
		//analisa coberturas do mes corrente
		Date dataInicio = DateUtil.asDate(LocalDate.of(DateUtil.asLocalDate(data).getYear(), DateUtil.asLocalDate(data).getMonthValue(), 1));
		Date dataFim    = data;
		
		//busca os animais disponíveis no período
		List<Animal> animaisDisponiveis = animalService.findAnimaisDisponiveisParaCobertura(dataInicio, dataFim);
		long vacasDisponiveis = animaisDisponiveis != null ? animaisDisponiveis.size() : 0;
		
		//busca todas as coberturas realizadas no periodo 
		List<Cobertura> coberturas = coberturaDao.findCoberturasPeriodo(dataInicio, dataFim); 
		long vacasEnseminadas = coberturas != null ? coberturas.size() : 0;
		
		if ( vacasEnseminadas <= 0 || vacasDisponiveis <= 0 ){
			return BigDecimal.ZERO;
		}
		
		return BigDecimal.valueOf(vacasEnseminadas).divide(BigDecimal.valueOf(vacasDisponiveis), 2, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100));
		
	}
	
}
