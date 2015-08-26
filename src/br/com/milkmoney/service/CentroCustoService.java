package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.CentroCustoDao;
import br.com.milkmoney.model.CentroCusto;
import br.com.milkmoney.validation.CentroCustoValidation;

@Service
public class CentroCustoService implements IService<Integer, CentroCusto>{

	@Autowired private CentroCustoDao dao;

	@Override
	@Transactional
	public boolean save(CentroCusto centroCusto) {
		validate(centroCusto);
		return dao.persist(centroCusto);
	}

	@Override
	@Transactional
	public boolean remove(CentroCusto centroCusto) {
		return dao.remove(centroCusto);
	}

	@Override
	public CentroCusto findById(Integer id) {
		return dao.findById(CentroCusto.class, id);
	}

	@Override
	public List<CentroCusto> findAll() {
		return dao.findAll(CentroCusto.class);
	}
	
	public ObservableList<CentroCusto> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(CentroCusto.class));
	}
	
	@Override
	public ObservableList<CentroCusto> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.findByDescricao(param));
	}

	@Override
	public void validate(CentroCusto centroCusto) {
		CentroCustoValidation.validate(centroCusto);
	}
	
	
}
