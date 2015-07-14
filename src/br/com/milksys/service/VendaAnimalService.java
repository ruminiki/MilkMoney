package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.VendaAnimalDao;
import br.com.milksys.model.Animal;
import br.com.milksys.model.VendaAnimal;
import br.com.milksys.validation.VendaAnimalValidation;

@Service
public class VendaAnimalService implements IService<Integer, VendaAnimal>{

	@Autowired private VendaAnimalDao dao;

	@Override
	public boolean save(VendaAnimal entity) {
		
		VendaAnimalValidation.validate(entity);
		return dao.persist(entity);
		
	}
	
	@Override
	public boolean remove(VendaAnimal entity) {
		return dao.remove(entity);
	}
	
	public void removeByAnimal(Animal animal){
		dao.removeByAnimal(animal);
	}

	@Override
	public VendaAnimal findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<VendaAnimal> findAll() {
		return dao.findAll(VendaAnimal.class);
	}
	
	public ObservableList<VendaAnimal> findAllAsObservableList() {
		ObservableList<VendaAnimal> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(VendaAnimal.class));
		return list;
	}

	@Override
	public ObservableList<VendaAnimal> defaultSearch(String param) {
		return null;
	}
	
	@Override
	public void validate(VendaAnimal entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
