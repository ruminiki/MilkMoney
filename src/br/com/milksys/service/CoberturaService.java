package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milksys.dao.CoberturaDao;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.util.DateUtil;
import br.com.milksys.validation.CoberturaValidation;

@Service
public class CoberturaService implements IService<Integer, Cobertura>{

	@Autowired private CoberturaDao dao;
	@Autowired private PartoService partoService;
	@Autowired private SemenService semenService;

	@Override
	@Transactional
	public boolean save(Cobertura cobertura) {
		
		CoberturaValidation.validate(cobertura);
		CoberturaValidation.validaSituacaoAnimal(cobertura.getFemea());
		CoberturaValidation.validaFemeaSelecionada(cobertura, findByAnimal(cobertura.getFemea()));
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
			
			configuraDataPrevisaoPartoELactacao(cobertura);
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
			configuraDataPrevisaoPartoELactacao(cobertura);
			dao.persist(cobertura);

		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	private void configuraDataPrevisaoPartoELactacao(Cobertura cobertura){
		
		if ( cobertura.getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) ||
				cobertura.getSituacaoCobertura().equals(SituacaoCobertura.INDEFINIDA) ){
			cobertura.setPrevisaoParto(DateUtil.asDate(DateUtil.asLocalDate(cobertura.getData()).plusDays(282)));
			cobertura.setPrevisaoEncerramentoLactacao(DateUtil.asDate(DateUtil.asLocalDate(cobertura.getData()).plusMonths(7)));
		}else{
			cobertura.setPrevisaoParto(null);
			cobertura.setPrevisaoEncerramentoLactacao(null);
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
		return FXCollections.observableArrayList(dao.findAllByNumeroNomeAnimal(param));
	}

	@Override
	public void validate(Cobertura cobertura) {
		CoberturaValidation.validate(cobertura);
	}

	public Cobertura findCoberturaAtivaByAnimal(Animal animal){
		return dao.findCoberturaAtivaByAnimal(animal);
	}
	
}
