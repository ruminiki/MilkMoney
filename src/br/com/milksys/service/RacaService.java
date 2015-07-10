package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.RacaDao;
import br.com.milksys.model.Raca;

@Service
public class RacaService implements IService<Integer, Raca>{

	@Autowired private RacaDao dao;

	@Override
	public boolean save(Raca entity) {
		return dao.persist(entity);
	}

	@Override
	public boolean remove(Raca entity) {
		return dao.remove(entity);
	}

	@Override
	public Raca findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<Raca> findAll() {
		return dao.findAll(Raca.class);
	}
	
	public ObservableList<Raca> findAllAsObservableList() {
		ObservableList<Raca> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(Raca.class));
		return list;
	}
	
	@Override
	public ObservableList<Raca> defaultSearch(String param) {
		return null;
	}

	@Override
	public void validate(Raca entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
