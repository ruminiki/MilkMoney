package br.com.milksys.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.milksys.dao.GenericDao;
import br.com.milksys.model.CalendarioRecolha;

@Service
public class CalendarioRecolhaService implements IService<Integer, CalendarioRecolha>{

	@Resource(name = "calendarioRecolhaDao")
	public GenericDao<Integer, CalendarioRecolha> dao;

	@Override
	public void save(CalendarioRecolha entity) {
		dao.persist(entity);
	}

	@Override
	public void remove(CalendarioRecolha entity) {
		dao.remove(entity);
	}

	@Override
	public CalendarioRecolha findById(Integer id) {
		return null;
	}

	@Override
	public List<CalendarioRecolha> findAll() {
		return dao.findAll(CalendarioRecolha.class);
	}
	
	
}
