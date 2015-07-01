package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.EntregaLeiteDao;
import br.com.milksys.model.EntregaLeite;

@Service
public class EntregaLeiteService implements IService<Integer, EntregaLeite>{

	@Autowired public EntregaLeiteDao dao;

	@Override
	public boolean save(EntregaLeite entity) {
		return dao.persist(entity);	
	}
	
	@Override
	public boolean remove(EntregaLeite entity) {
		return dao.remove(entity);
	}

	@Override
	public EntregaLeite findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<EntregaLeite> findAll() {
		return dao.findAll(EntregaLeite.class);
	}
	
	public EntregaLeite findByMesAno(String mes, int ano){
		return dao.findByMesAno(mes, ano);
	}
	
	public ObservableList<EntregaLeite> findAllByAnoAsObservableList(int anoReferencia) {
		ObservableList<EntregaLeite> list = FXCollections.observableArrayList();
		list.addAll(dao.findAllByAno(anoReferencia));
		return list;
	}
	
	
}
