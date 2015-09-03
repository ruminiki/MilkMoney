package br.com.milkmoney.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.ConfirmacaoPrenhes;
import br.com.milkmoney.model.Parametro;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.CoberturaValidation;

@Service
public class CoberturaService implements IService<Integer, Cobertura>{

	@Autowired private CoberturaDao dao;
	@Autowired private PartoService partoService;
	@Autowired private ParametroService parametroService;
	@Autowired private ConfirmacaoPrenhesService confirmacaoPrenhesService;

	@Override
	@Transactional
	public boolean save(Cobertura cobertura) {
		
		CoberturaValidation.validate(cobertura);
		CoberturaValidation.validaSituacaoAnimal(cobertura.getFemea());
		CoberturaValidation.validaFemeaSelecionada(cobertura, findByAnimal(cobertura.getFemea()), Integer.valueOf(parametroService.findBySigla(Parametro.IDADE_MINIMA_PARA_COBERTURA)));
		CoberturaValidation.validaSobreposicaoCoberturas(cobertura, dao.findLastCoberturaAnimal(cobertura.getFemea()));
		
		return dao.persist(cobertura);
	}
	
	@Transactional
	public void registrarParto(Cobertura cobertura) {
		try{
			
			cobertura.setSituacaoCobertura(SituacaoCobertura.PARIDA);
			dao.persist(cobertura);
			
		}catch(Exception e){
			cobertura.setParto(null);
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public void removerParto(Cobertura cobertura) {
		
		try{
			
			cobertura.setParto(null);
			//reconfigura situação cobertura
			ConfirmacaoPrenhes cp = confirmacaoPrenhesService.findLastByCobertura(cobertura);
			cobertura.setSituacaoCobertura(cp != null ? cp.getSituacaoCobertura() : SituacaoCobertura.NAO_CONFIRMADA);
			dao.persist(cobertura);

		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	public List<Cobertura> findByAnimal(Animal animal){
		return dao.findByAnimal(animal);
	}
	
	@Override
	@Transactional
	public boolean remove(Cobertura cobertura) {
		return dao.remove(cobertura);
	}
	
	@Transactional
	public void saveConfirmacaoPrenhes(Cobertura cobertura) {
		
		//ordena as confirmações pela data para recuperar a última situação
		
		Collections.sort(cobertura.getConfirmacoesPrenhes(), new Comparator<ConfirmacaoPrenhes>() {
		    @Override
		    public int compare(ConfirmacaoPrenhes cp1, ConfirmacaoPrenhes cp2) {
		        return cp1.getData().compareTo(cp2.getData());
		    }
		});
		
		//recupera o último registro de confirmação (a maior data)
		if ( cobertura.getConfirmacoesPrenhes() != null && cobertura.getConfirmacoesPrenhes().size() > 0 ){
			ConfirmacaoPrenhes cp = cobertura.getConfirmacoesPrenhes().get(cobertura.getConfirmacoesPrenhes().size()-1);
			cobertura.setSituacaoCobertura(cp.getSituacaoCobertura());
		}else{
			cobertura.setSituacaoCobertura(SituacaoCobertura.NAO_CONFIRMADA);
		}
		
		//salva em cascata as confirmações registradas dentro da mesma transação
		save(cobertura);
		
	}
	
	@Override
	public Cobertura findById(Integer id) {
		return dao.findById(Cobertura.class, id);
	}

	@Override
	public List<Cobertura> findAll() {
		return dao.findAll(Cobertura.class);
	}
	
	public ObservableList<Cobertura> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Cobertura.class));
	}
	
	@Override
	public ObservableList<Cobertura> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	@Override
	public void validate(Cobertura cobertura) {
		CoberturaValidation.validate(cobertura);
	}

	public Cobertura findCoberturaAtivaByAnimal(Animal animal){
		return dao.findCoberturaAtivaByAnimal(animal);
	}

	//------------FICHA ANIMAL
	public Date getDataUltimaCoberturaAnimal(Animal animal) {
		Cobertura cobertura = dao.findLastCoberturaAnimal(animal);
		return cobertura != null ? cobertura.getData() : null;
	}
	
	public int getNumeroServicosAtePrenhes(Animal animal) {
		
		List<Cobertura> coberturas = dao.findByAnimal(animal);
		int index = 0;
		
		for (Cobertura cobertura : coberturas){
			//conta quantas coberturas não tiveram parto, antes da última
			if ( !cobertura.getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ) index++;
			else break;
		}
		
		return index;
		
	}
	
	public BigDecimal getMediaIntervaloPrimeiraCoberturaAtePrenhes(Animal animal) {
		
		List<Cobertura> coberturas = dao.findByAnimal(animal);
		int index = 0;
		
		Date dataPrimeiraCobertura = null;
		int intervaloEntreCoberturas = 0;
		
		for (Cobertura cobertura : coberturas){
			
			//conta quantas coberturas não tiveram parto, antes da última
			if ( !cobertura.getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
				index++;
				if ( dataPrimeiraCobertura == null ){
					dataPrimeiraCobertura = cobertura.getData();
				}else{
					intervaloEntreCoberturas += (int) ChronoUnit.DAYS.between(DateUtil.asLocalDate(dataPrimeiraCobertura), DateUtil.asLocalDate(cobertura.getData()));
				}
			}
			else break;
		}
		
		if ( intervaloEntreCoberturas > 0 ){
			return BigDecimal.valueOf(intervaloEntreCoberturas).divide(BigDecimal.valueOf(index), 2, RoundingMode.HALF_EVEN);	
		}
		
		return BigDecimal.ZERO;
		
	}

	/*
	 * Se a última cobertura estava prenha - 
	 */
	public Date getProximoServico(Animal animal) {
		Cobertura cobertura = dao.findLastCoberturaAnimal(animal);
		
		if ( cobertura != null ){
			
			int periodoVoluntarioEspera = Integer.parseInt(parametroService.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA));
			
			if ( cobertura.getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
				return DateUtil.asDate(DateUtil.asLocalDate(cobertura.getParto().getData()).plusDays(periodoVoluntarioEspera));
			}
			
			if ( cobertura.getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) ){
				return DateUtil.asDate(DateUtil.asLocalDate(cobertura.getPrevisaoParto()).plusDays(periodoVoluntarioEspera));
			}
			
			if ( cobertura.getSituacaoCobertura().equals(SituacaoCobertura.VAZIA) ){
				ConfirmacaoPrenhes cp = confirmacaoPrenhesService.findLastByCobertura(cobertura);
				if ( cp != null )
					return DateUtil.asDate(DateUtil.asLocalDate(cp.getData()).plusDays(21));
			}
		}
		
