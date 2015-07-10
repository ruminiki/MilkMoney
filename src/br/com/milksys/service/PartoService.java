package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.AnimalDao;
import br.com.milksys.dao.PartoDao;
import br.com.milksys.model.Parto;
import br.com.milksys.validation.PartoValidation;

@Service
public class PartoService implements IService<Integer, Parto>{

	@Autowired private PartoDao dao;
	@Autowired private AnimalDao animalDao;

	@Override
	public boolean save(Parto entity) {
		
		PartoValidation.validate(entity);
    	return dao.persist(entity);
		
	}

	@Override
	public boolean remove(Parto entity) {
		return dao.remove(entity);
	}

	@Override
	public Parto findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<Parto> findAll() {
		return dao.findAll(Parto.class);
	}
	
	public ObservableList<Parto> findAllAsObservableList() {
		ObservableList<Parto> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(Parto.class));
		return list;
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
