package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.CoberturaDao;
import br.com.milksys.exception.ValidationException;
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
		
		return dao.persist(entity);
	}
	
	public void registrarPrimeiroToque(Cobertura entity){
		
		if ( entity.getParto() != null ){
			throw new ValidationException(Validator.REGRA_NEGOCIO, "A cobertura já tem parto registrado, não sendo possível executar essa operação.");
		}
		
		CoberturaValidation.validateRegistroPrimeiroToque(entity);
		if ( !entity.getSituacaoCobertura().equals(SituacaoCobertura.REPETIDA) )
			entity.setSituacaoCobertura(entity.getResultadoPrimeiroToque());
		
		dao.persist(entity);
	}
	

	public void removerRegistroPrimeiroToque(Cobertura entity) {
		
		if ( entity.getParto() != null ){
			throw new ValidationException(Validator.REGRA_NEGOCIO, "A cobertura já tem parto registrado, não sendo possível executar essa operação.");
		}
		
		entity.setDataPrimeiroToque(null);
		entity.setObservacaoPrimeiroToque(null);
		entity.setResultadoPrimeiroToque(null);
		
		configureSituacaoCobertura(entity);
		
		//verifica se existem outras coberturas para o animal com situação PRENHA, ou INDEFINIDA
		CoberturaValidation.validaSituacoesCoberturasDoAnimal(entity);
		
		dao.persist(entity);
		
	}
	
	
	public void registrarReconfirmacao(Cobertura entity){
		
		if ( entity.getParto() != null ){
			throw new ValidationException(Validator.REGRA_NEGOCIO, "A cobertura já tem parto registrado, não sendo possível executar essa operação.");
		}
		
		CoberturaValidation.validateRegistroReconfirmacao(entity);
		if ( !entity.getSituacaoCobertura().equals(SituacaoCobertura.REPETIDA) )
			entity.setSituacaoCobertura(entity.getResultadoReconfirmacao());
		dao.persist(entity);
	}
	
	public void removerRegistroReconfirmacao(Cobertura entity) {
		
		if ( entity.getParto() != null ){
			throw new ValidationException(Validator.REGRA_NEGOCIO, "A cobertura já tem parto registrado, não sendo possível executar essa operação.");
		}
		
		entity.setDataReconfirmacao(null);
		entity.setObservacaoReconfirmacao(null);
		entity.setResultadoReconfirmacao(null);
		
		configureSituacaoCobertura(entity);
		
		//verifica se existem outras coberturas para o animal com situação PRENHA, ou INDEFINIDA
		CoberturaValidation.validaSituacoesCoberturasDoAnimal(entity);
		
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
		CoberturaValidation.validaSituacoesCoberturasDoAnimal(entity);
		
		dao.persist(entity);
		
	}
	
	private void configureSituacaoCobertura(Cobertura entity){
		
		if ( entity.getDataRepeticaoCio() != null ){
			entity.setSituacaoCobertura(SituacaoCobertura.REPETIDA);
			return;
		}
		
		if ( entity.getDataReconfirmacao() != null ){
			entity.setSituacaoCobertura(entity.getResultadoReconfirmacao());
			return;
		}
		
		if ( entity.getDataPrimeiroToque() != null ){
			entity.setSituacaoCobertura(entity.getResultadoPrimeiroToque());
			return;
		}
		
		entity.setSituacaoCobertura(SituacaoCobertura.INDEFINIDA);
		
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
		dao.persist(entity);
		
	}

	public void removerParto(Cobertura entity) {
		configureSituacaoCobertura(entity);
		dao.removerParto(entity);
	}
	
}
