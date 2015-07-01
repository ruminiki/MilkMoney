package br.com.milksys.service;

import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.ProducaoLeiteDao;
import br.com.milksys.model.PrecoLeite;
import br.com.milksys.model.ProducaoLeite;

@Service
public class ProducaoLeiteService implements IService<Integer, ProducaoLeite>{

	@Autowired public ProducaoLeiteDao dao;

	@Override
	public boolean save(ProducaoLeite entity) {
		if ( entity.getId() <= 0 ){
			ProducaoLeite p = dao.findByDate(entity.getData());
			if ( p != null ){
				return true;
			}
		}
		return dao.persist(entity);	
	}
	
	@Override
	public boolean remove(ProducaoLeite entity) {
		return dao.remove(entity);
	}

	@Override
	public ProducaoLeite findById(Integer id) {
		return dao.findById(id);
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

	public void updatePrecoLeitePeriodo(PrecoLeite precoLeite, Date dataInicio, Date dataFim) {
		dao.updatePrecoLeitePeriodo(precoLeite, dataInicio, dataFim);
	}
	
	
}
