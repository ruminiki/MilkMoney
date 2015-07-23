package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.TouroDao;
import br.com.milkmoney.model.Touro;

@Service
public class TouroService implements IService<Integer, Touro>{

	@Autowired private TouroDao dao;

	@Override
	@Transactional
	public boolean save(Touro entity) {
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(Touro entity) {
		return dao.remove(entity);
	}

	@Override
	public Touro findById(Integer id) {
		return dao.findById(Touro.class, id);
	}

	@Override
	public List<Touro> findAll() {
		return dao.findAll(Touro.class);
	}
	
	public ObservableList<Touro> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Touro.class));
	}
	
	@Override
	public ObservableList<Touro> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	@Override
	public void validate(Touro entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
