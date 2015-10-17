package br.com.milkmoney.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
			cobertura.setSituacaoCobertura(cobertura.getSituacaoConfirmacaoPrenhes());
			dao.persist(cobertura);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	@Transactional
	public void desfazerConfirmacaoPrenhes(Cobertura cobertura) {
		
		try{
			cobertura.setDataConfirmacaoPrenhes(null);
			cobertura.setMetodoConfirmacaoPrenhes(null);
			cobertura.setObservacaoConfirmacaoPrenhes(null);
			cobertura.setSituacaoConfirmacaoPrenhes(null);
			cobertura.setSituacaoCobertura(SituacaoCobertura.NAO_CONFIRMADA);
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

	public ObservableList<Cobertura> defaultSearch(String param, Animal animal) {
		return FXCollections.observableArrayList(dao.defaultSearch(param, animal));
	}
	
	@Override
	public void validate(Cobertura cobertura) {
		CoberturaValidation.validate(cobertura);
	}
	
	public Cobertura findLastCoberturaAnimal(Animal animal){
		return dao.findLastCoberturaAnimal(animal);
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

		Parto parto = partoService.findLastParto(animal);
		if ( parto != null ){
			List<Cobertura> coberturas = dao.findAllAfterDate(animal, parto.getData());
			
			//se o animal tem coberturas após o parto, conta elas, senão tenta buscar do parto anterior
			if ( coberturas.size() > 0 ){
				return coberturas.size();
			}
			
		}
		
		List<Cobertura> coberturas = dao.findByAnimal(animal);
		int index = 0;
		
		for (Cobertura cobertura : coberturas){
			//conta quantas coberturas não tiveram parto, antes da última
			index++;
			if ( cobertura.getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ) break;
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
				if ( cobertura.getDataConfirmacaoPrenhes() != null )
					return DateUtil.asDate(DateUtil.asLocalDate(cobertura.getDataConfirmacaoPrenhes()).plusDays(21));
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
				//A data da concepção das vacas gestantes
				if ( primeiraCoberturaAposParto.getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) ){
					diasEmAberto = (int) ChronoUnit.DAYS.between(DateUtil.asLocalDate(ultimoParto.getData()), DateUtil.asLocalDate(primeiraCoberturaAposParto.getDataConfirmacaoPrenhes()));
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
