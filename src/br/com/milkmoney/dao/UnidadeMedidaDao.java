package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.UnidadeMedida;

@Repository
public class UnidadeMedidaDao extends AbstractGenericDao<Integer, UnidadeMedida> {

	@SuppressWarnings("unchecked")
	public List<UnidadeMedida> defaultSearch(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM UnidadeMedida r WHERE r.descricao like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

}