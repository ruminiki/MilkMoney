package br.com.milksys.service;

import java.util.List;

import javax.transaction.Transactional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.FuncionarioDao;
import br.com.milksys.model.Funcionario;

@Service
public class FuncionarioService implements IService<Integer, Funcionario>{

	@Autowired private FuncionarioDao dao;

	@Override
	@Transactional
	public boolean save(Funcionario entity) {
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(Funcionario entity) {
		return dao.remove(entity);
	}

	@Override
	public Funcionario findById(Integer id) {
		return dao.findById(Funcionario.class, id);
	}

	@Override
	public List<Funcionario> findAll() {
		return dao.findAll(Funcionario.class);
	}
	
	public ObservableList<Funcionario> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Funcionario.class));
	}

	@Override
	public ObservableList<Funcionario> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.findByNome(param));
	}
	
	public ObservableList<Funcionario> findAllAtivosAsObservableList() {
		// TODO Auto-generated method stub
		return findAllAsObservableList();
	}

	@Override
	public void validate(Funcionario entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
