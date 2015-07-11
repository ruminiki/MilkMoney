package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.CausaMorteAnimalDao;
import br.com.milksys.model.CausaMorteAnimal;

@Service
public class CausaMorteAnimalService implements IService<Integer, CausaMorteAnimal>{

	@Autowired private CausaMorteAnimalDao dao;

	@Override
	public boolean save(CausaMorteAnimal entity) {
		return dao.persist(entity);
	}

	@Override
	public boolean remove(CausaMorteAnimal entity) {
		return dao.remove(entity);
	}

	@Override
	public CausaMorteAnimal findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<CausaMorteAnimal> findAll() {
		return dao.findAll(CausaMorteAnimal.class);
	}
	
	public ObservableList<CausaMorteAnimal> findAllAsObservableList() {
		ObservableList<CausaMorteAnimal> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(CausaMorteAnimal.class));
		return list;
	}
	
	@Override
	public ObservableList<CausaMorteAnimal> defaultSearch(String param) {
		return null;
	}

	@Override
	public void validate(CausaMorteAnimal entity) {
		// TODO Auto-generated method stub
		
	}

}
