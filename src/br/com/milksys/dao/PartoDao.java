package br.com.milksys.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Animal;
import br.com.milksys.model.Parto;

@Repository
public class PartoDao extends AbstractGenericDao<Integer, Parto> {

	public Parto findLastParto(Animal animal) {
		
		Query query = entityManager.createQuery("SELECT p FROM Parto p where p.cobertura.femea = :animal order by p.data desc");
		query.setParameter("animal", animal);
		query.setMaxResults(1);
		
		try{
			return (Parto) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<Parto> findAllOrderDataDesc() {
		
		Query query = entityManager.createQuery("SELECT p FROM Parto p order by p.data asc");
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Parto> findUltimos2Partos(Animal femea) {
		
		//busca os dois últimos partos do animal
		Query queryParto = entityManager.createQuery("SELECT p FROM Parto p where p.cobertura.femea = :femea order by p.data desc");
		queryParto.setMaxResults(2);
		queryParto.setParameter("femea", femea);
		
		return queryParto.getResultList();
		
	}
	
}