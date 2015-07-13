package br.com.milksys.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.CausaMorteAnimal;

@Component
public class CausaMorteAnimalDao extends AbstractGenericDao<Integer, CausaMorteAnimal> {

	@SuppressWarnings("unchecked")
	public List<CausaMorteAnimal> findByDescricao(String param) {
		Query query = entityManager.createQuery("SELECT c FROM CausaMorteAnimal c WHERE c.descricao like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
	}
	
}