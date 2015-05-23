package br.com.milksys.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.milksys.dao.RacaDao;
import br.com.milksys.model.Raca;

@Service
public class RacaService implements IService<Integer, Raca>{

	@Resource(name = "racaDao")
	public RacaDao dao;

	@Override
	public void save(Raca entity) {
		dao.persist(entity);
		
	}

	@Override
	public void remove(Raca entity) {
		dao.remove(entity);
		
	}

	@Override
	public Raca findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Raca> findAll() {
		return dao.findAll(Raca.class);
	}
	
	
}
