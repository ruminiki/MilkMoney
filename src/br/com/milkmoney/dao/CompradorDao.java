package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Comprador;

@Repository
public class CompradorDao extends AbstractGenericDao<Integer, Comprador> {

	@SuppressWarnings("unchecked")
	public List<Comprador> defaultSearch(String param) {
		Query query = entityManager.createQuery("SELECT c FROM Comprador c where c.nome like :param");
		query.setParameter("param", "%"+param+"%");
		
		return query.getResultList();
	}

}