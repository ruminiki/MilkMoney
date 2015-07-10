package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.MotivoVendaAnimalDao;
import br.com.milksys.model.MotivoVendaAnimal;

@Service
public class MotivoVendaAnimalService implements IService<Integer, MotivoVendaAnimal>{

	@Autowired private MotivoVendaAnimalDao dao;

	@Override
	public boolean save(MotivoVendaAnimal entity) {
		return dao.persist(entity);
	}

	@Override
	public boolean remove(MotivoVendaAnimal entity) {
		return dao.remove(entity);
	}

	@Override
	public MotivoVendaAnimal findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<MotivoVendaAnimal> findAll() {
		return dao.findAll(MotivoVendaAnimal.class);
	}
	
	public ObservableList<MotivoVendaAnimal> findAllAsObservableList() {
		ObservableList<MotivoVendaAnimal> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(MotivoVendaAnimal.class));
		return list;
	}
	
	@Override
	public ObservableList<MotivoVendaAnimal> defaultSearch(String param) {
		return null;
	}

	@Override
	public void validate(MotivoVendaAnimal entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
