package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.ConfirmacaoPrenhes;

@Repository
public class ConfirmacaoPrenhesDao extends AbstractGenericDao<Integer, ConfirmacaoPrenhes> {

	@SuppressWarnings("unchecked")
	public List<ConfirmacaoPrenhes> findByCobertura(Cobertura cobertura) {
		Query query = entityManager.createQuery("SELECT c FROM ConfirmacaoPrenhes c WHERE c.cobertura = :cobertura");
		query.setParameter("cobertura", cobertura);
		
		return query.getResultList();
	}

	public ConfirmacaoPrenhes findLastByCobertura(Cobertura cobertura) {
		
		Query query = entityManager.createQuery("SELECT c FROM ConfirmacaoPrenhes c WHERE c.cobertura = :cobertura order by c.data desc");
		query.setParameter("cobertura", cobertura);
		query.setMaxResults(1);
		
		try{
			return (ConfirmacaoPrenhes) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}

}