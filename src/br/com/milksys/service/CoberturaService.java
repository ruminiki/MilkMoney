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
	public boolean save(Cobertura entity) {
		
		CoberturaValidation.validate(entity);
		CoberturaValidation.validaSituacaoAnimal(entity.getFemea());
		CoberturaValidation.validaFemeaSelecionada(entity, findByAnimal(entity.getFemea()));
		if ( entity.getSemen() != null ){
			boolean aumentouQuantidadeDosesUtilizadas = entity.getQuantidadeDosesUtilizadas() > dao.findQuantidadeDosesSemenUtilizadasNaCobertura(entity);
			//recarrega o registro do semen para recalcular as doses disponíveis
			entity.setSemen(semenService.findById(entity.getSemen().getId()));
			CoberturaValidation.validaEnseminacaoArtificial(entity, entity.getSemen().getQuantidadeDisponivel(), aumentouQuantidadeDosesUtilizadas);	
		}
		
		CoberturaValidation.validaSobreposicaoCoberturas(entity, dao.findLastCoberturaByAnimal(entity.getFemea()));
		
		return dao.persist(entity);
	}
	
	@Transactional
	public void registrarParto(Cobertura entity) {
		configuraDataPrevisaoPartoEEncerramentoLactacao(entity);
		try{
			
			dao.persist(entity);
			
		}catch(Exception e){
			entity.setParto(null);
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public void removerParto(Cobertura entity) {
		
		try{
			
			entity.setParto(null);
			dao.persist(entity);

		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	private void configuraDataPrevisaoPartoEEncerramentoLactacao(Cobertura entity){
		
		if ( entity.getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) ||
				entity.getSituacaoCobertura().equals(SituacaoCobertura.INDEFINIDA) ){
			entity.setPrevisaoParto(DateUtil.asDate(DateUtil.asLocalDate(entity.getData()).plusDays(282)));
			entity.setPrevisaoEncerramentoLactacao(DateUtil.asDate(DateUtil.asLocalDate(entity.getData()).plusMonths(7)));
		}else{
			entity.setPrevisaoParto(null);
			entity.setPrevisaoEncerramentoLactacao(null);
		}
		
	}
	
	public List<Cobertura> findByAnimal(Animal animal){
		return dao.findByAnimal(animal);
	}
	
	@Override
	@Transactional
	public boolean remove(Cobertura entity) {
		return dao.remove(entity);
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

	@Transactional
	public void removeServicoFromCobertura(Cobertura cobertura) {
		cobertura.setServico(null);
		dao.persist(cobertura);
	}

	@Override
	public void validate(Cobertura entity) {
		CoberturaValidation.validate(entity);
	}

	public Cobertura findCoberturaAtivaByAnimal(Animal animal){
		return dao.findCoberturaAtivaByAnimal(animal);
	}
	
}
