package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.SemenDao;
import br.com.milksys.model.Semen;

@Service
public class SemenService implements IService<Integer, Semen>{

	@Autowired private SemenDao dao;

	@Override
	public boolean save(Semen entity) {
		return dao.persist(entity);
	}

	@Override
	public boolean remove(Semen entity) {
		return dao.remove(entity);
	}

	@Override
	public Semen findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<Semen> findAll() {
		return dao.findAll(Semen.class);
	}
	
	public ObservableList<Semen> findAllAsObservableList() {
		ObservableList<Semen> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(Semen.class));
		return list;
	}

	public ObservableList<Semen> findAllComEstoqueAsObservableList() {
		// TODO Auto-generated method stub
		return findAllAsObservableList();
	}

	@Override
	public void validate(Semen entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
