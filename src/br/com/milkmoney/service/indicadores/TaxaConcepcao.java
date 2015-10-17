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
 * O intervalo da primeira IA at� a concep��o representa a taxa de prenhes, ou seja, 
 * a velocidade em que a vaca fica gestante. Esse intervalo varia muito entre vacas do mesmo rebanho.
 * Dois fatores determinam a taxa de prenhes:
 * 1 - Taxa de concep��o;
 * 2 - Taxa de detec��o de cio ou taxa de servi�o. 
 * http://www.milkpoint.com.br/radar-tecnico/reproducao/estrategias-de-manejo-para-aumentar-a-eficiencia-reprodutiva-de-vacas-de-leite-28283n.aspx
 * 
 * **Corre��o
 * http://rehagro.com.br/plus/modulos/noticias/ler.php?cdnoticia=2394
 * Ent�o, Taxa de prenhez = Taxa de servi�o x Taxa de concep��o
 * 
 * http://rehagro.com.br/plus/modulos/noticias/ler.php?cdnoticia=1405
 * 
 * 
 * http://www.milkpoint.com.br/radar-tecnico/reproducao/manejo-reprodutivo-do-rebanho-leiteiro-26245n.aspx
 * 
 * A taxa de detec��o de cio (TDC) � calculada dividindo o n�mero de vacas inseminadas no per�odo de 21 dias 
 * pelo n�mero de vacas dispon�veis para serem inseminadas no mesmo per�odo.
 * 
 * a taxa de concep��o (TC) do rebanho, dividindo o n�mero de vacas que ficaram gestantes no per�odo de 21 dias 
 * pelo n�mero de vacas inseminadas. 
 * 
 * para obter a taxa de prenhez, devemos multiplicar a taxa de detec��o de cio pela taxa de concep��o. 
 * 
 * @author ruminiki
 */


@Service
public class TaxaConcepcao extends AbstractCalculadorIndicador{

	@Autowired private AnimalDao animalDao;
	@Autowired private CoberturaService coberturaService;
	
	@Override
	public BigDecimal getValue() {

		List<Animal> animais = animalDao.findAnimaisComCobertura(); 
		BigDecimal somaMediaIntervaloPrimeiraCoberturaAtePrenhes = BigDecimal.ZERO;
		
		for ( Animal animal : animais ){
			somaMediaIntervaloPrimeiraCoberturaAtePrenhes = somaMediaIntervaloPrimeiraCoberturaAtePrenhes.add(coberturaService.getMediaIntervaloPrimeiraCoberturaAtePrenhes(animal));
		}
		
		if ( animais != null && animais.size() > 0 && somaMediaIntervaloPrimeiraCoberturaAtePrenhes.compareTo(BigDecimal.ZERO) > 0 ){
			return somaMediaIntervaloPrimeiraCoberturaAtePrenhes.divide(BigDecimal.valueOf(animais.size()), 2, RoundingMode.HALF_EVEN);
		}
		
		return BigDecimal.ZERO;
		
	}
	
}
