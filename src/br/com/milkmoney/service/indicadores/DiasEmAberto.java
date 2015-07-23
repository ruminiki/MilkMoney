package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.service.CoberturaService;

/**
 *Para o cálculo do dias em aberto devemos considerar o número de dias do último parto até:
 *A data da concepção das vacas gestantes
 *A data da última cobertura das vacas ainda não confirmadas gestantes
 *Ou a data em que o cálculo foi realizado.
 *As vacas já definidas como descarte, mas que ainda estão em lactação, não precisam ser incluídas nos cálculo
 *O cálculo da média dos dias em aberto é feito pela soma dos dias em aberto de cada vaca divido pelo número de vacas do rebanho.
 * 
 * @author ruminiki
 */


@Service
public class DiasEmAberto extends AbstractCalculadorIndicador{
	
	@Autowired CoberturaService coberturaService;
	@Autowired AnimalDao animalDao;

	@Override
	public String getValue() {
		BigDecimal diasEmAberto = BigDecimal.ZERO;
		
		List<Animal> vacas = animalDao.findAnimaisComParto();
		
		for ( Animal femea : vacas ){
			diasEmAberto = diasEmAberto.add(BigDecimal.valueOf(coberturaService.getDiasEmAberto(femea)));
		}
		
		if ( vacas != null && vacas.size() > 0 ){
			diasEmAberto = diasEmAberto.divide(BigDecimal.valueOf(vacas.size()), 2, RoundingMode.HALF_EVEN);
		}
		
		return String.valueOf(diasEmAberto);
		
	}
	
}
