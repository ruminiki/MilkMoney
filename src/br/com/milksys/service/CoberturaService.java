package br.com.milksys.service;

import java.util.Collections;
import java.util.Comparator;
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
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.validation.CoberturaValidation;

@Service
public class CoberturaService implements IService<Integer, Cobertura>{

	@Autowired private CoberturaDao dao;
	@Autowired private SemenService semenService;
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
		
		CoberturaValidation.validaSobreposicaoCoberturas(cobertura, dao.findLastCoberturaByAnimal(cobertura.getFemea()));
		
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

}
