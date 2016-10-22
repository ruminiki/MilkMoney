package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.ComplicacaoPartoDao;
import br.com.milkmoney.model.ComplicacaoParto;
import br.com.milkmoney.validation.ComplicacaoPartoValidation;

@Service
public class ComplicacaoPartoService implements IService<Integer, ComplicacaoParto>{

	@Autowired private ComplicacaoPartoDao dao;

	@Override
	@Transactional
	public boolean save(ComplicacaoParto complicacaoParto) {
		validate(complicacaoParto);
		return dao.persist(complicacaoParto);
	}

	@Override
	@Transactional
	public boolean remove(ComplicacaoParto complicacaoParto) {
		return dao.remove(complicacaoParto);
	}

	@Override
	public ComplicacaoParto findById(Integer id) {
		return dao.findById(ComplicacaoParto.class, id);
	}

	@Override
	public List<ComplicacaoParto> findAll() {
		return dao.findAll(ComplicacaoParto.class);
	}
	
	public ObservableList<ComplicacaoParto> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(ComplicacaoParto.class));
	}
	
	@Override
	public ObservableList<ComplicacaoParto> defaultSearch(String param, int limit) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	@Override
	public void validate(ComplicacaoParto complicacaoParto) {
		ComplicacaoPartoValidation.validate(complicacaoParto);
	}
	
	
}
