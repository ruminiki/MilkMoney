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

	@Autowired private PrestadorServicoDao dao;

	@Override
	public boolean save(PrestadorServico entity) {
		return dao.persist(entity);
	}

	@Override
	public boolean remove(PrestadorServico entity) {
		return dao.remove(entity);
	}

	@Override
	public PrestadorServico findById(Integer id) {
		return dao.findById(id);
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
