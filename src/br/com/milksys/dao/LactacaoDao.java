package br.com.milksys.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Animal;
import br.com.milksys.model.Lactacao;

@Repository
public class LactacaoDao extends AbstractGenericDao<Integer, Lactacao> {

	public Lactacao findUltimaLactacaoAnimal(Animal animal) {
		
		Query query = entityManager.createQuery("SELECT e FROM Lactacao e WHERE e.animal = :animal order by e.dataInicio desc");
		query.setParameter("animal", animal);
		query.setMaxResults(1);
		
		try{
			return(Lactacao) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Lactacao> findByAnimal(Animal animal){
		Query query = entityManager.createQuery("SELECT lc FROM Lactacao lc where lc.animal = :animal ");
		query.setParameter("animal", animal);
		return query.getResultList();
	}

}