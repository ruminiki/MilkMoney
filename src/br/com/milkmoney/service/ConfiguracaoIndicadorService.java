package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import br.com.milkmoney.dao.ConfiguracaoIndicadorDao;
import br.com.milkmoney.model.ConfiguracaoIndicador;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.validation.ConfiguracaoIndicadorValidation;

@Service
public class ConfiguracaoIndicadorService implements IService<Integer, ConfiguracaoIndicador>{

	@Autowired private ConfiguracaoIndicadorDao dao;

	@Override
	@Transactional
	public boolean save(ConfiguracaoIndicador configuracaoIndicador) {
		validate(configuracaoIndicador);
		return dao.persist(configuracaoIndicador);
	}

	@Override
	@Transactional
	public boolean remove(ConfiguracaoIndicador configuracaoIndicador) {
		return dao.remove(configuracaoIndicador);
	}

	@Override
	public ConfiguracaoIndicador findById(Integer id) {
		return dao.findById(ConfiguracaoIndicador.class, id);
	}

	@Override
	public List<ConfiguracaoIndicador> findAll() {
		return dao.findAll(ConfiguracaoIndicador.class);
	}
	
	@Override
	public ObservableList<ConfiguracaoIndicador> defaultSearch(String param, int limit) {
		throw new NotImplementedException();
	}
	
	public ObservableList<ConfiguracaoIndicador> defaultSearch(Object[] param) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}
	
	public ObservableList<ConfiguracaoIndicador> findByYear(int ano) {
		return FXCollections.observableArrayList(dao.findByYear(ano));
	}
	
	public ObservableList<ConfiguracaoIndicador> findByYear(Indicador indicador, int ano) {
		return FXCollections.observableArrayList(dao.findByYear(indicador, ano));
	}

	@Override
	public void validate(ConfiguracaoIndicador configuracaoIndicador) {
		ConfiguracaoIndicadorValidation.validate(configuracaoIndicador);
	}

	@Override
	public ObservableList<ConfiguracaoIndicador> findAllAsObservableList() {
		return null;
	}

	
}
