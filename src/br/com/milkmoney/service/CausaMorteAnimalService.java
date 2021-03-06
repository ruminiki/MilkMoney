package br.com.milkmoney.service;

import java.util.List;

import javax.transaction.Transactional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.CausaMorteAnimalDao;
import br.com.milkmoney.model.CausaMorteAnimal;

@Service
public class CausaMorteAnimalService implements IService<Integer, CausaMorteAnimal>{

	@Autowired private CausaMorteAnimalDao dao;

	@Override
	@Transactional
	public boolean save(CausaMorteAnimal entity) {
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(CausaMorteAnimal entity) {
		return dao.remove(entity);
	}

	@Override
	public CausaMorteAnimal findById(Integer id) {
		return dao.findById(CausaMorteAnimal.class, id);
	}

	@Override
	public List<CausaMorteAnimal> findAll() {
		return dao.findAll(CausaMorteAnimal.class);
	}
	
	public ObservableList<CausaMorteAnimal> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(CausaMorteAnimal.class));
	}
	
	@Override
	public ObservableList<CausaMorteAnimal> defaultSearch(String param, int limit) {
		return FXCollections.observableArrayList(dao.findByDescricao(param));
	}

	@Override
	public void validate(CausaMorteAnimal entity) {
		// TODO Auto-generated method stub
		
	}

}
