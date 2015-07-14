package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.IndicadorDao;
import br.com.milksys.model.Indicador;

@Service
public class IndicadorService implements IService<Integer, Indicador>{

	@Autowired private IndicadorDao dao;

	@Override
	public boolean save(Indicador entity) {
		return false;
	}

	@Override
	public boolean remove(Indicador entity) {
		return false;
	}

	@Override
	public Indicador findById(Integer id) {
		return null;
	}

	@Override
	public List<Indicador> findAll() {
		return dao.findAll(Indicador.class);
	}
	
	public ObservableList<Indicador> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Indicador.class));
	}
	
	@Override
	public ObservableList<Indicador> defaultSearch(String param) {
		return null;
	}

	@Override
	public void validate(Indicador entity) {
		
	}
	
	
}
