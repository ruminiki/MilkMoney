package br.com.milksys.service;

import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.ProducaoIndividualDao;
import br.com.milksys.model.ProducaoIndividual;

@Service
public class ProducaoIndividualService implements IService<Integer, ProducaoIndividual>{

	@Autowired public ProducaoIndividualDao dao;

	@Override
	public void save(ProducaoIndividual entity) {
		dao.persist(entity);	
	}
	
	@Override
	public void remove(ProducaoIndividual entity) {
		dao.remove(entity);
		
	}

	@Override
	public ProducaoIndividual findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProducaoIndividual> findAll() {
		return dao.findAll(ProducaoIndividual.class);
	}

	public ObservableList<ProducaoIndividual> findByDate(Date data) {
		ObservableList<ProducaoIndividual> list = FXCollections.observableArrayList();
		list.addAll(dao.findByDate(data));
		return list;
	}
	
	
}
