package br.com.milksys.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.PrecoLeite;

@Component
public class PrecoLeiteDao extends AbstractGenericDao<Integer, PrecoLeite> {

	public PrecoLeite findByMesAno(String mesReferencia, int anoReferencia) {
		
		Query query = entityManager.createQuery("SELECT p FROM PrecoLeite p WHERE p.mesReferencia = :mesReferencia and p.anoReferencia = :anoReferencia");
		query.setParameter("mesReferencia", mesReferencia);
		query.setParameter("anoReferencia", anoReferencia);
		try{
			return (PrecoLeite) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<PrecoLeite> findAllByAno(int anoReferencia) {

		Query query = entityManager.createQuery("SELECT p FROM PrecoLeite p WHERE p.anoReferencia = :anoReferencia order by codigoMes");
		query.setParameter("anoReferencia", anoReferencia);
		return query.getResultList();
		
	}

}