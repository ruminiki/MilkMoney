package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.TipoInsumoDao;
import br.com.milkmoney.model.TipoInsumo;
import br.com.milkmoney.validation.TipoInsumoValidation;

@Service
public class TipoInsumoService implements IService<Integer, TipoInsumo>{

	@Autowired private TipoInsumoDao dao;

	@Override
	@Transactional
	public boolean save(TipoInsumo tipoInsumo) {
		validate(tipoInsumo);
		return dao.persist(tipoInsumo);
	}

	@Override
	@Transactional
	public boolean remove(TipoInsumo tipoInsumo) {
		return dao.remove(tipoInsumo);
	}

	@Override
	public TipoInsumo findById(Integer id) {
		return dao.findById(TipoInsumo.class, id);
	}

	@Override
	public List<TipoInsumo> findAll() {
		return dao.findAll(TipoInsumo.class);
	}
	
	public ObservableList<TipoInsumo> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(TipoInsumo.class));
	}
	
	@Override
	public ObservableList<TipoInsumo> defaultSearch(String param, int limit) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	@Override
	public void validate(TipoInsumo tipoInsumo) {
		TipoInsumoValidation.validate(tipoInsumo);
	}
	
	
}
