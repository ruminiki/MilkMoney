package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.OcorrenciaFuncionarioDao;
import br.com.milkmoney.model.Funcionario;
import br.com.milkmoney.model.OcorrenciaFuncionario;

@Service
public class OcorrenciaFuncionarioService implements IService<Integer, OcorrenciaFuncionario>{

	@Autowired private OcorrenciaFuncionarioDao dao;

	@Override
	@Transactional
	public boolean save(OcorrenciaFuncionario entity) {
		return dao.persist(entity);
		
	}

	@Override
	@Transactional
	public boolean remove(OcorrenciaFuncionario entity) {
		return dao.remove(entity);
	}

	@Override
	public OcorrenciaFuncionario findById(Integer id) {
		return dao.findById(OcorrenciaFuncionario.class, id);
	}

	@Override
	public List<OcorrenciaFuncionario> findAll() {
		return dao.findAll(OcorrenciaFuncionario.class);
	}
	
	public ObservableList<OcorrenciaFuncionario> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(OcorrenciaFuncionario.class));
	}

	@Override
	public ObservableList<OcorrenciaFuncionario> defaultSearch(String param) {
		return null;
	}
	
	public List<OcorrenciaFuncionario> findByFuncionario(Funcionario funcionario) {
		return dao.findByFuncionario(funcionario);
	}

	@Override
	public void validate(OcorrenciaFuncionario entity) {
		// TODO Auto-generated method stub
		
	}

}
