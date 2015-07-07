package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.CoberturaDao;
import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.validation.CoberturaValidation;
import br.com.milksys.validation.Validator;

@Service
public class CoberturaService implements IService<Integer, Cobertura>{

	@Autowired private CoberturaDao dao;

	@Override
	public boolean save(Cobertura entity) {
		
		if ( entity.getParto() != null ){
			throw new ValidationException(Validator.REGRA_NEGOCIO, "A cobertura já tem parto registrado, não sendo possível executar essa operação.");
		}
		
		CoberturaValidation.validate(entity);
		CoberturaValidation.validaCoberturasAnimal(entity, findByAnimal(entity.getFemea()));
		CoberturaValidation.validaEnseminacaoArtificial(entity, (entity.getQuantidadeDosesUtilizadas() > dao.findQuantidadeDosesSemenUtilizadasNaCobertura(entity)));
		
		return dao.persist(entity);
	}
	
	public void registrarPrimeiroToque(Cobertura entity){
		
		if ( entity.getParto() != null ){
			throw new ValidationException(Validator.REGRA_NEGOCIO, "A cobertura já tem parto registrado, não sendo possível executar essa operação.");
		}
		
		CoberturaValidation.validateRegistroPrimeiroToque(entity);
		if ( !entity.getSituacaoCobertura().equals(SituacaoCobertura.REPETIDA) )
			entity.setSituacaoCobertura(entity.getSituacaoPrimeiroToque());
		
		dao.persist(entity);
	}
	

	public void removerRegistroPrimeiroToque(Cobertura entity) {
		
		if ( entity.getParto() != null ){
			throw new ValidationException(Validator.REGRA_NEGOCIO, "A cobertura já tem parto registrado, não sendo possível executar essa operação.");
		}
		
		entity.setDataPrimeiroToque(null);
		entity.setObservacaoPrimeiroToque(null);
		entity.setSituacaoPrimeiroToque(null);
		
		configureSituacaoCobertura(entity);
		
		//verifica se existem outras coberturas para o animal com situação PRENHA, ou INDEFINIDA
		CoberturaValidation.validaSituacoesCoberturasDoAnimal(entity, findByAnimal(entity.getFemea()));
		
		dao.persist(entity);
		
	}
	
	
	public void registrarReconfirmacao(Cobertura entity){
		
		if ( entity.getParto() != null ){
			throw new ValidationException(Validator.REGRA_NEGOCIO, "A cobertura já tem parto registrado, não sendo possível executar essa operação.");
		}
		
		CoberturaValidation.validateRegistroReconfirmacao(entity);
		if ( !entity.getSituacaoCobertura().equals(SituacaoCobertura.REPETIDA) )
			entity.setSituacaoCobertura(entity.getSituacaoReconfirmacao());
		dao.persist(entity);
	}
	
	public void removerRegistroReconfirmacao(Cobertura entity) {
		
		if ( entity.getParto() != null ){
			throw new ValidationException(Validator.REGRA_NEGOCIO, "A cobertura já tem parto registrado, não sendo possível executar essa operação.");
		}
		
		entity.setDataReconfirmacao(null);
		entity.setObservacaoReconfirmacao(null);
		entity.setSituacaoReconfirmacao(null);
		
		configureSituacaoCobertura(entity);
		
		//verifica se existem outras coberturas para o animal com situação PRENHA, ou INDEFINIDA
		CoberturaValidation.validaSituacoesCoberturasDoAnimal(entity, findByAnimal(entity.getFemea()));
		
		dao.persist(entity);
		
	}

	public void registrarRepeticaoCio(Cobertura entity){
		
		if ( entity.getParto() != null ){
			throw new ValidationException(Validator.REGRA_NEGOCIO, "A cobertura já tem parto registrado, não sendo possível executar essa operação.");
		}
		
		entity.setSituacaoCobertura(SituacaoCobertura.REPETIDA);
		CoberturaValidation.validateRegistroRepeticaoCio(entity);
		dao.persist(entity);
	}
	
	public void removerRegistroRepeticaoCio(Cobertura entity) {
		
		if ( entity.getParto() != null ){
			throw new ValidationException(Validator.REGRA_NEGOCIO, "A cobertura já tem parto registrado, não sendo possível executar essa operação.");
		}
		
		entity.setDataRepeticaoCio(null);
		entity.setObservacaoRepeticaoCio(null);
		
		configureSituacaoCobertura(entity);
		
		//verifica se existem outras coberturas para o animal com situação PRENHA, ou INDEFINIDA
		CoberturaValidation.validaSituacoesCoberturasDoAnimal(entity, findByAnimal(entity.getFemea()));
		
		dao.persist(entity);
		
	}
	
	private void configureSituacaoCobertura(Cobertura entity){
		
		if ( entity.getDataRepeticaoCio() != null ){
			entity.setSituacaoCobertura(SituacaoCobertura.REPETIDA);
			return;
		}
		
		if ( entity.getDataReconfirmacao() != null ){
			entity.setSituacaoCobertura(entity.getSituacaoReconfirmacao());
			return;
		}
		
		if ( entity.getDataPrimeiroToque() != null ){
			entity.setSituacaoCobertura(entity.getSituacaoPrimeiroToque());
			return;
		}
		
		entity.setSituacaoCobertura(SituacaoCobertura.INDEFINIDA);
		
	}
	
	public List<Cobertura> findByAnimal(Animal animal){
		return dao.findByAnimal(animal);
	}
	
	
	@Override
	public boolean remove(Cobertura entity) {
		return dao.remove(entity);
	}

	@Override
	public Cobertura findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<Cobertura> findAll() {
		return dao.findAll(Cobertura.class);
	}
	
	public ObservableList<Cobertura> findAllAsObservableList() {
		ObservableList<Cobertura> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(Cobertura.class));
		return list;
	}

	public void removeServicoFromCobertura(Cobertura cobertura) {
		dao.removeServicoFromCobertura(cobertura);
	}

	@Override
	public void validate(Cobertura entity) {
		CoberturaValidation.validate(entity);
	}

	public void registrarParto(Cobertura entity) {
		entity.setSituacaoCobertura(SituacaoCobertura.PARIDA);
		try{
			dao.persist(entity);
		}catch(Exception e){
			configureSituacaoCobertura(entity);
			entity.setParto(null);
			throw new RuntimeException(e);
		}
	}

	public void removerParto(Cobertura entity) {
		
		configureSituacaoCobertura(entity);
		try{
			dao.removerParto(entity);
		}catch(Exception e){
			entity.setSituacaoCobertura(SituacaoCobertura.PARIDA);
			throw new RuntimeException(e);
		}
		
	}
	
}
