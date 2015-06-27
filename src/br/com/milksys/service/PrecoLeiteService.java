package br.com.milksys.service;

import java.math.BigDecimal;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.PrecoLeiteDao;
import br.com.milksys.model.PrecoLeite;

@Service
public class PrecoLeiteService implements IService<Integer, PrecoLeite>{

	@Autowired public PrecoLeiteDao dao;

	@Override
	public void save(PrecoLeite entity) {
		dao.persist(entity);
		
	}

	@Override
	public void remove(PrecoLeite entity) {
		dao.remove(entity);
		
	}

	@Override
	public PrecoLeite findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PrecoLeite> findAll() {
		return dao.findAll(PrecoLeite.class);
	}
	
	public PrecoLeite findByMesAno(String mesReferencia, int anoReferencia){
		return dao.findByMesAno(mesReferencia, anoReferencia);
	}
	
	public PrecoLeite findByMesAno(int mesReferencia, int anoReferencia) {
		return dao.findByMesAno(mesReferencia, anoReferencia);
	}
	
	public ObservableList<PrecoLeite> findAllAsObservableList() {
		ObservableList<PrecoLeite> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(PrecoLeite.class));
		return list;
	}

	public ObservableList<PrecoLeite> findAllByAnoAsObservableList(int anoReferencia) {
		ObservableList<PrecoLeite> list = FXCollections.observableArrayList();
		list.addAll(dao.findAllByAno(anoReferencia));
		return list;
	}

	public boolean isPrecoCadastrado(String mesReferencia, int anoReferencia) {
		PrecoLeite precoLeite = dao.findByMesAno(mesReferencia, anoReferencia);
		return precoLeite != null && precoLeite.getValor().compareTo(BigDecimal.ZERO) > 0;
	}

}
