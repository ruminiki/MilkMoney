package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.OcorrenciaFuncionarioDao;
import br.com.milksys.model.Funcionario;
import br.com.milksys.model.OcorrenciaFuncionario;

@Service
public class OcorrenciaFuncionarioService implements IService<Integer, OcorrenciaFuncionario>{

	@Autowired public OcorrenciaFuncionarioDao dao;

	@Override
	public void save(OcorrenciaFuncionario entity) {
		dao.persist(entity);
		
	}

	@Override
	public void remove(OcorrenciaFuncionario entity) {
		dao.remove(entity);
		
	}

	@Override
	public OcorrenciaFuncionario findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OcorrenciaFuncionario> findAll() {
		return dao.findAll(OcorrenciaFuncionario.class);
	}
	
	public ObservableList<OcorrenciaFuncionario> findAllAsObservableList() {
		ObservableList<OcorrenciaFuncionario> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(OcorrenciaFuncionario.class));
		return list;
	}

	public List<OcorrenciaFuncionario> findByFuncionario(Funcionario funcionario) {
		return dao.findByFuncionario(funcionario);
	}

}