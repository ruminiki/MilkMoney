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

	@Autowired public SemenDao dao;

	@Override
	public void save(Semen entity) {
		dao.persist(entity);
		
	}

	@Override
	public void remove(Semen entity) {
		dao.remove(entity);
		
	}

	@Override
	public Semen findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
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
	
	
}