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
 * Taxa de servi�o � definida como a porcentagem de vacas aptas a serem inseminadas no per�odo de 21 dias que s�o realmente inseminadas. 
 * A taxa de servi�o � reflexo da taxa de detec��o de cio, pois para a vaca ser inseminada, tem que ser detectada em cio antes. 
 * 
 * TDC - dividindo o n�mero de vacas inseminadas no per�odo de 21 dias pelo n�mero de vacas 
 * dispon�veis para serem inseminadas no mesmo per�odo.
 * 
 * http://www.milkpoint.com.br/radar-tecnico/reproducao/manejo-reprodutivo-do-rebanho-leiteiro-26245n.aspx
 * 
 * http://www.milkpoint.com.br/radar-tecnico/reproducao/estrategias-de-manejo-para-aumentar-a-eficiencia-reprodutiva-de-vacas-de-leite-28283n.aspx
 * 
 * PVE - Periodo volunt�rio de espera (dias ap�s o parto em que a vaca n�o deve ser inseminada)
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
		
		//busca os animais dispon�veis no per�odo
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
