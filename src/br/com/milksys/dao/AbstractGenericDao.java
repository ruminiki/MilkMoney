package br.com.milksys.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public abstract class AbstractGenericDao<K, E> implements GenericDao<K, E> {
	protected Class<E> entityClass;

	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public AbstractGenericDao() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[1];
		this.entityManager = Persistence.createEntityManagerFactory("MilkSys").createEntityManager();
	}
	
	public boolean persist(E entity) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		
		entityTransaction.begin();
        
        try{
        	
        	entityManager.persist(entity);
        	entityTransaction.commit();
        	
        }catch(Exception e){
        	throw e;
        }finally{
        	if ( entityTransaction.isActive() )
        		entityTransaction.rollback();
		}
        
		return true;
	}

	public boolean remove(E entity) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		
		if ( !entityTransaction.isActive() )
			entityTransaction.begin();
		
        try{
	        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
	        entityTransaction.commit();
		}catch (Exception e) {
			throw e;
		}finally{
			if ( entityTransaction.isActive() )
        		entityTransaction.rollback();
		}
        
		return true;
	}

	public E findById(K id) {
		
		E e = entityManager.find(entityClass, id);
		if ( entityManager.contains(e) ){
			entityManager.refresh(e);
		}
		return e;
		
	}
	
	public List<E> findAll(Class<E> clazz) {
		//entityManager.flush();
		/*List<E> list = entityManager.createNamedQuery(clazz.getSimpleName()+".findAll", (clazz)).getResultList();
		
		for (Iterator<E> iterator = list.iterator(); iterator.hasNext();) {
			E e = (E) iterator.next();
			if ( entityManager.contains(e) ){
				entityManager.refresh(e);
			}
		}*/
		
		return entityManager.createNamedQuery(clazz.getSimpleName()+".findAll", (clazz)).getResultList();
	}
}