		return null;
	}

	/*
	 * Para o cálculo do dias em aberto devemos considerar o número de dias do último parto até:
	 * A data da concepção das vacas gestantes
	 * A data da última cobertura das vacas ainda não confirmadas gestantes
	 * Ou a data em que o cálculo foi realizado.
	 */
	public int getDiasEmAberto(Animal animal) {
		Parto ultimoParto = partoService.findLastParto(animal);
		int diasEmAberto = 0;
		if ( ultimoParto != null ){
			
			Cobertura primeiraCoberturaAposParto = dao.findFirstAfterDate(animal, ultimoParto.getData());
			
			if ( primeiraCoberturaAposParto != null ){
				ConfirmacaoPrenhes confirmacao = confirmacaoPrenhesService.findLastByCobertura(primeiraCoberturaAposParto);
				//A data da concepção das vacas gestantes
				if ( confirmacao != null && confirmacao.getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) ){
					diasEmAberto = (int) ChronoUnit.DAYS.between(DateUtil.asLocalDate(ultimoParto.getData()), DateUtil.asLocalDate(confirmacao.getData()));
				}else{
					//A data da última cobertura das vacas ainda não confirmadas gestantes
					diasEmAberto = (int) ChronoUnit.DAYS.between(DateUtil.asLocalDate(ultimoParto.getData()), DateUtil.asLocalDate(primeiraCoberturaAposParto.getData()));
				}
				
			}else{
				//Ou a data em que o cálculo foi realizado.
				diasEmAberto = (int) ChronoUnit.DAYS.between(DateUtil.asLocalDate(ultimoParto.getData()), LocalDate.now());
			}
			
		}
		return diasEmAberto;
	}

}
