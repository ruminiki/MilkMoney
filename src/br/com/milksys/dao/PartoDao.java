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
	
	public long countByAnimal(Animal animal) {
		
		Query query = entityManager.createQuery("SELECT count(p) FROM Parto p where p.cobertura.femea = :animal");
		query.setHint("org.hibernate.cacheable", "false");
		query.setParameter("animal", animal);
		
		try{
			return (long) query.getSingleResult();
		}catch ( NoResultException e ){
			return 0L;
		}
		
	}
	
	public long countCriasByAnimalAndSexo(Animal animal, String sexo) {
		
		Query query = entityManager.createQuery("SELECT count(c) FROM Parto p inner join p.crias c where p.cobertura.femea = :animal and c.sexo = :sexo");
		query.setHint("org.hibernate.cacheable", "false");
		query.setParameter("animal", animal);
		query.setParameter("sexo", sexo);
		
		try{
			return (long) query.getSingleResult();
		}catch ( NoResultException e ){
			return 0L;
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<Parto> findAllOrderByDataDesc() {
		
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

	@SuppressWarnings("unchecked")
	public List<Parto> findByAnimal(Animal femea) {
		//busca os dois últimos partos do animal
		Query queryParto = entityManager.createQuery("SELECT p FROM Parto p where p.cobertura.femea = :femea order by p.data desc");
		queryParto.setParameter("femea", femea);
		
		return queryParto.getResultList();
	}

	public Parto findFirstParto(Animal animal) {
		
		Query query = entityManager.createQuery("SELECT p FROM Parto p where p.cobertura.femea = :animal order by p.data asc");
		query.setParameter("animal", animal);
		query.setMaxResults(1);
		
		try{
			return (Parto) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}
	
}