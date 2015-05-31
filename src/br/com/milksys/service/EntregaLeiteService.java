package br.com.milksys.service;

import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.milksys.dao.EntregaLeiteDao;
import br.com.milksys.model.EntregaLeite;

@Service
public class EntregaLeiteService implements IService<Integer, EntregaLeite>{

	@Resource(name = "entregaLeiteDao")
	public EntregaLeiteDao dao;

	@Override
	public void save(EntregaLeite entity) {
		if ( entity.getId() <= 0 ){
			EntregaLeite el = dao.findByDate(entity.getData());
			if ( el == null ){
				dao.persist(entity);
			}
		}else{
			dao.persist(entity);	
		}
	}
	
	@Override
	public void remove(EntregaLeite entity) {
		dao.remove(entity);
		
	}

	@Override
	public EntregaLeite findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntregaLeite> findAll() {
		return dao.findAll(EntregaLeite.class);
	}
	
	public ObservableList<EntregaLeite> findAllByPeriodoAsObservableList(Date inicio, Date fim) {
		ObservableList<EntregaLeite> list = FXCollections.observableArrayList();
		list.addAll(dao.findAllByPeriodo(inicio, fim));
		return list;
	}
	
	
}
