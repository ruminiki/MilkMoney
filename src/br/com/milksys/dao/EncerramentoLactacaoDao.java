package br.com.milksys.dao;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Animal;
import br.com.milksys.model.EncerramentoLactacao;

@Repository
public class EncerramentoLactacaoDao extends AbstractGenericDao<Integer, EncerramentoLactacao> {

	public void removeLastByAnimal(Animal animal) {
		
		Query query = entityManager.createQuery("SELECT e FROM EncerramentoLactacao e WHERE e.animal = :animal order by e.data desc");
		query.setParameter("animal", animal);
		query.setMaxResults(1);
		
		try{
			EncerramentoLactacao e = (EncerramentoLactacao) query.getSingleResult();
			super.remove(e);
		}catch(NoResultException e){
			throw e;
		}
		
	}

}