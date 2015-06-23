package br.com.milksys.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.ProducaoIndividual;

@Component
public class ProducaoIndividualDao extends AbstractGenericDao<Integer, ProducaoIndividual> {

	@SuppressWarnings("unchecked")
	public List<ProducaoIndividual> findByDate(Date data) {
		
		Query query = entityManager.createQuery("SELECT c FROM ProducaoIndividual c WHERE c.data = :data");
		query.setParameter("data", data);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ProducaoIndividual> findAllByPeriodo(Date inicio, Date fim) {
		Query query = entityManager.createQuery("SELECT c FROM ProducaoIndividual c WHERE c.data between :inicio and  :fim");
		query.setParameter("inicio", inicio);
		query.setParameter("fim", fim);
		return query.getResultList();
	}

}