package br.com.milkmoney.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Lactacao;

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
	
	public long countByAnimal(Animal animal) {
		
		Query query = entityManager.createQuery("SELECT count(p) FROM Lactacao p where p.animal = :animal");
		query.setHint("org.hibernate.cacheable", "false");
		query.setParameter("animal", animal);
		
		try{
			return (long) query.getSingleResult();
		}catch ( NoResultException e ){
			return 0L;
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<Lactacao> findAllWithDurationIn(Date dataInicio, Date dataFim) {
		Query query = entityManager.createQuery("SELECT lc FROM Lactacao lc where coalesce(lc.dataFim, :dataFim) between :dataInicio and :dataFim ");
		
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		
		return query.getResultList();
	}

}