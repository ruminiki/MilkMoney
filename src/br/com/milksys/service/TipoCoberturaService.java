package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.TipoCoberturaDao;
import br.com.milksys.model.TipoCobertura;

@Service
public class TipoCoberturaService implements IService<Integer, TipoCobertura>{

	@Autowired public TipoCoberturaDao dao;

	@Override
	public void save(TipoCobertura entity) {
		dao.persist(entity);
		
	}

	@Override
	public void remove(TipoCobertura entity) {
		dao.remove(entity);
		
	}

	@Override
	public TipoCobertura findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TipoCobertura> findAll() {
		return dao.findAll(TipoCobertura.class);
	}
	
	public ObservableList<TipoCobertura> findAllAsObservableList() {
		ObservableList<TipoCobertura> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(TipoCobertura.class));
		return list;
	}
	
	
}
