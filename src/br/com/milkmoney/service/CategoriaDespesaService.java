package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.CategoriaDespesaDao;
import br.com.milkmoney.model.CategoriaDespesa;
import br.com.milkmoney.validation.CategoriaDespesaValidation;

@Service
public class CategoriaDespesaService implements IService<Integer, CategoriaDespesa>{

	@Autowired private CategoriaDespesaDao dao;

	@Override
	@Transactional
	public boolean save(CategoriaDespesa categoriaDespesa) {
		validate(categoriaDespesa);
		return dao.persist(categoriaDespesa);
	}

	@Override
	@Transactional
	public boolean remove(CategoriaDespesa categoriaDespesa) {
		dao.configuraExclusaoCategoria(categoriaDespesa);
		return dao.remove(categoriaDespesa);
	}

	@Override
	public CategoriaDespesa findById(Integer id) {
		return dao.findById(CategoriaDespesa.class, id);
	}

	@Override
	public List<CategoriaDespesa> findAll() {
		return dao.findAll(CategoriaDespesa.class);
	}
	
	public ObservableList<CategoriaDespesa> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(CategoriaDespesa.class));
	}
	
	@Override
	public ObservableList<CategoriaDespesa> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.findByDescricao(param));
	}

	@Override
	public void validate(CategoriaDespesa categoriaDespesa) {
		CategoriaDespesaValidation.validate(categoriaDespesa);
	}
	
	
}
