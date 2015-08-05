package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.ServicoDao;
import br.com.milkmoney.model.Servico;

@Service
public class ServicoService implements IService<Integer, Servico>{

	@Autowired private ServicoDao dao;

	@Override
	@Transactional
	public boolean save(Servico entity) {
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(Servico entity) {
		return dao.remove(entity);
	}

	@Override
	public Servico findById(Integer id) {
		return dao.findById(Servico.class, id);
	}

	@Override
	public List<Servico> findAll() {
		return dao.findAll(Servico.class);
	}
	
	public ObservableList<Servico> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Servico.class));
	}
	
	@Override
	public ObservableList<Servico> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	@Override
	public void validate(Servico entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}