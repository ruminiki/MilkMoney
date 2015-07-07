package br.com.milksys.dao;

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

}