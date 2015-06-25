package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.FinalidadeLoteDao;
import br.com.milksys.model.FinalidadeLote;

@Service
public class FinalidadeLoteService implements IService<Integer, FinalidadeLote>{

	@Autowired public FinalidadeLoteDao dao;

	@Override
	public void save(FinalidadeLote entity) {
		dao.persist(entity);
		
	}

	@Override
	public void remove(FinalidadeLote entity) {
		dao.remove(entity);
		
	}

	@Override
	public FinalidadeLote findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FinalidadeLote> findAll() {
		return dao.findAll(FinalidadeLote.class);
	}
	
	public ObservableList<FinalidadeLote> findAllAsObservableList() {
		ObservableList<FinalidadeLote> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(FinalidadeLote.class));
		return list;
	}
	
	
}
