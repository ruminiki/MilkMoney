package br.com.milksys.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.ProducaoIndividualDao;
import br.com.milksys.model.Animal;
import br.com.milksys.model.ProducaoIndividual;

@Service
public class ProducaoIndividualService implements IService<Integer, ProducaoIndividual>{

	@Autowired public ProducaoIndividualDao dao;

	@Override
	public boolean save(ProducaoIndividual entity) {
		return dao.persist(entity);	
	}
	
	@Override
	public boolean remove(ProducaoIndividual entity) {
		return dao.remove(entity);
	}

	@Override
	public ProducaoIndividual findById(Integer id) {
		return dao.findById(id);
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

	public ProducaoIndividual findByAnimalAndData(Animal animal, Date data) {
		return dao.findByAnimalAndData(animal, data);
	}
	
	
}
