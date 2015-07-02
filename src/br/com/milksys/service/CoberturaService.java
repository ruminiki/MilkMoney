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
	
	public boolean registrarPrimeiroToque(Cobertura entity){
		CoberturaValidation.validateRegistroPrimeiroToque(entity);
		if ( !entity.getSituacaoCobertura().equals(SituacaoCobertura.REPETIDA) )
			entity.setSituacaoCobertura(entity.getResultadoPrimeiroToque());
		return dao.persist(entity);
	}
	
	
	public boolean registrarReconfirmacao(Cobertura entity){
		CoberturaValidation.validateRegistroReconfirmacao(entity);
		if ( !entity.getSituacaoCobertura().equals(SituacaoCobertura.REPETIDA) )
			entity.setSituacaoCobertura(entity.getResultadoReconfirmacao());
		return dao.persist(entity);
	}

	public boolean registrarRepeticaoCio(Cobertura entity){
		entity.setSituacaoCobertura(SituacaoCobertura.REPETIDA);
		CoberturaValidation.validateRegistroRepeticaoCio(entity);
		return dao.persist(entity);
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
