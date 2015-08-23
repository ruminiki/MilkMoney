package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Insumo;

@Repository
public class InsumoDao extends AbstractGenericDao<Integer, Insumo> {

	@SuppressWarnings("unchecked")
	public List<Insumo> defaultSearch(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM Insumo r WHERE r.descricao like :param "
				+ "or r.unidadeMedida.descricao like :param "
				+ "or r.tipoInsumo.descricao like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

}