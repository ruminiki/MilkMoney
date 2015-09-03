package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.ComplicacaoParto;

@Repository
public class ComplicacaoPartoDao extends AbstractGenericDao<Integer, ComplicacaoParto> {

	@SuppressWarnings("unchecked")
	public List<ComplicacaoParto> defaultSearch(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM ComplicacaoParto r WHERE r.descricao like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

}