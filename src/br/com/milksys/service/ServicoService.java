package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.ServicoDao;
import br.com.milksys.model.Servico;

@Service
public class ServicoService implements IService<Integer, Servico>{

	@Autowired public ServicoDao dao;

	@Override
	public void save(Servico entity) {
		dao.persist(entity);
		
	}

	@Override
	public void remove(Servico entity) {
		dao.remove(entity);
		
	}

	@Override
	public Servico findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Servico> findAll() {
		return dao.findAll(Servico.class);
	}
	
	public ObservableList<Servico> findAllAsObservableList() {
		ObservableList<Servico> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(Servico.class));
		return list;
	}
	
	
}
