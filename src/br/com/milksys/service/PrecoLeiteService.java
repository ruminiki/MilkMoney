package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.milksys.dao.PrecoLeiteDao;
import br.com.milksys.model.PrecoLeite;

@Service
public class PrecoLeiteService implements IService<Integer, PrecoLeite>{

	@Resource(name = "precoLeiteDao")
	public PrecoLeiteDao dao;

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
	
	public ObservableList<PrecoLeite> findAllAsObservableList() {
		ObservableList<PrecoLeite> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(PrecoLeite.class));
		return list;
	}
	
	
}
