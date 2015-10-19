package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.PartoDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.service.CoberturaService;

/**
	A m�dia de n�mero de servi�os por concep��o do rebanho indica a 
	fertilidade das vacas que foram cobertas e ficaram gestantes, as vacas descarte e as 
	vacas que repeat breeder (vacas com mais de 4 coberturas) n�o diagnosticadas gestantes n�o entram nesse �ndice.

	O c�lculo do n�mero de servi�os por concep��o � feito da seguinte forma:
	
	Calcular o n�mero de coberturas das vacas gestantes
	A m�dia � obtida pela soma das coberturas dividida pelo n�mero de vacas gestantes.
	
	A baixa fertilidade pode ser decorrente de detec��o de cio inadequada, falhas nas t�cnicas de insemina��o artificial, 
	s�men com problemas (armazenamento ou infertilidade do touro) ou problemas nas vacas (infec��o uterina, 
	doen�as infecciosas, etc...).

	Esse �ndice isoladamente n�o reflete a efici�ncia reprodutiva do rebanho, pois podemos ter 
	baixo n�mero de servi�o por concep��o, mas poucas vacas do rebanho est�o gestantes ou as vacas 
	est�o com dias em aberto muito longo por falhas na detec��o de cio (ver radar publicado no dia 
	05/11/2007: Avalia��o da efici�ncia reprodutiva de tr�s rebanhos leiteiros).
	
	< 1,75 = boa
	1,76 a 2,00 = adequado
	2,01 a 2,30 = problemas moderados
	> 2,30 = problemas severos
	
	http://www.milkpoint.com.br/radar-tecnico/reproducao/interpretacao-dos-indices-da-eficiencia-reprodutiva-41269n.aspx
 * 
 * @author ruminiki
 *
 */


@Service
public class NumeroServicosPorConcepcao extends AbstractCalculadorIndicador{

	@Autowired private PartoDao partoDao;
	@Autowired private AnimalDao animalDao;
	@Autowired private CoberturaService coberturaService;
	
	@Override
	public BigDecimal getValue() {
		
		long numeroServicos = 0;
		long concepcoes = 0;
		
		List<Animal> animais = animalDao.findAllFemeasAtivas();
		
		for ( Animal animal : animais ){
			
			int servicos = coberturaService.getNumeroServicosPorConcepcao(animal);
			numeroServicos += servicos;
			
			concepcoes += servicos > 0 ? 1 : 0;
			
		}
		
		return numeroServicos > 0 ? BigDecimal.valueOf(numeroServicos).divide(BigDecimal.valueOf(concepcoes), 1, RoundingMode.HALF_EVEN) : BigDecimal.ZERO;
		
	}
	
}
