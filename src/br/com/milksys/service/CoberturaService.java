package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.CoberturaDao;
import br.com.milksys.model.Cobertura;

@Service
public class CoberturaService implements IService<Integer, Cobertura>{

	@Autowired public CoberturaDao dao;

	@Override
	public void save(Cobertura entity) {
		dao.persist(entity);
		
	}

	@Override
	public void remove(Cobertura entity) {
		dao.remove(entity);
		
	}

	@Override
	public Cobertura findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cobertura> findAll() {
		return dao.findAll(Cobertura.class);
	}
	
	public ObservableList<Cobertura> findAllAsObservableList() {
		ObservableList<Cobertura> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(Cobertura.class));
		return list;
	}

	public void removeServicoFromCobertura(Cobertura cobertura) {
		dao.removeServicoFromCobertura(cobertura);
	}
	
	
}
