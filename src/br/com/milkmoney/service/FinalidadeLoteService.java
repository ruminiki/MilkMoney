package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.FinalidadeLoteDao;
import br.com.milkmoney.model.FinalidadeLote;

@Service
@Transactional(propagation=Propagation.SUPPORTS)
public class FinalidadeLoteService implements IService<Integer, FinalidadeLote>{

	@Autowired private FinalidadeLoteDao dao;

	@Override
	@Transactional
	public boolean save(FinalidadeLote entity) {
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(FinalidadeLote entity) {
		return dao.remove(entity);
	}

	@Override
	public FinalidadeLote findById(Integer id) {
		return dao.findById(FinalidadeLote.class, id);
	}

	@Override
	public List<FinalidadeLote> findAll() {
		return dao.findAll(FinalidadeLote.class);
	}
	
	public ObservableList<FinalidadeLote> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(FinalidadeLote.class));
	}

	@Override
	public ObservableList<FinalidadeLote> defaultSearch(String param, int limit) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}
	
	@Override
	public void validate(FinalidadeLote entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
