package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.UnidadeMedidaDao;
import br.com.milkmoney.model.UnidadeMedida;
import br.com.milkmoney.validation.UnidadeMedidaValidation;

@Service
public class UnidadeMedidaService implements IService<Integer, UnidadeMedida>{

	@Autowired private UnidadeMedidaDao dao;

	@Override
	@Transactional
	public boolean save(UnidadeMedida unidadeMedida) {
		validate(unidadeMedida);
		return dao.persist(unidadeMedida);
	}

	@Override
	@Transactional
	public boolean remove(UnidadeMedida unidadeMedida) {
		return dao.remove(unidadeMedida);
	}

	@Override
	public UnidadeMedida findById(Integer id) {
		return dao.findById(UnidadeMedida.class, id);
	}

	@Override
	public List<UnidadeMedida> findAll() {
		return dao.findAll(UnidadeMedida.class);
	}
	
	public ObservableList<UnidadeMedida> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(UnidadeMedida.class));
	}
	
	@Override
	public ObservableList<UnidadeMedida> defaultSearch(String param, int limit) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	@Override
	public void validate(UnidadeMedida unidadeMedida) {
		UnidadeMedidaValidation.validate(unidadeMedida);
	}
	
	
}
