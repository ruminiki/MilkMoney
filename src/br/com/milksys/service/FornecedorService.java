package br.com.milksys.service;

import java.util.List;

import javax.transaction.Transactional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.FornecedorDao;
import br.com.milksys.model.Fornecedor;
import br.com.milksys.validation.FornecedorValidation;

@Service
public class FornecedorService implements IService<Integer, Fornecedor>{

	@Autowired private FornecedorDao dao;

	@Override
	@Transactional
	public boolean save(Fornecedor entity) {
		
		FornecedorValidation.validate(entity);
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(Fornecedor entity) {
		return dao.remove(entity);
		
	}

	@Override
	public Fornecedor findById(Integer id) {
		return dao.findById(Fornecedor.class, id);
	}

	@Override
	public List<Fornecedor> findAll() {
		return dao.findAll(Fornecedor.class);
	}
	
	public ObservableList<Fornecedor> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Fornecedor.class));
	}

	@Override
	public ObservableList<Fornecedor> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	@Override
	public void validate(Fornecedor entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
