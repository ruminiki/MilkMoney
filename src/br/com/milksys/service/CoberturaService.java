package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.CoberturaDao;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.validation.CoberturaValidation;

@Service
public class CoberturaService implements IService<Integer, Cobertura>{

	@Autowired private CoberturaDao dao;

	@Override
	public boolean save(Cobertura entity) {
		CoberturaValidation.validate(entity);
		return dao.persist(entity);
	}
	
	public void registrarPrimeiroToque(Cobertura entity){
		CoberturaValidation.validateRegistroPrimeiroToque(entity);
		if ( !entity.getSituacaoCobertura().equals(SituacaoCobertura.REPETIDA) )
			entity.setSituacaoCobertura(entity.getResultadoPrimeiroToque());
		dao.persist(entity);
	}
	

	public void removerRegistroPrimeiroToque(Cobertura entity) {
		
		entity.setDataPrimeiroToque(null);
		entity.setObservacaoPrimeiroToque(null);
		entity.setResultadoPrimeiroToque(null);
		
		if ( entity.getSituacaoCobertura() != null && !entity.getSituacaoCobertura().equals(SituacaoCobertura.REPETIDA) &&
				!entity.getSituacaoCobertura().equals(entity.getResultadoReconfirmacao()))
			entity.setSituacaoCobertura(SituacaoCobertura.INDEFINIDA);
		
		//verifica se existem outras coberturas para o animal com situação PRENHA, ou INDEFINIDA
		CoberturaValidation.validaSituacoesCoberturasDoAnimal(entity);
		
		dao.persist(entity);
		
	}
	
	
	public void registrarReconfirmacao(Cobertura entity){
		CoberturaValidation.validateRegistroReconfirmacao(entity);
		if ( !entity.getSituacaoCobertura().equals(SituacaoCobertura.REPETIDA) )
			entity.setSituacaoCobertura(entity.getResultadoReconfirmacao());
		dao.persist(entity);
	}
	
	public void removerRegistroReconfirmacao(Cobertura entity) {
		
		entity.setDataReconfirmacao(null);
		entity.setObservacaoReconfirmacao(null);
		entity.setResultadoReconfirmacao(null);
		
		if ( entity.getSituacaoCobertura() != null && !entity.getSituacaoCobertura().equals(SituacaoCobertura.REPETIDA) ){
			if ( entity.getResultadoPrimeiroToque() != null && !entity.getResultadoPrimeiroToque().isEmpty() ){
				entity.setSituacaoCobertura(entity.getResultadoPrimeiroToque());
			}else{
				entity.setSituacaoCobertura(SituacaoCobertura.INDEFINIDA);
			}
		}
		
		//verifica se existem outras coberturas para o animal com situação PRENHA, ou INDEFINIDA
		CoberturaValidation.validaSituacoesCoberturasDoAnimal(entity);
		
		dao.persist(entity);
		
	}

	public void registrarRepeticaoCio(Cobertura entity){
		entity.setSituacaoCobertura(SituacaoCobertura.REPETIDA);
		CoberturaValidation.validateRegistroRepeticaoCio(entity);
		dao.persist(entity);
	}
	
	public void removerRegistroRepeticaoCio(Cobertura entity) {
		
		entity.setDataRepeticaoCio(null);
		entity.setObservacaoRepeticaoCio(null);
		
		if ( entity.getResultadoReconfirmacao() != null && !entity.getResultadoReconfirmacao().isEmpty() ){
			entity.setSituacaoCobertura(entity.getResultadoReconfirmacao());
		}else{
			if ( entity.getResultadoPrimeiroToque() != null &&  !entity.getResultadoPrimeiroToque().isEmpty() ){
				entity.setSituacaoCobertura(entity.getResultadoPrimeiroToque());
			}else{
				entity.setSituacaoCobertura(SituacaoCobertura.INDEFINIDA);
			}
		}
		
		//verifica se existem outras coberturas para o animal com situação PRENHA, ou INDEFINIDA
		CoberturaValidation.validaSituacoesCoberturasDoAnimal(entity);
		
		dao.persist(entity);
		
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

}
