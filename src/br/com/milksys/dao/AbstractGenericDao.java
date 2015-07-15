package br.com.milksys.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

@Component
public abstract class AbstractGenericDao<K, E> implements GenericDao<K, E> {
	protected Class<E> entityClass;

	@PersistenceContext(unitName = "milkMoney")
	protected EntityManager entityManager;
	
	public boolean persist(E entity) {
        try{
        	
        	entityManager.merge(entity);
        	
        }catch(Exception e){
        	throw e;
        }
		return true;
	}

	public boolean remove(E entity) {
        try{
	        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
	        entityManager.flush();
		}catch (Exception e) {
			throw e;
		}
        
		return true;
	}

	public E findById(Class<E> clazz, K id) {
		return entityManager.find(clazz, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<E> findAll(Class<E> clazz) {
		Query query = entityManager.createNamedQuery(clazz.getSimpleName()+".findAll", (clazz));
		return query.getResultList();
	}
	
}