package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.TipoProcedimentoDao;
import br.com.milkmoney.model.TipoProcedimento;
import br.com.milkmoney.validation.TipoProcedimentoValidation;

@Service
@Transactional(propagation=Propagation.SUPPORTS)
public class TipoProcedimentoService implements IService<Integer, TipoProcedimento>{

	@Autowired private TipoProcedimentoDao dao;

	@Override
	@Transactional
	public boolean save(TipoProcedimento tipoProcedimento) {
		validate(tipoProcedimento);
		return dao.persist(tipoProcedimento);
	}

	@Override
	@Transactional
	public boolean remove(TipoProcedimento tipoProcedimento) {
		return dao.remove(tipoProcedimento);
	}

	@Override
	public TipoProcedimento findById(Integer id) {
		return dao.findById(TipoProcedimento.class, id);
	}

	@Override
	public List<TipoProcedimento> findAll() {
		return dao.findAll(TipoProcedimento.class);
	}
	
	public ObservableList<TipoProcedimento> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(TipoProcedimento.class));
	}

	@Override
	public ObservableList<TipoProcedimento> defaultSearch(String param, int limit) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}
	
	@Override
	public void validate(TipoProcedimento tipoProcedimento) {
		TipoProcedimentoValidation.validate(tipoProcedimento);
	}
	
	
}
