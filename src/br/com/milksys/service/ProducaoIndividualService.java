package br.com.milksys.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.milksys.dao.ProducaoIndividualDao;
import br.com.milksys.model.ProducaoIndividual;
import br.com.milksys.model.ProducaoLeite;

@Service
public class ProducaoIndividualService implements IService<Integer, ProducaoIndividual>{

	@Resource(name = "producaoIndividualDao")
	public ProducaoIndividualDao dao;

	@Override
	public void save(ProducaoIndividual entity) {
		if ( entity.getId() <= 0 ){
			ProducaoLeite p = dao.findByDate(entity.getData());
			if ( p != null ){
				return;
			}
		}
		dao.persist(entity);	
	}
	
	@Override
	public void remove(ProducaoIndividual entity) {
		dao.remove(entity);
		
	}

	@Override
	public ProducaoIndividual findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProducaoIndividual> findAll() {
		return dao.findAll(ProducaoIndividual.class);
	}
	
	
}
