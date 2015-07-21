package br.com.milksys.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Cobertura;
import br.com.milksys.model.ConfirmacaoPrenhez;

@Repository
public class ConfirmacaoPrenhezDao extends AbstractGenericDao<Integer, ConfirmacaoPrenhez> {

	@SuppressWarnings("unchecked")
	public List<ConfirmacaoPrenhez> findByCobertura(Cobertura cobertura) {
		Query query = entityManager.createQuery("SELECT c FROM ConfirmacaoPrenhez c WHERE c.cobertura = :cobertura");
		query.setParameter("cobertura", cobertura);
		
		return query.getResultList();
	}

	public ConfirmacaoPrenhez findLastByCobertura(Cobertura cobertura) {
		
		Query query = entityManager.createQuery("SELECT c FROM ConfirmacaoPrenhez c WHERE c.cobertura = :cobertura order by c.data desc");
		query.setParameter("cobertura", cobertura);
		query.setMaxResults(1);
		
		try{
			return (ConfirmacaoPrenhez) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}

}