package br.com.milksys.service;

import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.ProducaoIndividualDao;
import br.com.milksys.model.Animal;
import br.com.milksys.model.ProducaoIndividual;

@Service
public class ProducaoIndividualService implements IService<Integer, ProducaoIndividual>{

	@Autowired public ProducaoIndividualDao dao;

	@Override
	public void save(ProducaoIndividual entity) {
		dao.persist(entity);	
	}
	
	@Override
	public void remove(ProducaoIndividual entity) {
		dao.remove(entity);
		
	}

	@Override
	public ProducaoIndividual findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProducaoIndividual> findAll() {
		return dao.findAll(ProducaoIndividual.class);
	}

	public List<ProducaoIndividual> findByDate(Date data) {
		return dao.findByDate(data);
		//return list;
	}

	public List<ProducaoIndividual> findByAnimal(Animal animal) {
		return dao.findByAnimal(animal);
	}
	
	
}
