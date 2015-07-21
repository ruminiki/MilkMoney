package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milksys.dao.ParametroDao;
import br.com.milksys.model.Parametro;
import br.com.milksys.validation.ParametroValidation;

@Service
public class ParametroService implements IService<Integer, Parametro>{

	@Autowired private ParametroDao dao;

	@Override
	@Transactional
	public boolean save(Parametro entity) {
		ParametroValidation.validate(entity);
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(Parametro entity) {
		return dao.remove(entity);
	}

	@Override
	public Parametro findById(Integer id) {
		return dao.findById(Parametro.class, id);
	}
	
	public String findBySigla(String param) {
		return dao.findBySigla(param);
	}

	@Override
	public List<Parametro> findAll() {
		return dao.findAll(Parametro.class);
	}
	
	public ObservableList<Parametro> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Parametro.class));
	}
	
	@Override
	public ObservableList<Parametro> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.findByDescricaoSigla(param));
	}

	@Override
	public void validate(Parametro entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
