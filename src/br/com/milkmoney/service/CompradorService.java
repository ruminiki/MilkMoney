package br.com.milkmoney.service;

import java.util.List;

import javax.transaction.Transactional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.CompradorDao;
import br.com.milkmoney.model.Comprador;
import br.com.milkmoney.validation.CompradorValidation;

@Service
public class CompradorService implements IService<Integer, Comprador>{

	@Autowired private CompradorDao dao;

	@Override
	@Transactional
	public boolean save(Comprador entity) {
		
		CompradorValidation.validate(entity);
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(Comprador entity) {
		return dao.remove(entity);
		
	}

	@Override
	public Comprador findById(Integer id) {
		return dao.findById(Comprador.class, id);
	}

	@Override
	public List<Comprador> findAll() {
		return dao.findAll(Comprador.class);
	}
	
	public ObservableList<Comprador> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Comprador.class));
	}
	
	@Override
	public ObservableList<Comprador> defaultSearch(String param, int limit) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	@Override
	public void validate(Comprador entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
