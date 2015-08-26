package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.CategoriaLancamentoFinanceiroDao;
import br.com.milkmoney.model.CategoriaLancamentoFinanceiro;
import br.com.milkmoney.validation.CategoriaLancamentoFinanceiroValidation;

@Service
public class CategoriaLancamentoFinanceiroService implements IService<Integer, CategoriaLancamentoFinanceiro>{

	@Autowired private CategoriaLancamentoFinanceiroDao dao;

	@Override
	@Transactional
	public boolean save(CategoriaLancamentoFinanceiro categoriaLancamentoFinanceiro) {
		validate(categoriaLancamentoFinanceiro);
		return dao.persist(categoriaLancamentoFinanceiro);
	}

	@Override
	@Transactional
	public boolean remove(CategoriaLancamentoFinanceiro categoriaLancamentoFinanceiro) {
		dao.configuraExclusaoCategoria(categoriaLancamentoFinanceiro);
		return dao.remove(categoriaLancamentoFinanceiro);
	}

	@Override
	public CategoriaLancamentoFinanceiro findById(Integer id) {
		return dao.findById(CategoriaLancamentoFinanceiro.class, id);
	}

	@Override
	public List<CategoriaLancamentoFinanceiro> findAll() {
		return dao.findAll(CategoriaLancamentoFinanceiro.class);
	}
	
	public ObservableList<CategoriaLancamentoFinanceiro> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(CategoriaLancamentoFinanceiro.class));
	}
	
	@Override
	public ObservableList<CategoriaLancamentoFinanceiro> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.findByDescricao(param));
	}

	@Override
	public void validate(CategoriaLancamentoFinanceiro categoriaLancamentoFinanceiro) {
		CategoriaLancamentoFinanceiroValidation.validate(categoriaLancamentoFinanceiro);
	}
	
	
}
