package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.MotivoEncerramentoLactacaoDao;
import br.com.milkmoney.model.MotivoEncerramentoLactacao;
import br.com.milkmoney.validation.MotivoEncerramentoLactacaoValidation;

@Service
public class MotivoEncerramentoLactacaoService implements IService<Integer, MotivoEncerramentoLactacao>{

	@Autowired private MotivoEncerramentoLactacaoDao dao;

	@Override
	@Transactional
	public boolean save(MotivoEncerramentoLactacao MotivoEncerramentoLactacao) {
		validate(MotivoEncerramentoLactacao);
		return dao.persist(MotivoEncerramentoLactacao);
	}

	@Override
	@Transactional
	public boolean remove(MotivoEncerramentoLactacao MotivoEncerramentoLactacao) {
		return dao.remove(MotivoEncerramentoLactacao);
	}

	@Override
	public MotivoEncerramentoLactacao findById(Integer id) {
		return dao.findById(MotivoEncerramentoLactacao.class, id);
	}

	@Override
	public List<MotivoEncerramentoLactacao> findAll() {
		return dao.findAll(MotivoEncerramentoLactacao.class);
	}
	
	public ObservableList<MotivoEncerramentoLactacao> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(MotivoEncerramentoLactacao.class));
	}
	
	@Override
	public ObservableList<MotivoEncerramentoLactacao> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	@Override
	public void validate(MotivoEncerramentoLactacao MotivoEncerramentoLactacao) {
		MotivoEncerramentoLactacaoValidation.validate(MotivoEncerramentoLactacao);
	}
	
	
}
