package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.TipoInsumo;

@Repository
public class TipoInsumoDao extends AbstractGenericDao<Integer, TipoInsumo> {

	@SuppressWarnings("unchecked")
	public List<TipoInsumo> defaultSearch(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM TipoInsumo r WHERE r.descricao like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

}