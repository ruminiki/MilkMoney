package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.TouroDao;
import br.com.milksys.model.Touro;

@Service
public class TouroService implements IService<Integer, Touro>{

	@Autowired private TouroDao dao;

	@Override
	public boolean save(Touro entity) {
		return dao.persist(entity);
	}

	@Override
	public boolean remove(Touro entity) {
		return dao.remove(entity);
	}

	@Override
	public Touro findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<Touro> findAll() {
		return dao.findAll(Touro.class);
	}
	
	public ObservableList<Touro> findAllAsObservableList() {
		ObservableList<Touro> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(Touro.class));
		return list;
	}
	
	@Override
	public ObservableList<Touro> defaultSearch(String param) {
		return null;
	}

	@Override
	public void validate(Touro entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
