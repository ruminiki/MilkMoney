package br.com.milksys.service;

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
			EntregaLeite el = dao.findByMes(entity.getMesReferencia());
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
		return null;
	}

	@Override
	public List<EntregaLeite> findAll() {
		return dao.findAll(EntregaLeite.class);
	}
	
	public ObservableList<EntregaLeite> findAllByAnoAsObservableList(int anoReferencia) {
		ObservableList<EntregaLeite> list = FXCollections.observableArrayList();
		list.addAll(dao.findAllByAno(anoReferencia));
		return list;
	}
	
	
}
