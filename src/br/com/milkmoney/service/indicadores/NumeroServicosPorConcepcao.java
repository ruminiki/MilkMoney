package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.dao.PartoDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.SituacaoCobertura;

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
	@Autowired private CoberturaDao coberturaDao;
	
	@Override
	public BigDecimal getValue() {
		long numeroServicosPorConcepcao = 0;
		
		List<Animal> animais = animalDao.findFemeasPrenhas();
		
		for ( Animal animal : animais ){
			//recupera a cobertura mais recente
			Cobertura coberturaPrenha = coberturaDao.findLastCoberturaAnimal(animal);
			
			if ( coberturaPrenha != null ){
				//contabiliza a cobertura encontrada
				numeroServicosPorConcepcao += 1;
				//come�a a percorrer as coberturas anteriores contando todas que n�o tiveram parto.
				//ao encontrar uma com parto para a contagem.
				numeroServicosPorConcepcao = countNumeroServicosAtePrenhes(animal, coberturaPrenha.getData(), numeroServicosPorConcepcao);
			}
			
		}
		
		if ( numeroServicosPorConcepcao > 0 ){
			numeroServicosPorConcepcao = numeroServicosPorConcepcao / animais.size();
		}
		
		return BigDecimal.valueOf(numeroServicosPorConcepcao).setScale(2);
		
	}

	private long countNumeroServicosAtePrenhes(Animal femea, Date data, long numeroServicosPorConcepcao) {
		
		Cobertura coberturaAnterior = coberturaDao.findFirstBeforeDate(femea, data);
		if ( coberturaAnterior.getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
			return numeroServicosPorConcepcao;
		}else{
			numeroServicosPorConcepcao += 1;
			return countNumeroServicosAtePrenhes(femea, coberturaAnterior.getData(), numeroServicosPorConcepcao);
		}
		
	}
	
}
