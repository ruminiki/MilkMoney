package br.com.milksys.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.EntregaLeite;

@Component
public class EntregaLeiteDao extends AbstractGenericDao<Integer, EntregaLeite> {

	@SuppressWarnings("unchecked")
	public List<EntregaLeite> findAllByAno(int anoReferencia) {
		Query query = entityManager.createQuery("SELECT c FROM EntregaLeite c WHERE c.anoReferencia = :anoReferencia");
		query.setParameter("anoReferencia", anoReferencia);
		return query.getResultList();
	}

	public EntregaLeite findByMes(String mesReferencia) {
		Query query = entityManager.createQuery("SELECT c FROM EntregaLeite c WHERE c.mesReferencia = :mesReferencia");
		query.setParameter("mesReferencia", mesReferencia);
		try{
			return (EntregaLeite) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

}