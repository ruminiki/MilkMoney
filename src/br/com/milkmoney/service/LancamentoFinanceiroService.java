package br.com.milkmoney.service;

import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.LancamentoFinanceiroDao;
import br.com.milkmoney.model.LancamentoFinanceiro;
import br.com.milkmoney.validation.LancamentoFinanceiroValidation;

@Service
public class LancamentoFinanceiroService implements IService<Integer, LancamentoFinanceiro>{

	@Autowired private LancamentoFinanceiroDao dao;

	@Override
	@Transactional
	public boolean save(LancamentoFinanceiro lancamentoFinanceiro) {
		validate(lancamentoFinanceiro);
		return dao.persist(lancamentoFinanceiro);
	}

	@Override
	@Transactional
	public boolean remove(LancamentoFinanceiro lancamentoFinanceiro) {
		return dao.remove(lancamentoFinanceiro);
	}

	@Override
	public LancamentoFinanceiro findById(Integer id) {
		return dao.findById(LancamentoFinanceiro.class, id);
	}

	@Override
	public List<LancamentoFinanceiro> findAll() {
		return dao.findAll(LancamentoFinanceiro.class);
	}
	
	public ObservableList<LancamentoFinanceiro> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(LancamentoFinanceiro.class));
	}
	
	@Override
	public ObservableList<LancamentoFinanceiro> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	@Override
	public void validate(LancamentoFinanceiro lancamentoFinanceiro) {
		LancamentoFinanceiroValidation.validate(lancamentoFinanceiro);
	}

	public List<LancamentoFinanceiro> findByMes(Date dataInicio, Date dataFim) {
		return dao.findByMes(dataInicio, dataFim);
	}
	
	
}
