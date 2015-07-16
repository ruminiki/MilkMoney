package br.com.milksys.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.PrecoLeite;
import br.com.milksys.model.ProducaoLeite;

@Repository
public class ProducaoLeiteDao extends AbstractGenericDao<Integer, ProducaoLeite> {

	public ProducaoLeite findByDate(Date data) {
		
		Query query = entityManager.createQuery("SELECT c FROM ProducaoLeite c WHERE c.data = :data");
		query.setParameter("data", data);
		try{
			return (ProducaoLeite) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<ProducaoLeite> findAllByPeriodo(Date inicio, Date fim) {
		Query query = entityManager.createQuery("SELECT c FROM ProducaoLeite c WHERE c.data between :inicio and  :fim");
		query.setParameter("inicio", inicio);
		query.setParameter("fim", fim);
		return query.getResultList();
	}

	public void updatePrecoLeitePeriodo(PrecoLeite precoLeite, Date dataInicio,	Date dataFim) {
		
		Query query = entityManager.createQuery("update ProducaoLeite set precoLeite = :precoLeite where data between :dataInicio and :dataFim");
		query.setParameter("precoLeite", precoLeite);
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		
		EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        query.executeUpdate();
		entityTransaction.commit();
		
	}

}