package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.PrestadorServicoDao;
import br.com.milkmoney.model.PrestadorServico;

@Service
public class PrestadorServicoService implements IService<Integer, PrestadorServico>{

	@Autowired private PrestadorServicoDao dao;

	@Override
	@Transactional
	public boolean save(PrestadorServico entity) {
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(PrestadorServico entity) {
		return dao.remove(entity);
	}

	@Override
	public PrestadorServico findById(Integer id) {
		return dao.findById(PrestadorServico.class, id);
	}

	@Override
	public List<PrestadorServico> findAll() {
		return dao.findAll(PrestadorServico.class);
	}
	
	public ObservableList<PrestadorServico> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(PrestadorServico.class));
	}
	
	@Override
	public ObservableList<PrestadorServico> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	@Override
	public void validate(PrestadorServico entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
