package br.com.milksys.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.EntregaLeite;

@Component
public class EntregaLeiteDao extends AbstractGenericDao<Integer, EntregaLeite> {

	public EntregaLeite findByDate(Date data) {
		
		Query query = entityManager.createQuery("SELECT c FROM EntregaLeite c WHERE c.data = :data");
		query.setParameter("data", data);
		try{
			return (EntregaLeite) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<EntregaLeite> findAllByPeriodo(Date inicio, Date fim) {
		Query query = entityManager.createQuery("SELECT c FROM EntregaLeite c WHERE c.data between :inicio and  :fim");
		query.setParameter("inicio", inicio);
		query.setParameter("fim", fim);
		return query.getResultList();
	}

}