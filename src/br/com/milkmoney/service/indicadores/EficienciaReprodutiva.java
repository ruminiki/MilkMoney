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
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.model.VendaAnimal;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.util.DateUtil;

/**
 * M�todo Butendieck (1972)
 * 
 *
 * Somar os dias de gesta��o do rebanho no per�odo
 * 	1 - Encontrar as vacas que tiveram coberturas e/ou estavam prenhas no per�odo.
 *  2 - Somar os dias de ingresso (novilhas) e elimina��o (descartes)
 *  3 - N�o contabiliar como dias gestantes os abortos.
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
	
	double DVG  = 0;   //dias de gesta��o do rebanho
	double DVE  = 0;   //dias fora do rebanho (data de inicio at� a primeira cobertura) e/ou (data venda/morte at� a data fim)
	double DG   = 0;   //dura��o m�dia da gesta��o
	double R    = 85;  //per�odo seco ideal
	double N    = 0;   //n�mero total de vacas consideradas
	int    P    = 2;   //intervalo em anos
	double DIAS = 365; //constante 
	
	//per�odo avaliado (em anos)
	//Date dataInicio = new GregorianCalendar(2013,0,01).getTime();
	//Date dataFim = new GregorianCalendar(2014,11,31).getTime();
	
	Date dataInicio = DateUtil.asDate(LocalDate.now().minusYears(P));
	Date dataFim = new Date();
		
	@Override
	public BigDecimal getValue() {

		DG = DVG = DVE = 0;

		//seleciona os animais que comp�em o c�lculo
		//f�meas com partos ou coberturas no per�odo
		//a data de ingresso � contado a partir da primeira cobertura de novilhas
		//a data de elimina��o a partir da data de morte ou venda
		//abortos n�o s�o considerados nos dias gestantes
		
		//busca os animais que far�o parte do �ndice
		List<Animal> animais = animalDao.findAnimaisParaCalculoEficiencia(dataInicio, dataFim);
		
		for ( Animal animal : animais ){
			getDVGAndDVEAnimal(animal);
		}
		
		N = animais.size();
		DG = DG / animais.size();
		
		return calculaIndice();
		
	}
	
	public BigDecimal getValue(Animal animal) {

		DG = DVG = DVE = 0;
		N = 1;
		
		getDVGAndDVEAnimal(animal);
		
		return calculaIndice();
		
	}
	
	private void getDVGAndDVEAnimal(Animal animal){
		
		List<Cobertura> coberturas = coberturaDao.findCoberturasIniciadasOuComPartoNoPeriodo(animal, dataInicio, dataFim);
		
		for ( Cobertura cobertura : coberturas ){
			
			//abortos n�o s�o contabilizados nos dias gestantes
			if ( cobertura.getSituacaoCobertura().equals(SituacaoCobertura.ABORTADA) ){
				continue;
			}
				
			//se tiver parto, conta os dias de gesta��o at� a data do parto
			if ( cobertura.getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
				
				//verifica se a cobertura foi realizada dentro do per�odo
				//|---------------------------|(per�odo avaliado)
				//    IC|************|Parto (cobertura com parto)
				if ( cobertura.getData().compareTo(dataInicio) >= 0 &&  cobertura.getData().compareTo(dataFim) <= 0){
					DVG += ChronoUnit.DAYS.between(DateUtil.asLocalDate(cobertura.getData()), DateUtil.asLocalDate(cobertura.getParto().getData()));	
				}else{
					//      |---------------------------|(per�odo avaliado)
					//IC|---*********|Parto (cobertura com parto)
					DVG += ChronoUnit.DAYS.between(DateUtil.asLocalDate(dataInicio), DateUtil.asLocalDate(cobertura.getParto().getData()));						
				}
				
			}
			
			if ( cobertura.getSituacaoCobertura().matches(SituacaoCobertura.NAO_CONFIRMADA + "|" + SituacaoCobertura.PRENHA) ){
				//caso de cobertura que n�o foi registrado o parto e n�o foi confirmada vazia (j� passado do tempo) n�o considerar como gestante
				//       |---------------------------|(per�odo avaliado)
				// IC|---**************************** (cobertura n�o confirmada e sem parto)
				
				//se o parto j� deveria ter ocorrido, n�o considera pois o produtor esqueceu de marcar
				if ( cobertura.getPrevisaoParto().compareTo(dataFim) > 0 ){
					DVG += ChronoUnit.DAYS.between(DateUtil.asLocalDate(cobertura.getData()), DateUtil.asLocalDate(dataFim));						
				}
				
			}
			
			//se for a primeira cobertura do animal - considera a entrada dele no rebanho
			Cobertura primeiraCoberturaAnimal = coberturaDao.findFirstCobertura(animal);
			if ( primeiraCoberturaAnimal.getId() == cobertura.getId() ){
				//se a primeira cobertura do animal foi dentro do per�odo avaliado, considera a entrada dele no rebanho
				//a partir da data da primeira cobertura
				if ( cobertura.getData().compareTo(dataInicio) >= 0 ){
					DVE += ChronoUnit.DAYS.between(DateUtil.asLocalDate(dataInicio), DateUtil.asLocalDate(cobertura.getData()));	
				}
				
			}
			
		}
		
		//calcula o per�odo que o animal foi eliminado do rebanho
		MorteAnimal morte = morteAnimalDao.findByAnimalPeriodo(animal, dataInicio, dataFim);
		if ( morte != null )
			DVE += ChronoUnit.DAYS.between(DateUtil.asLocalDate(morte.getDataMorte()), DateUtil.asLocalDate(dataFim));
		
		VendaAnimal venda = vendaAnimalDao.findByAnimalPeriodo(animal, dataInicio, dataFim);
		if ( venda != null )
			DVE += ChronoUnit.DAYS.between(DateUtil.asLocalDate(venda.getDataVenda()), DateUtil.asLocalDate(dataFim));
		
		//adiciona a dura��o da gesta��o para fazer a m�dia das ra�as do rebanho
		DG += animal.getRaca().getDuracaoGestacao() > 0 ? animal.getRaca().getDuracaoGestacao() : 280;
	}
	
	private BigDecimal calculaIndice(){
		if ( N > 0 ){
			double index = (  (( (DVG / DG) * R ) + DVG)  /  ((N * DIAS * P) - DVE)  ) * 100;
			try{
				return BigDecimal.valueOf(index).setScale(0, RoundingMode.HALF_EVEN);
			}catch(Exception e){
				return BigDecimal.ZERO;  
			}
		}
		return BigDecimal.ZERO;
	}
	
	
}
