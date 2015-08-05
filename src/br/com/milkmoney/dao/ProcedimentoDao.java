package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Procedimento;

@Repository
public class ProcedimentoDao extends AbstractGenericDao<Integer, Procedimento> {

	@SuppressWarnings("unchecked")
	public List<Procedimento> defaultSearch(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM Procedimento r WHERE r.descricao like :param or "
				+ "r.tipoProcedimento.descricao like :param or "
				+ "r.responsavel like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

}