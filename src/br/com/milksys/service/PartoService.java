package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milksys.dao.PartoDao;
import br.com.milksys.model.Parto;
import br.com.milksys.validation.PartoValidation;

@Service
public class PartoService implements IService<Integer, Parto>{

	@Autowired private PartoDao dao;

	@Override
	@Transactional
	public boolean save(Parto entity) {
    	return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(Parto entity) {
		return dao.remove(entity);
	}

	@Override
	public Parto findById(Integer id) {
		return dao.findById(Parto.class, id);
	}

	@Override
	public List<Parto> findAll() {
		return dao.findAll(Parto.class);
	}
	
	public ObservableList<Parto> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Parto.class));
	}
	
	@Override
	public ObservableList<Parto> defaultSearch(String param) {
		return null;
	}

	@Override
	public void validate(Parto entity) {
		PartoValidation.validate(entity);
	}
	
}
