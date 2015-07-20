package br.com.milksys.dao;

import java.util.List;

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

}