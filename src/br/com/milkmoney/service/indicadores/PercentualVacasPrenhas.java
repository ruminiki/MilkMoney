package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.SituacaoCobertura;

/**
 * Calcula o percentual de vacas prenhas no rebanho.

 * @author ruminiki
 */


@Service
public class PercentualVacasPrenhas extends AbstractCalculadorIndicador{

	@Autowired AnimalDao animalDao;
	@Autowired CoberturaDao coberturaDao;
	
	@Override
	public BigDecimal getValue(Date data) {

		BigDecimal   rebanho      = BigDecimal.valueOf(animalDao.countAllVacasAtivas(data).longValue());
		List<Animal> animais      = animalDao.findAllVacasAtivas(data);
		BigDecimal   vacasPrenhas = BigDecimal.ZERO;
		BigDecimal   result       = BigDecimal.ZERO;
		
		for ( Animal a : animais ){
			Cobertura cobertura = coberturaDao.findFirstBeforeDate(a, data);
			
			if ( cobertura.getSituacaoConfirmacaoPrenhez().matches(SituacaoCobertura.PRENHA + "|" + SituacaoCobertura.NAO_CONFIRMADA) ){
				
				if ( cobertura.getAborto() != null ){
					if ( cobertura.getAborto().getData().compareTo(data) > 0 ){
						vacasPrenhas = vacasPrenhas.add(BigDecimal.ONE);
					}
				}
				
				if ( cobertura.getParto() != null ){
					if ( cobertura.getParto().getData().compareTo(data) > 0 ){
						vacasPrenhas = vacasPrenhas.add(BigDecimal.ONE);
					}
				}
				
				if ( cobertura.getParto() == null && cobertura.getAborto() == null ){
					vacasPrenhas = vacasPrenhas.add(BigDecimal.ONE);
				}
				
			}
			
		}
		
		if ( rebanho.compareTo(BigDecimal.ZERO) > 0 && vacasPrenhas.compareTo(BigDecimal.ZERO) > 0 ){
			result = vacasPrenhas.divide(rebanho,2,RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100));
		}
		
		return result;
		
	}
	
}
