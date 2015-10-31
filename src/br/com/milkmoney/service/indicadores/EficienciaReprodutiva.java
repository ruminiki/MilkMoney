package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.dao.MorteAnimalDao;
import br.com.milkmoney.dao.PartoDao;
import br.com.milkmoney.dao.VendaAnimalDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.MorteAnimal;
import br.com.milkmoney.model.SituacaoAnimal;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.model.TipoParto;
import br.com.milkmoney.model.VendaAnimal;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.util.DateUtil;

/**
 * Método Butendieck (1972)
 * 
 *
 * Somar os dias de gestação do rebanho no período
 * 	1 - Encontrar as vacas que tiveram coberturas e/ou estavam prenhas no período.
 *  2 - Somar os dias de ingresso (novilhas) e eliminação (descartes)
 *  3 - Não contabiliar como dias gestantes os abortos.
 * 
 * @author ruminiki
 */


@Service
public class EficienciaReprodutiva extends AbstractCalculadorIndicador{

	@Autowired private CoberturaDao coberturaDao;
	@Autowired private MorteAnimalDao morteAnimalDao;
	@Autowired private VendaAnimalDao vendaAnimalDao;
	@Autowired private PartoDao partoDao;
	@Autowired private AnimalDao animalDao;
	@Autowired private AnimalService animalService;
	
	@Override
	public BigDecimal getValue() {

		double DVG  = 0;   //dias de gestação do rebanho
		double DVE  = 0;   //dias fora do rebanho (data de inicio até a primeira cobertura) e/ou (data venda/morte até a data fim)
		double DG   = 280; //duração média da gestação
		double R    = 85;  //período seco ideal
		double N    = 0;   //número total de vacas consideradas
		double P    = 2;   //intervalo em anos
		double DIAS = 365; //constante 
		
		//período de 1 ano
		Date dataInicio = DateUtil.asDate(LocalDate.now().minusYears(2));
		Date dataFim = new Date();
		
		//seleciona os animais que compõem o cálculo
		//fêmeas com partos ou coberturas no período
		//a data de ingresso é contado a partir da primeira cobertura de novilhas
		//a data de eliminação a partir da data de morte ou venda
		//abortos não são considerados nos dias gestantes
		
		//busca os animais que farão parte do índice
		List<Animal> animais = animalDao.findAnimaisParaCalculoEficiencia(dataInicio, dataFim);
		
		for ( Animal animal : animais ){
			
			List<Cobertura> coberturas = coberturaDao.findCoberturasIniciadasOuComPartoNoPeriodo(animal, dataInicio, dataFim);
			
			for ( Cobertura cobertura : coberturas ){
				
				//se tiver parto, conta os dias de gestação até a data do parto
				if ( cobertura.getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
					
					//abortos não são contabilizados nos dias gestantes
					if ( cobertura.getParto().getTipoParto().equals(TipoParto.ABORTO) ){
						continue;
					}
					
					//verifica se a cobertura foi realizada dentro do período
					//|---------------------------|(período avaliado)
					//    IC|************|Parto (cobertura com parto)
					if ( cobertura.getData().compareTo(dataInicio) >= 0 &&  cobertura.getData().compareTo(dataFim) <= 0){
						DVG += ChronoUnit.DAYS.between(DateUtil.asLocalDate(cobertura.getData()), DateUtil.asLocalDate(cobertura.getParto().getData()));	
					}else{
						//      |---------------------------|(período avaliado)
						//IC|---*********|Parto (cobertura com parto)
						DVG += ChronoUnit.DAYS.between(DateUtil.asLocalDate(dataInicio), DateUtil.asLocalDate(cobertura.getParto().getData()));						
					}
					
				}
				
				if ( cobertura.getSituacaoCobertura().matches(SituacaoCobertura.NAO_CONFIRMADA + "|" + SituacaoCobertura.PRENHA) ){
					//caso de cobertura que não foi registrado o parto e não foi confirmada vazia (já passado do tempo) não considerar como gestante
					//       |---------------------------|(período avaliado)
					// IC|---**************************** (cobertura não confirmada e sem parto)
					
					//se o parto já deveria ter ocorrido, não considera pois o produtor esqueceu de marcar
					if ( cobertura.getPrevisaoParto().compareTo(dataFim) > 0 ){
						DVG += ChronoUnit.DAYS.between(DateUtil.asLocalDate(cobertura.getData()), DateUtil.asLocalDate(dataFim));						
					}
					
				}
				
				//se for a primeira cobertura do animal - considera a entrada dele no rebanho
				Cobertura primeiraCoberturaAnimal = coberturaDao.findFirstCobertura(animal);
				if ( primeiraCoberturaAnimal.getId() == cobertura.getId() ){
					//se a primeira cobertura do animal foi dentro do período avaliado, considera a entrada dele no rebanho
					//a partir da data da primeira cobertura
					if ( cobertura.getData().compareTo(dataInicio) >= 0 ){
						DVE += ChronoUnit.DAYS.between(DateUtil.asLocalDate(dataInicio), DateUtil.asLocalDate(cobertura.getData()));	
					}
					
				}
				
			}
			
			//calcula o período que o animal foi eliminado do rebanho
			if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.MORTO) ){
				MorteAnimal morte = morteAnimalDao.findByAnimalPeriodo(animal, dataInicio, dataFim);
				DVE += ChronoUnit.DAYS.between(DateUtil.asLocalDate(morte.getDataMorte()), DateUtil.asLocalDate(dataFim));
			}
			
			if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.VENDIDO) ){
				VendaAnimal venda = vendaAnimalDao.findByAnimalPeriodo(animal, dataInicio, dataFim);
				DVE += ChronoUnit.DAYS.between(DateUtil.asLocalDate(venda.getDataVenda()), DateUtil.asLocalDate(dataFim));
			}
			
		}
		
		N = animais.size();
		
		if ( N > 0 ){
			double index = (  (( (DVG / DG) * R ) + DVG)  /  ((N * DIAS * P) - DVE)  ) * 100;
			return BigDecimal.valueOf(index).setScale(0, RoundingMode.HALF_EVEN);			
		}
		
		return BigDecimal.ZERO;

	}
	
}
