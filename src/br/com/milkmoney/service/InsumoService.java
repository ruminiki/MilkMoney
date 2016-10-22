package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.InsumoDao;
import br.com.milkmoney.model.Insumo;
import br.com.milkmoney.validation.InsumoValidation;

@Service
public class InsumoService implements IService<Integer, Insumo>{

	@Autowired private InsumoDao dao;

	@Override
	@Transactional
	public boolean save(Insumo insumo) {
		validate(insumo);
		return dao.persist(insumo);
	}

	@Override
	@Transactional
	public boolean remove(Insumo insumo) {
		return dao.remove(insumo);
	}

	@Override
	public Insumo findById(Integer id) {
		return dao.findById(Insumo.class, id);
	}

	@Override
	public List<Insumo> findAll() {
		return dao.findAll(Insumo.class);
	}
	
	public ObservableList<Insumo> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Insumo.class));
	}
	
	@Override
	public ObservableList<Insumo> defaultSearch(String param, int limit) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	@Override
	public void validate(Insumo insumo) {
		InsumoValidation.validate(insumo);
	}
	
	
}
