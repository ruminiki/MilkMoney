package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Touro;

@Repository
public class TouroDao extends AbstractGenericDao<Integer, Touro> {

	@SuppressWarnings("unchecked")
	public List<Touro> defaultSearch(String param) {
		Query query = entityManager.createQuery("SELECT t FROM Touro t WHERE t.nome like :param or t.codigo like :param");
		query.setParameter("param", '%' + param + '%');
		query.setHint("org.hibernate.cacheable", "false");
		
		return query.getResultList();
	}

}