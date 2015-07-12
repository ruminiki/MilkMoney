package br.com.milksys.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.Semen;

@Component
public class SemenDao extends AbstractGenericDao<Integer, Semen> {

	public int findQuantidadeUtilizada(Semen semen) {
		
		try{
			Query query = entityManager.createQuery("SELECT sum(quantidadeDosesUtilizadas) from Cobertura c where c.semen =:semen ");
			query.setParameter("semen", semen);

			return ((Long)query.getSingleResult()).intValue();
			
		}catch(NoResultException | NullPointerException e){
			return 0;
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<Semen> findAllComEstoque() {
		
		Query query = entityManager.createQuery("SELECT s FROM Semen s "
				+ "WHERE (s.quantidade - coalesce((SELECT sum(quantidadeDosesUtilizadas) from Cobertura c where c.semen.id = s.id), 0)) > 0 ");

		return query.getResultList();
		
	}

}