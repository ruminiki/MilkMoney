package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.PrestadorServicoDao;
import br.com.milksys.model.PrestadorServico;

@Service
public class PrestadorServicoService implements IService<Integer, PrestadorServico>{

	@Autowired public PrestadorServicoDao dao;

	@Override
	public void save(PrestadorServico entity) {
		dao.persist(entity);
		
	}

	@Override
	public void remove(PrestadorServico entity) {
		dao.remove(entity);
		
	}

	@Override
	public PrestadorServico findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PrestadorServico> findAll() {
		return dao.findAll(PrestadorServico.class);
	}
	
	public ObservableList<PrestadorServico> findAllAsObservableList() {
		ObservableList<PrestadorServico> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(PrestadorServico.class));
		return list;
	}
	
	
}
