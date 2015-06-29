package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.MotivoOcorrenciaFuncionarioDao;
import br.com.milksys.model.MotivoOcorrenciaFuncionario;

@Service
public class MotivoOcorrenciaFuncionarioService implements IService<Integer, MotivoOcorrenciaFuncionario>{

	@Autowired public MotivoOcorrenciaFuncionarioDao dao;

	@Override
	public void save(MotivoOcorrenciaFuncionario entity) {
		dao.persist(entity);
		
	}

	@Override
	public void remove(MotivoOcorrenciaFuncionario entity) {
		dao.remove(entity);
		
	}

	@Override
	public MotivoOcorrenciaFuncionario findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MotivoOcorrenciaFuncionario> findAll() {
		return dao.findAll(MotivoOcorrenciaFuncionario.class);
	}
	
	public ObservableList<MotivoOcorrenciaFuncionario> findAllAsObservableList() {
		ObservableList<MotivoOcorrenciaFuncionario> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(MotivoOcorrenciaFuncionario.class));
		return list;
	}
	
	
}