package br.com.milksys.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.Raca;

@Component
public class RacaDao extends AbstractGenericDao<Integer, Raca> {

	@SuppressWarnings("unchecked")
	public List<Raca> findByDescricao(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM Raca r WHERE r.descricao like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

}