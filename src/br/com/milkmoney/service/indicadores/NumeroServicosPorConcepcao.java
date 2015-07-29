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
	A média de número de serviços por concepção do rebanho indica a 
	fertilidade das vacas que foram cobertas e ficaram gestantes, as vacas descarte e as 
	vacas que repeat breeder (vacas com mais de 4 coberturas) não diagnosticadas gestantes não entram nesse índice.

	O cálculo do número de serviços por concepção é feito da seguinte forma:
	
	Calcular o número de coberturas das vacas gestantes
	A média é obtida pela soma das coberturas dividida pelo número de vacas gestantes.
	
	A baixa fertilidade pode ser decorrente de detecção de cio inadequada, falhas nas técnicas de inseminação artificial, 
	sêmen com problemas (armazenamento ou infertilidade do touro) ou problemas nas vacas (infecção uterina, 
	doenças infecciosas, etc...).

	Esse índice isoladamente não reflete a eficiência reprodutiva do rebanho, pois podemos ter 
	baixo número de serviço por concepção, mas poucas vacas do rebanho estão gestantes ou as vacas 
	estão com dias em aberto muito longo por falhas na detecção de cio (ver radar publicado no dia 
	05/11/2007: Avaliação da eficiência reprodutiva de três rebanhos leiteiros).
	
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
				//começa a percorrer as coberturas anteriores contando todas que não tiveram parto.
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
