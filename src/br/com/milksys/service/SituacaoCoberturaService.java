package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.SituacaoCoberturaDao;
import br.com.milksys.model.SituacaoCobertura;

@Service
public class SituacaoCoberturaService implements IService<Integer, SituacaoCobertura>{

	@Autowired public SituacaoCoberturaDao dao;

	@Override
	public void save(SituacaoCobertura entity) {
		dao.persist(entity);
		
	}

	@Override
	public void remove(SituacaoCobertura entity) {
		dao.remove(entity);
		
	}

	@Override
	public SituacaoCobertura findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SituacaoCobertura> findAll() {
		return dao.findAll(SituacaoCobertura.class);
	}
	
	public ObservableList<SituacaoCobertura> findAllAsObservableList() {
		ObservableList<SituacaoCobertura> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(SituacaoCobertura.class));
		return list;
	}
	
	
}
