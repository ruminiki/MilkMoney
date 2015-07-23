package br.com.milksys.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.FinalidadeLote;

@Repository
public class FinalidadeLoteDao extends AbstractGenericDao<Integer, FinalidadeLote> {

	@SuppressWarnings("unchecked")
	public List<FinalidadeLote> defaultSearch(String param) {
		Query query = entityManager.createQuery("SELECT f FROM FinalidadeLote f WHERE f.descricao like :param ");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
	}

}