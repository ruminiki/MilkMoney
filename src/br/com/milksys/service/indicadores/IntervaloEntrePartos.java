package br.com.milksys.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.AnimalDao;
import br.com.milksys.dao.PartoDao;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Parto;
import br.com.milksys.util.DateUtil;

/**
 * O intervalo entre partos atual é o cálculo do número de meses entre o parto mais recente e o 
 * parto anterior das vacas com mais de um parto. Neste dado não entram as vacas de primeira lactação.
 * http://www.milkpoint.com.br/radar-tecnico/reproducao/interpretacao-dos-indices-da-eficiencia-reprodutiva-41269n.aspx
 * 
 * Verificar se incluem as vacas mortas e vendidas
 * 
 * @author ruminiki
 */


@Service
public class IntervaloEntrePartos extends AbstractCalculadorIndicador{

	@Autowired AnimalDao animalDao;
	@Autowired PartoDao partoDao;
	
	
	@Override
	public String getValue() {
		BigDecimal intervaloEntrePartos = BigDecimal.ZERO;
		
		//vacas com mais de um parto
		List<Animal> femeas = animalDao.findAnimaisComMaisDeUmParto();
		
		for ( Animal femea : femeas ){
			
			List<Parto> partos = partoDao.findUltimos2Partos(femea);
			
			if ( partos != null && partos.size() == 2 ){
				//soma o intervalo de meses entre um e outro
				intervaloEntrePartos = BigDecimal.valueOf(ChronoUnit.MONTHS.between(DateUtil.asLocalDate(partos.get(1).getData()), DateUtil.asLocalDate(partos.get(0).getData())));
				
			}
			
		}
		//divide a soma dos intervalos pelo número de partos para obter a média
		if ( intervaloEntrePartos.compareTo(BigDecimal.ZERO) > 0 ){
			
			intervaloEntrePartos = intervaloEntrePartos.divide(BigDecimal.valueOf(femeas.size()), 2, RoundingMode.HALF_EVEN);
			
		}
		
		return String.valueOf(intervaloEntrePartos);
		
	}
	
}
