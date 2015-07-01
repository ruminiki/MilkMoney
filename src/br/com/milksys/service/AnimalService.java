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
	public boolean save(Animal entity) {
		return dao.persist(entity);
	}

	@Override
	public boolean remove(Animal entity) {
		return dao.remove(entity);
	}

	@Override
	public Animal findById(Integer id) {
		return dao.findById(id);
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

	public ObservableList<Animal> findAllFemeasAsObservableList() {
		return findAllAsObservableList();
	}

	public ObservableList<Animal> findAllReprodutoresAsObservableList() {
		return findAllAsObservableList();
	}
	
	
}
