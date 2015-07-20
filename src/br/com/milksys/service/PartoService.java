package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import br.com.milksys.dao.PartoDao;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Parto;
import br.com.milksys.validation.PartoValidation;

@Service
public class PartoService implements IService<Integer, Parto>{

	@Autowired private PartoDao dao;

	@Override
	@Transactional
	public boolean save(Parto parto) {
		throw new NotImplementedException();
	}

	@Override
	@Transactional
	public boolean remove(Parto parto) {
		throw new NotImplementedException();
	}

	@Override
	public Parto findById(Integer id) {
		return dao.findById(Parto.class, id);
	}

	@Override
	public List<Parto> findAll() {
		return dao.findAll(Parto.class);
	}
	
	public ObservableList<Parto> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Parto.class));
	}
	
	@Override
	public ObservableList<Parto> defaultSearch(String param) {
		return null;
	}

	@Override
	public void validate(Parto parto) {
		PartoValidation.validate(parto);
		PartoValidation.validadeCrias(parto);
	}

	public Parto findLastParto(Animal animal) {
		return dao.findLastParto(animal);
	}
	
}
