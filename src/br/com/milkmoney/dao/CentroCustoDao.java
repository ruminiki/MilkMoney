package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.CentroCusto;

@Repository
public class CentroCustoDao extends AbstractGenericDao<Integer, CentroCusto> {

	@SuppressWarnings("unchecked")
	public List<CentroCusto> findByDescricao(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM CentroCusto r WHERE r.descricao like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

}