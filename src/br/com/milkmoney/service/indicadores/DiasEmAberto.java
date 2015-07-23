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
 *Para o c�lculo do dias em aberto devemos considerar o n�mero de dias do �ltimo parto at�:
 *A data da concep��o das vacas gestantes
 *A data da �ltima cobertura das vacas ainda n�o confirmadas gestantes
 *Ou a data em que o c�lculo foi realizado.
 *As vacas j� definidas como descarte, mas que ainda est�o em lacta��o, n�o precisam ser inclu�das nos c�lculo
 *O c�lculo da m�dia dos dias em aberto � feito pela soma dos dias em aberto de cada vaca divido pelo n�mero de vacas do rebanho.
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
