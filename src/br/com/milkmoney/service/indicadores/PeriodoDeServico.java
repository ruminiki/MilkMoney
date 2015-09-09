package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.ConfirmacaoPrenhes;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.ConfirmacaoPrenhesService;
import br.com.milkmoney.service.PartoService;
import br.com.milkmoney.util.DateUtil;

/**
 * É o tempo decorrido entre o parto e a nova concepção, medido em dias.
 * @author ruminiki
 */


@Service
public class PeriodoDeServico extends AbstractCalculadorIndicador{

	@Autowired private AnimalDao animalDao;
	@Autowired private CoberturaService coberturaService;
	@Autowired private PartoService partoService;
	@Autowired private ConfirmacaoPrenhesService confirmacaoPrenhesService;
	
	@Override
	public BigDecimal getValue() {

		BigDecimal periodo = BigDecimal.ZERO;
		List<Animal> vacas = animalDao.findAnimaisComParto();
		
		int vacasComConfirmacao = 0;
		
		for ( Animal femea : vacas ){
			
			Parto ultimoParto = partoService.findLastParto(femea);
			if ( ultimoParto != null ){
				
				ConfirmacaoPrenhes confirmacao = confirmacaoPrenhesService.findFirstAfterData(femea, ultimoParto.getData(), SituacaoCobertura.PRENHA);
				if ( confirmacao != null ){
					periodo = periodo.add(BigDecimal.valueOf(ChronoUnit.DAYS.between(DateUtil.asLocalDate(ultimoParto.getData()), DateUtil.asLocalDate(confirmacao.getData()))));
					vacasComConfirmacao++;
				}
				
			}
			
		}
		
		if ( vacasComConfirmacao > 0 && periodo.compareTo(BigDecimal.ZERO) > 0 ){
			periodo = periodo.divide(BigDecimal.valueOf(vacasComConfirmacao), 2, RoundingMode.HALF_EVEN);
		}
		
		return periodo;
		
	}
	
}
