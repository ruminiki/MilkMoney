package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.AnimalDao;
import br.com.milksys.model.Animal;

@Service
public class AnimalService implements IService<Integer, Animal>{

	@Autowired public AnimalDao dao;

	@Override
	public void save(Animal entity) {
		dao.persist(entity);
		
	}

	@Override
	public void remove(Animal entity) {
		dao.remove(entity);
		
	}

	@Override
	public Animal findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Animal> findAll() {
		return dao.findAll(Animal.class);
	}

	public ObservableList<Animal> findAllAsObservableList() {
		ObservableList<Animal> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(Animal.class));
		return list;
	}
	
	
}
