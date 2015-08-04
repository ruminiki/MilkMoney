package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Lote;

@Repository
public class LoteDao extends AbstractGenericDao<Integer, Lote> {

	@SuppressWarnings("unchecked")
	public List<Lote> findByDescricao(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM Lote r WHERE r.descricao like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

}