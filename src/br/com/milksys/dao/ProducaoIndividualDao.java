package br.com.milksys.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.ProducaoIndividual;
import br.com.milksys.model.ProducaoLeite;

@Component
public class ProducaoIndividualDao extends AbstractGenericDao<Integer, ProducaoIndividual> {

	public ProducaoLeite findByDate(Date data) {
		
		Query query = entityManager.createQuery("SELECT c FROM ProducaoIndividual c WHERE c.data = :data");
		query.setParameter("data", data);
		try{
			return (ProducaoLeite) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<ProducaoLeite> findAllByPeriodo(Date inicio, Date fim) {
		Query query = entityManager.createQuery("SELECT c FROM ProducaoIndividual c WHERE c.data between :inicio and  :fim");
		query.setParameter("inicio", inicio);
		query.setParameter("fim", fim);
		return query.getResultList();
	}

}