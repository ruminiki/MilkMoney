package br.com.milkmoney.service.fichaAnimal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.model.Parametro;
import br.com.milkmoney.service.LactacaoService;
import br.com.milkmoney.service.ParametroService;
import br.com.milkmoney.util.DateUtil;

@Service
public class EficienciaTempoProducao extends AbstractFichaAnimal {

	@Autowired LactacaoService lactacaoService;
	@Autowired ParametroService parametroService;
	
	@Override
	public void load(Object ... params) {
		
		FichaAnimal ficha = (FichaAnimal) params[0];
		Animal animal     = (Animal) params[1];
		
		int idadePrimeiraCobertura = Integer.parseInt(parametroService.findBySigla(Parametro.IDADE_MINIMA_PARA_COBERTURA));
		int idadeProdutiva = animal.getIdade() - idadePrimeiraCobertura;
		
		BigDecimal lactacoesIdeal, mesesProducaoIdeal = BigDecimal.ZERO;
		
		if ( idadeProdutiva > 0 ){
			lactacoesIdeal = BigDecimal.valueOf(idadeProdutiva).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_EVEN);
			mesesProducaoIdeal = lactacoesIdeal.multiply(BigDecimal.valueOf(10));
		}
		
		int mesesProduzindo = 0;
		for ( Lactacao l : lactacaoService.findLactacoesAnimal(animal) ){
			mesesProduzindo += ChronoUnit.MONTHS.between(
							DateUtil.asLocalDate(l.getDataInicio()), 
							(l.getDataFim() == null ? LocalDate.now() : DateUtil.asLocalDate(l.getDataFim())));
		}
		
		ficha.setMesesProducaoIdeal(mesesProducaoIdeal);
		ficha.setMesesProduzindo(mesesProduzindo);
		
		if ( mesesProducaoIdeal.compareTo(BigDecimal.ZERO) > 0 ){
			ficha.setEficienciaTempoProducao(
					BigDecimal.valueOf(mesesProduzindo)
					.divide(mesesProducaoIdeal, 2, RoundingMode.HALF_EVEN)
					.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_EVEN));
		}else{
			ficha.setEficienciaTempoProducao(BigDecimal.ZERO);
		}
		
	}

}
