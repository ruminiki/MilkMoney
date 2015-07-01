package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.FuncionarioDao;
import br.com.milksys.model.Funcionario;

@Service
public class FuncionarioService implements IService<Integer, Funcionario>{

	@Autowired public FuncionarioDao dao;

	@Override
	public boolean save(Funcionario entity) {
		return dao.persist(entity);
	}

	@Override
	public boolean remove(Funcionario entity) {
		return dao.remove(entity);
	}

	@Override
	public Funcionario findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<Funcionario> findAll() {
		return dao.findAll(Funcionario.class);
	}
	
	public ObservableList<Funcionario> findAllAsObservableList() {
		ObservableList<Funcionario> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(Funcionario.class));
		return list;
	}

	public ObservableList<Funcionario> findAllAtivosAsObservableList() {
		// TODO Auto-generated method stub
		return findAllAsObservableList();
	}
	
	
}
