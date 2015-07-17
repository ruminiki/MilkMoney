package br.com.milksys.service;

import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.AnimalDao;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Lactacao;
import br.com.milksys.validation.AnimalValidation;

@Service
public class AnimalService implements IService<Integer, Animal>{

	@Autowired private AnimalDao dao;

	@Override
	@Transactional
	public boolean save(Animal entity) {
		AnimalValidation.validate(entity);
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(Animal entity) {
		return dao.remove(entity);
	}

	@Override
	public Animal findById(Integer id) {
		return dao.findById(Animal.class, id);
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
	
	@Override
	public ObservableList<Animal> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.findAllByNumeroNome(param));
	}

	public ObservableList<Animal> findAllFemeasAsObservableList() {
		return findAllAsObservableList();
	}

	public ObservableList<Animal> findAllReprodutoresAsObservableList() {
		return findAllAsObservableList();
	}

	@Override
	public void validate(Animal entity) {
		AnimalValidation.validate(entity);
	}

	public Long countAnimaisEmLactacao(Date data) {
		return dao.countAnimaisEmLactacao(data);
	}
	
	public ObservableList<Lactacao> findLactacoesAnimal(Animal animal) {
		return FXCollections.observableArrayList(dao.findLactacoesAnimal(animal));
	}

	public Long getNumeroPartos(Animal animal) {
		return dao.countNumeroPartos(animal);
	}

}
