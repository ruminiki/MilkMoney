package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.CategoriaLancamentoFinanceiro;

@Repository
public class CategoriaLancamentoFinanceiroDao extends AbstractGenericDao<Integer, CategoriaLancamentoFinanceiro> {

	@SuppressWarnings("unchecked")
	public List<CategoriaLancamentoFinanceiro> findByDescricao(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM CategoriaLancamentoFinanceiro r WHERE r.descricao like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<CategoriaLancamentoFinanceiro> findAllOrderly(Class<CategoriaLancamentoFinanceiro> class1) {
		Query query = entityManager.createQuery("SELECT r FROM CategoriaLancamentoFinanceiro r order by descricao");
		return query.getResultList();
	}

}