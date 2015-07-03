package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.FornecedorDao;
import br.com.milksys.model.Fornecedor;

@Service
public class FornecedorService implements IService<Integer, Fornecedor>{

	@Autowired private FornecedorDao dao;

	@Override
	public boolean save(Fornecedor entity) {
		return dao.persist(entity);
	}

	@Override
	public boolean remove(Fornecedor entity) {
		return dao.remove(entity);
		
	}

	@Override
	public Fornecedor findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<Fornecedor> findAll() {
		return dao.findAll(Fornecedor.class);
	}
	
	public ObservableList<Fornecedor> findAllAsObservableList() {
		ObservableList<Fornecedor> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(Fornecedor.class));
		return list;
	}

	@Override
	public void validate(Fornecedor entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
