package br.com.milksys.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.FichaAnimalDao;
import br.com.milksys.model.Animal;
import br.com.milksys.model.FichaAnimal;

@Service
public class FichaAnimalService{

	@Autowired private FichaAnimalDao dao;

	public ObservableList<FichaAnimal> findAllByAnimal(Animal animal) {
		return FXCollections.observableArrayList(dao.findAllByAnimal(animal));
	}
	
	
}
