package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.MotivoOcorrenciaFuncionarioDao;
import br.com.milkmoney.model.MotivoOcorrenciaFuncionario;

@Service
public class MotivoOcorrenciaFuncionarioService implements IService<Integer, MotivoOcorrenciaFuncionario>{

	@Autowired private MotivoOcorrenciaFuncionarioDao dao;

	@Override
	@Transactional
	public boolean save(MotivoOcorrenciaFuncionario entity) {
		return dao.persist(entity);
		
	}

	@Override
	@Transactional
	public boolean remove(MotivoOcorrenciaFuncionario entity) {
		return dao.remove(entity);
	}

	@Override
	public MotivoOcorrenciaFuncionario findById(Integer id) {
		return dao.findById(MotivoOcorrenciaFuncionario.class, id);
	}

	@Override
	public List<MotivoOcorrenciaFuncionario> findAll() {
		return dao.findAll(MotivoOcorrenciaFuncionario.class);
	}
	
	public ObservableList<MotivoOcorrenciaFuncionario> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(MotivoOcorrenciaFuncionario.class));
	}
	
	@Override
	public ObservableList<MotivoOcorrenciaFuncionario> defaultSearch(String param, int limit) {
		return FXCollections.observableArrayList(dao.findByDescricao(param));
	}

	@Override
	public void validate(MotivoOcorrenciaFuncionario entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
