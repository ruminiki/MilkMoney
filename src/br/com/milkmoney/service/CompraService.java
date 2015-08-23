package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.CompraDao;
import br.com.milkmoney.model.Compra;
import br.com.milkmoney.validation.CompraValidation;

@Service
public class CompraService implements IService<Integer, Compra>{

	@Autowired private CompraDao dao;

	@Override
	@Transactional
	public boolean save(Compra aquisicaoInsumo) {
		validate(aquisicaoInsumo);
		return dao.persist(aquisicaoInsumo);
	}

	@Override
	@Transactional
	public boolean remove(Compra aquisicaoInsumo) {
		return dao.remove(aquisicaoInsumo);
	}

	@Override
	public Compra findById(Integer id) {
		return dao.findById(Compra.class, id);
	}

	@Override
	public List<Compra> findAll() {
		return dao.findAll(Compra.class);
	}
	
	public ObservableList<Compra> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Compra.class));
	}
	
	@Override
	public ObservableList<Compra> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	@Override
	public void validate(Compra aquisicaoInsumo) {
		CompraValidation.validate(aquisicaoInsumo);
	}
	
	
}
