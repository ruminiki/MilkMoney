package br.com.milksys.service;

import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.milksys.dao.ProducaoLeiteDao;
import br.com.milksys.model.ProducaoLeite;

@Service
public class ProducaoLeiteService implements IService<Integer, ProducaoLeite>{

	@Resource(name = "producaoLeiteDao")
	public ProducaoLeiteDao dao;

	@Override
	public void save(ProducaoLeite entity) {
		if ( entity.getId() <= 0 ){
			ProducaoLeite p = dao.findByDate(entity.getData());
			if ( p != null ){
				return;
			}
		}
		dao.persist(entity);	
	}
	
	@Override
	public void remove(ProducaoLeite entity) {
		dao.remove(entity);
		
	}

	@Override
	public ProducaoLeite findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProducaoLeite> findAll() {
		return dao.findAll(ProducaoLeite.class);
	}
	
	public ObservableList<ProducaoLeite> findAllByPeriodoAsObservableList(Date inicio, Date fim) {
		ObservableList<ProducaoLeite> list = FXCollections.observableArrayList();
		list.addAll(dao.findAllByPeriodo(inicio, fim));
		return list;
	}
	
	
}
