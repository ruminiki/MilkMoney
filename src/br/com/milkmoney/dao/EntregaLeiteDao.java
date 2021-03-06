package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.EntregaLeite;

@Repository
public class EntregaLeiteDao extends AbstractGenericDao<Integer, EntregaLeite> {

	@SuppressWarnings("unchecked")
	public List<EntregaLeite> findAllByAno(int anoReferencia) {
		Query query = entityManager.createQuery("SELECT c FROM EntregaLeite c WHERE c.anoReferencia = :anoReferencia");
		query.setParameter("anoReferencia", anoReferencia);
		return query.getResultList();
	}

	public EntregaLeite findByMesAno(String mesReferencia, int anoReferencia) {
		Query query = entityManager.createQuery("SELECT c FROM EntregaLeite c WHERE c.mesReferencia = :mesReferencia and c.anoReferencia = :anoReferencia");
		query.setParameter("mesReferencia", mesReferencia);
		query.setParameter("anoReferencia", anoReferencia);
		try{
			return (EntregaLeite) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	public EntregaLeite getPrimeiraEntrega() {
		Query query = entityManager.createQuery("SELECT c FROM EntregaLeite c order by c.anoReferencia asc");
		query.setMaxResults(1);
		try{
			return (EntregaLeite) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

}