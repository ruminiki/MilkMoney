package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.CompradorDao;
import br.com.milksys.model.Comprador;
import br.com.milksys.validation.CompradorValidation;

@Service
public class CompradorService implements IService<Integer, Comprador>{

	@Autowired private CompradorDao dao;

	@Override
	public boolean save(Comprador entity) {
		
		CompradorValidation.validate(entity);
		return dao.persist(entity);
	}

	@Override
	public boolean remove(Comprador entity) {
		return dao.remove(entity);
		
	}

	@Override
	public Comprador findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<Comprador> findAll() {
		return dao.findAll(Comprador.class);
	}
	
	public ObservableList<Comprador> findAllAsObservableList() {
		ObservableList<Comprador> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(Comprador.class));
		return list;
	}

	@Override
	public void validate(Comprador entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
