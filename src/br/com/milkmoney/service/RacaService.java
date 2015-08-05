package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.RacaDao;
import br.com.milkmoney.model.Raca;
import br.com.milkmoney.validation.RacaValidation;

@Service
public class RacaService implements IService<Integer, Raca>{

	@Autowired private RacaDao dao;

	@Override
	@Transactional
	public boolean save(Raca entity) {
		RacaValidation.validate(entity);
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(Raca entity) {
		return dao.remove(entity);
	}

	@Override
	public Raca findById(Integer id) {
		return dao.findById(Raca.class, id);
	}

	@Override
	public List<Raca> findAll() {
		return dao.findAll(Raca.class);
	}
	
	public ObservableList<Raca> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Raca.class));
	}
	
	@Override
	public ObservableList<Raca> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.findByDescricao(param));
	}

	@Override
	public void validate(Raca entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}