package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.dao.MorteAnimalDao;
import br.com.milkmoney.dao.PartoDao;
import br.com.milkmoney.dao.VendaAnimalDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.Limit;
import br.com.milkmoney.model.MorteAnimal;
import br.com.milkmoney.model.Parametro;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.model.VendaAnimal;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.ParametroService;
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
	@Autowired private ParametroService parametroService;
	
	private double DVG                    = 0;   //dias de gestação do rebanho
	private double DVE                    = 0;   //dias fora do rebanho (data de inicio até a primeira cobertura) e/ou (data venda/morte até a data fim)
	private double DG                     = 0;   //duração média da gestação
	private double R                      = 85;  //período seco ideal
	private double N                      = 0;   //número total de vacas consideradas
	private int    P                      = 2;   //período avaliado (em anos)
	private double DIAS                   = 365; //constante 
	private int    IDADE_MINIMA_COBERTURA = 18;  //idade mínima para início do período de serviço
	private Date   data                   = new Date();
	
	@PostConstruct
	public void setParameters(){
		IDADE_MINIMA_COBERTURA = Integer.parseInt(parametroService.findBySigla(Parametro.IDADE_MINIMA_PARA_COBERTURA));
		P                      = Integer.parseInt(parametroService.findBySigla(Parametro.PERIODO_AVALIACAO_EFICIENCIA_REPRODUTIVA));
	}
	
	@Override
	public BigDecimal getValue(Date data) {

		DG = DVG = DVE = 0;
		
		Date dataInicio = DateUtil.asDate(DateUtil.asLocalDate(data).minusYears(P));
		Date dataFim = data;

		//seleciona os animais que compõem o cálculo
		//fêmeas com partos ou coberturas no período
		//a data de ingresso é contado a partir da primeira cobertura de novilhas
		//a data de eliminação a partir da data de morte ou venda
		//abortos não são considerados nos dias gestantes
		
		//busca os animais que farão parte do índice
		List<Animal> animais = animalDao.findAnimaisParaCalculoEficiencia(dataInicio, dataFim, Limit.UNLIMITED);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		//double DVGANIMAL, DVEANIMAL;
		//DVGANIMAL=DVEANIMAL=0;
		
		System.out.println(sdf.format(data) + "\n");
		for ( Animal animal : animais ){
			
			//DVGANIMAL = DVG;
			//DVEANIMAL = DVE;
			
			getDVGAndDVEAnimal(animal, dataInicio, dataFim);
			//DVGANIMAL = DVG - DVGANIMAL;
			//DVEANIMAL = DVE - DVEANIMAL;
			
			//System.out.println(animal.getNumeroNome() + ";" + sdf.format(dataInicio) + ";" + sdf.format(dataFim) + ";" + DVEANIMAL + ";" + DVGANIMAL );
			
			//getValue(animal);
		}
		
		N = animais.size();
		DG = DG / animais.size();
		
		return calculaIndice();
		
	}
	
	public BigDecimal getValue(Animal animal) {

		N = 1;
		DG = DVG = DVE = 0;
		
		double DVGANIMAL, DVEANIMAL;
		DVGANIMAL=DVEANIMAL=0;
		
		//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Date dataInicio = DateUtil.asDate(DateUtil.asLocalDate(data).minusYears(P));
		Date dataFim = data;
		
		DVGANIMAL = DVG;
		DVEANIMAL = DVE;
		
		getDVGAndDVEAnimal(animal, dataInicio, dataFim);
		
		DVGANIMAL = DVG - DVGANIMAL;
		DVEANIMAL = DVE - DVEANIMAL;
		
		//System.out.println(animal.getNumeroNome() + ";" + sdf.format(dataInicio) + ";" + sdf.format(dataFim) + ";" + DVEANIMAL + ";" + DVGANIMAL );
		
		BigDecimal ie = calculaIndice();
		
		//System.out.println(animal.getNumeroNome() + ";" + ie.toString());
		
		return ie;
		
	}
	
	private void getDVGAndDVEAnimal(Animal animal, Date dataInicio, Date dataFim){
		
		boolean calculouDVEAnimal = false;
		
		List<Cobertura> coberturas = coberturaDao.findCoberturasIniciadasOuComPartoNoPeriodo(animal, dataInicio, dataFim);
		
		for ( Cobertura cobertura : coberturas ){
			
			//abortos não são contabilizados nos dias gestantes
			if ( cobertura.getSituacaoCobertura().equals(SituacaoCobertura.ABORTADA) ){
				continue;
			}
				
			//se tiver parto, conta os dias de gestação até a data do parto
			if ( cobertura.getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
				
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
				
				//se a previsão para o parto for maior que a data atual, conta até a data atual
				if ( cobertura.getPrevisaoParto().compareTo(dataFim) > 0 ){
					DVG += ChronoUnit.DAYS.between(DateUtil.asLocalDate(cobertura.getData()), DateUtil.asLocalDate(dataFim));						
				}
				
				//se o parto já deveria ter ocorrido, considera a gestação até a data prevista para o parto
				if ( cobertura.getPrevisaoParto().compareTo(dataFim) <= 0 ){
					DVG += ChronoUnit.DAYS.between(DateUtil.asLocalDate(cobertura.getData()), DateUtil.asLocalDate(cobertura.getPrevisaoParto()));						
				}
				
			}
			
			//considera a entrada do animal no rebanho o início do período de serviço
			//ou seja a idade mínima para inseminação
			
			//verifica se no início do período de avaliação a fêmea já tinha idade suficiente para inseminação
			//se sim, não conta DVE. Senão, calcula o DVE entre o inicio do período e a primeira cobertura ou idade mínima para cobertura.
			long idadeFemeaInicioPeriodoAvaliacao = ChronoUnit.MONTHS.between(DateUtil.asLocalDate(animal.getDataNascimento()), DateUtil.asLocalDate(dataInicio));
			if ( !calculouDVEAnimal && Math.abs(idadeFemeaInicioPeriodoAvaliacao) < IDADE_MINIMA_COBERTURA ){
				//busca a primeira cobertura do animal
				Cobertura primeiraCoberturaAnimal = coberturaDao.findFirstCobertura(animal);
				
				//calcula a data em que o animal iria atingir o periodo de serviço
				LocalDate dataAnimalAtingiuIdadeMinimaCobertura;
				//se o animal nasceu depois do início do periodo
				if ( idadeFemeaInicioPeriodoAvaliacao < IDADE_MINIMA_COBERTURA ){
					dataAnimalAtingiuIdadeMinimaCobertura = DateUtil.asLocalDate(animal.getDataNascimento()).plusMonths(IDADE_MINIMA_COBERTURA);	
				}else{
					dataAnimalAtingiuIdadeMinimaCobertura = DateUtil.asLocalDate(animal.getDataNascimento()).plusMonths(IDADE_MINIMA_COBERTURA - idadeFemeaInicioPeriodoAvaliacao); 
				}
			
				//se existe cobertura antes da idade mínima do animal, considera a data da cobertura
				//se a primeira cobertura do animal foi dentro do período avaliado, considera a entrada dele no rebanho
				//a partir da data da primeira cobertura
				if ( primeiraCoberturaAnimal.getData().compareTo(DateUtil.asDate(dataAnimalAtingiuIdadeMinimaCobertura)) <= 0 &&
						primeiraCoberturaAnimal.getData().compareTo(dataInicio) >= 0){
					DVE += ChronoUnit.DAYS.between(DateUtil.asLocalDate(dataInicio), DateUtil.asLocalDate(cobertura.getData()));	
				}else{
					DVE += ChronoUnit.DAYS.between(DateUtil.asLocalDate(dataInicio), dataAnimalAtingiuIdadeMinimaCobertura);	
				}
				
				calculouDVEAnimal = true;
				
			}

		}
		
		//calcula o período que o animal foi eliminado do rebanho
		MorteAnimal morte = morteAnimalDao.findByAnimalPeriodo(animal, dataInicio, dataFim);
		if ( morte != null )
			DVE += ChronoUnit.DAYS.between(DateUtil.asLocalDate(morte.getDataMorte()), DateUtil.asLocalDate(dataFim));
		
		VendaAnimal venda = vendaAnimalDao.findByAnimalPeriodo(animal, dataInicio, dataFim);
		if ( venda != null )
			DVE += ChronoUnit.DAYS.between(DateUtil.asLocalDate(venda.getDataVenda()), DateUtil.asLocalDate(dataFim));
		
		//adiciona a duração da gestação para fazer a média das raças do rebanho
		DG += animal.getRaca().getDuracaoGestacao() > 0 ? animal.getRaca().getDuracaoGestacao() : 280;
	}
	
	private BigDecimal calculaIndice(){
		if ( N > 0 ){
			
			double index = (  (( (DVG / DG) * R ) + DVG)  /  ((N * DIAS * P) - DVE)  ) * 100;
			try{
				BigDecimal indice = BigDecimal.valueOf(index).setScale(0, RoundingMode.HALF_EVEN);
				//padroniza para retornar no máximo 100%
				return indice;
				//return indice.compareTo(BigDecimal.valueOf(100)) > 0 ? BigDecimal.valueOf(100) : indice;
			}catch(Exception e){
				return BigDecimal.ZERO;  
			}
			
		}
		return BigDecimal.ZERO;
	}
	
	
}
