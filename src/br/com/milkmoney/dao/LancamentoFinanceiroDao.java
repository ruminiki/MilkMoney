package br.com.milkmoney.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.LancamentoFinanceiro;

@Repository
public class LancamentoFinanceiroDao extends AbstractGenericDao<Integer, LancamentoFinanceiro> {

	@SuppressWarnings("unchecked")
	public List<LancamentoFinanceiro> defaultSearch(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM LancamentoFinanceiro r WHERE r.descricao like :param or "
				+ "r.tipoLancamento like :param or "
				+ "r.categoria.descricao like :param or "
				+ "r.centroCusto.descricao like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<LancamentoFinanceiro> findByMes(Date dataInicio, Date dataFim) {
		Query query = entityManager.createQuery("SELECT c FROM LancamentoFinanceiro c WHERE c.dataVencimento between :inicio and :fim");
		query.setParameter("inicio", dataInicio);
		query.setParameter("fim", dataFim);
		return query.getResultList();
	}

}