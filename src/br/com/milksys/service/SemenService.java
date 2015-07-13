package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.SemenDao;
import br.com.milksys.model.Semen;
import br.com.milksys.validation.SemenValidation;

@Service
public class SemenService implements IService<Integer, Semen>{

	@Autowired private SemenDao dao;

	@Override
	public boolean save(Semen entity) {
		
		SemenValidation.validate(entity);
		
		if ( entity.getId() > 0 )
			SemenValidation.validadeQuantidadeUtilizada(entity, dao.findQuantidadeUtilizada(entity));
		
		return dao.persist(entity);
	}

	@Override
	public boolean remove(Semen entity) {
		return dao.remove(entity);
	}

	@Override
	public Semen findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<Semen> findAll() {
		return dao.findAll(Semen.class);
	}
	
	public ObservableList<Semen> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Semen.class));
	}

	@Override
	public ObservableList<Semen> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.findDefault(param));
	}

	@Override
	public void validate(Semen entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
