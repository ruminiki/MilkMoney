package br.com.milksys.service;

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

import br.com.milksys.dao.CoberturaDao;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.ConfirmacaoPrenhez;
import br.com.milksys.model.Parametro;
import br.com.milksys.model.Parto;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.util.DateUtil;
import br.com.milksys.validation.CoberturaValidation;

@Service
public class CoberturaService implements IService<Integer, Cobertura>{

	@Autowired private CoberturaDao dao;
	@Autowired private SemenService semenService;
	@Autowired private PartoService partoService;
	@Autowired private ParametroService parametroService;
	@Autowired private ConfirmacaoPrenhezService confirmacaoPrenhezService;

	@Override
	@Transactional
	public boolean save(Cobertura cobertura) {
		
		CoberturaValidation.validate(cobertura);
		CoberturaValidation.validaSituacaoAnimal(cobertura.getFemea());
		CoberturaValidation.validaFemeaSelecionada(cobertura, findByAnimal(cobertura.getFemea()), Integer.valueOf(parametroService.findBySigla(Parametro.IDADE_MINIMA_PARA_COBERTURA)));
		if ( cobertura.getSemen() != null ){
			boolean aumentouQuantidadeDosesUtilizadas = cobertura.getQuantidadeDosesUtilizadas() > dao.findQuantidadeDosesSemenUtilizadasNaCobertura(cobertura);
			//recarrega o registro do semen para recalcular as doses disponíveis
			cobertura.setSemen(semenService.findById(cobertura.getSemen().getId()));
			CoberturaValidation.validaEnseminacaoArtificial(cobertura, cobertura.getSemen().getQuantidadeDisponivel(), aumentouQuantidadeDosesUtilizadas);	
		}
		
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
			ConfirmacaoPrenhez cp = confirmacaoPrenhezService.findLastByCobertura(cobertura);
			cobertura.setSituacaoCobertura(cp != null ? cp.getSituacaoCobertura() : SituacaoCobertura.INDEFINIDA);
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
	public void saveConfirmacaoPrenhez(Cobertura cobertura) {
		
		//ordena as confirmações pela data para recuperar a última situação
		
		Collections.sort(cobertura.getConfirmacoesPrenhez(), new Comparator<ConfirmacaoPrenhez>() {
		    @Override
		    public int compare(ConfirmacaoPrenhez cp1, ConfirmacaoPrenhez cp2) {
		        return cp1.getData().compareTo(cp2.getData());
		    }
		});
		
		//recupera o último registro de confirmação (a maior data)
		if ( cobertura.getConfirmacoesPrenhez() != null && cobertura.getConfirmacoesPrenhez().size() > 0 ){
			ConfirmacaoPrenhez cp = cobertura.getConfirmacoesPrenhez().get(cobertura.getConfirmacoesPrenhez().size()-1);
			cobertura.setSituacaoCobertura(cp.getSituacaoCobertura());
		}else{
			cobertura.setSituacaoCobertura(SituacaoCobertura.INDEFINIDA);
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
	
	public int getNumeroServicosAtePrenhez(Animal animal) {
		
		List<Cobertura> coberturas = dao.findByAnimal(animal);
		int index = 0;
		
		for (Cobertura cobertura : coberturas){
			//conta quantas coberturas não tiveram parto, antes da última
			if ( !cobertura.getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ) index++;
			else break;
		}
		
		return index;
		
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
				ConfirmacaoPrenhez cp = confirmacaoPrenhezService.findLastByCobertura(cobertura);
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
				ConfirmacaoPrenhez confirmacao = confirmacaoPrenhezService.findLastByCobertura(primeiraCoberturaAposParto);
				//A data da concepção das vacas gestantes
				if ( confirmacao.getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) ){
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
