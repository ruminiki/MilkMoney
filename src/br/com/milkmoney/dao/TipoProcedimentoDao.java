package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.TipoProcedimento;

@Repository
public class TipoProcedimentoDao extends AbstractGenericDao<Integer, TipoProcedimento> {

	@SuppressWarnings("unchecked")
	public List<TipoProcedimento> defaultSearch(String param) {
		Query query = entityManager.createQuery("SELECT f FROM TipoProcedimento f WHERE f.descricao like :param ");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
	}

}