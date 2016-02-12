package br.com.milkmoney.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Parto;

@Repository
public class PartoDao extends AbstractGenericDao<Integer, Parto> {

	public Parto findLastParto(Animal animal, Date data) {
		
		Query query = entityManager.createQuery("SELECT p FROM Parto p where p.cobertura.femea = :animal and p.data <= :data order by p.data desc");
		query.setParameter("animal", animal);
		query.setParameter("data", data);
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
	public List<Parto> findUltimos2PartosBeforeDate(Animal femea, Date data) {
		
		//busca os dois últimos partos do animal
		Query queryParto = entityManager.createQuery("SELECT p FROM Parto p where p.cobertura.femea = :femea and p.data <= :data order by p.data desc");
		queryParto.setParameter("femea", femea);
		queryParto.setParameter("data", data);
		queryParto.setMaxResults(2);
		
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

	public Parto findPartoPeriodo(Animal animal, Date dataInicio, Date dataFim) {
		Query query = entityManager.createQuery("SELECT p FROM Parto p where p.cobertura.femea = :animal and p.data between :dataInicio and :dataFim");
		query.setParameter("animal", animal);
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		query.setMaxResults(1);
		
		try{
			return (Parto) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Parto> findAllOrderByDataDesc(Date data) {
		Query query = entityManager.createQuery("SELECT p FROM Parto p where p.data <= :data order by p.data desc");
		query.setParameter("data", data);
		return query.getResultList();
	}
	
}