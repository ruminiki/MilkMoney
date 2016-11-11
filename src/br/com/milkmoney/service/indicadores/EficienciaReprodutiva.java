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
	@Autowired private ParametroService parametroService;
	
	private double DVG                    = 0;   //dias de gesta��o do rebanho
	private double DVE                    = 0;   //dias fora do rebanho (data de inicio at� a primeira cobertura) e/ou (data venda/morte at� a data fim)
	private double DG                     = 0;   //dura��o m�dia da gesta��o
	private double R                      = 85;  //per�odo seco ideal
	private double N                      = 0;   //n�mero total de vacas consideradas
	private int    P                      = 2;   //per�odo avaliado (em anos)
	private double DIAS                   = 365; //constante 
	private int    IDADE_MINIMA_COBERTURA = 18;  //idade m�nima para in�cio do per�odo de servi�o
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

		//seleciona os animais que comp�em o c�lculo
		//f�meas com partos ou coberturas no per�odo
		//a data de ingresso � contado a partir da primeira cobertura de novilhas
		//a data de elimina��o a partir da data de morte ou venda
		//abortos n�o s�o considerados nos dias gestantes
		
		//busca os animais que far�o parte do �ndice
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
				
				//se a previs�o para o parto for maior que a data atual, conta at� a data atual
				if ( cobertura.getPrevisaoParto().compareTo(dataFim) > 0 ){
					DVG += ChronoUnit.DAYS.between(DateUtil.asLocalDate(cobertura.getData()), DateUtil.asLocalDate(dataFim));						
				}
				
				//se o parto j� deveria ter ocorrido, considera a gesta��o at� a data prevista para o parto
				if ( cobertura.getPrevisaoParto().compareTo(dataFim) <= 0 ){
					DVG += ChronoUnit.DAYS.between(DateUtil.asLocalDate(cobertura.getData()), DateUtil.asLocalDate(cobertura.getPrevisaoParto()));						
				}
				
			}
			
			//considera a entrada do animal no rebanho o in�cio do per�odo de servi�o
			//ou seja a idade m�nima para insemina��o
			
			//verifica se no in�cio do per�odo de avalia��o a f�mea j� tinha idade suficiente para insemina��o
			//se sim, n�o conta DVE. Sen�o, calcula o DVE entre o inicio do per�odo e a primeira cobertura ou idade m�nima para cobertura.
			long idadeFemeaInicioPeriodoAvaliacao = ChronoUnit.MONTHS.between(DateUtil.asLocalDate(animal.getDataNascimento()), DateUtil.asLocalDate(dataInicio));
			if ( !calculouDVEAnimal && Math.abs(idadeFemeaInicioPeriodoAvaliacao) < IDADE_MINIMA_COBERTURA ){
				//busca a primeira cobertura do animal
				Cobertura primeiraCoberturaAnimal = coberturaDao.findFirstCobertura(animal);
				
				//calcula a data em que o animal iria atingir o periodo de servi�o
				LocalDate dataAnimalAtingiuIdadeMinimaCobertura;
				//se o animal nasceu depois do in�cio do periodo
				if ( idadeFemeaInicioPeriodoAvaliacao < IDADE_MINIMA_COBERTURA ){
					dataAnimalAtingiuIdadeMinimaCobertura = DateUtil.asLocalDate(animal.getDataNascimento()).plusMonths(IDADE_MINIMA_COBERTURA);	
				}else{
					dataAnimalAtingiuIdadeMinimaCobertura = DateUtil.asLocalDate(animal.getDataNascimento()).plusMonths(IDADE_MINIMA_COBERTURA - idadeFemeaInicioPeriodoAvaliacao); 
				}
			
				//se existe cobertura antes da idade m�nima do animal, considera a data da cobertura
				//se a primeira cobertura do animal foi dentro do per�odo avaliado, considera a entrada dele no rebanho
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
				BigDecimal indice = BigDecimal.valueOf(index).setScale(0, RoundingMode.HALF_EVEN);
				//padroniza para retornar no m�ximo 100%
				return indice;
				//return indice.compareTo(BigDecimal.valueOf(100)) > 0 ? BigDecimal.valueOf(100) : indice;
			}catch(Exception e){
				return BigDecimal.ZERO;  
			}
			
		}
		return BigDecimal.ZERO;
	}
	
	
}
