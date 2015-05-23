package br.com.milksys.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.milksys.dao.AnimalDao;
import br.com.milksys.model.Animal;

@Service
public class AnimalService implements IService<Integer, Animal>{

	@Resource(name = "animalDao")
	public AnimalDao animalDao;

	@Override
	public void save(Animal entity) {
		animalDao.persist(entity);
		
	}

	@Override
	public void remove(Animal entity) {
		animalDao.remove(entity);
		
	}

	@Override
	public Animal findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Animal> findAll() {
		return animalDao.findAll(Animal.class);
	}
	
	
}
