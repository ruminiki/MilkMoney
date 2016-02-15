package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import br.com.milkmoney.dao.ValorIndicadorDao;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.model.ValorIndicador;

@Service
public class ValorIndicadorService implements IService<Integer, ValorIndicador>{

	@Autowired private ValorIndicadorDao dao;

	@Override
	@Transactional
	public boolean save(ValorIndicador valorIndicador) {
		return dao.persist(valorIndicador);
	}

	@Override
	@Transactional
	public boolean remove(ValorIndicador valorIndicador) {
		return dao.remove(valorIndicador);
	}

	@Override
	public ValorIndicador findById(Integer id) {
		return dao.findById(ValorIndicador.class, id);
	}

	@Override
	public List<ValorIndicador> findAll() {
		return dao.findAll(ValorIndicador.class);
	}
	
	@Override
	public ObservableList<ValorIndicador> defaultSearch(String param) {
		throw new NotImplementedException();
	}
	
	public ObservableList<ValorIndicador> findByYear(Indicador indicador, int ano) {
		return FXCollections.observableArrayList(dao.findByYear(indicador, ano));
	}

	@Override
	public void validate(ValorIndicador valorIndicador) {

	}

	@Override
	public ObservableList<ValorIndicador> findAllAsObservableList() {
		return null;
	}
	
	
}
