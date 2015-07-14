package br.com.milksys.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;

public abstract class AbstractGenericDao<K, E> implements GenericDao<K, E> {
	protected Class<E> entityClass;

	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public AbstractGenericDao() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[1];
		this.entityManager = Persistence.createEntityManagerFactory("MilkSys").createEntityManager();
		getSession().setCacheMode(CacheMode.REFRESH);
		this.entityManager.getEntityManagerFactory().getCache().evictAll();
	}
	
	public Session getSession(){
		return entityManager.unwrap(Session.class);
	}
	
	public boolean persist(E entity) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		
		entityTransaction.begin();
        
        try{
        	entityManager.unwrap(Session.class).setFlushMode(FlushMode.COMMIT);
        	entityManager.persist(entity);
        	entityTransaction.commit();
        	//entityManager.clear();
        	entityManager.refresh(entity);
        }catch(Exception e){
        	throw e;
        }finally{
        	if ( entityTransaction.isActive() )
        		entityTransaction.rollback();
		}
        
		return true;
	}

	public boolean remove(E entity) {
		
		entityManager.getTransaction().begin();
		
        try{
        	entityManager.unwrap(Session.class).setFlushMode(FlushMode.COMMIT);
	        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
	        entityManager.getTransaction().commit();
	        entityManager.clear();
		}catch (Exception e) {
			entityManager.clear();
			throw e;
		}finally{
			if ( entityManager.getTransaction().isActive() ){
				entityManager.getTransaction().rollback();
			}
		}
        
		return true;
	}

	public E findById(K id) {
		
		entityManager.unwrap(Session.class).setFlushMode(FlushMode.ALWAYS);
		E e = entityManager.find(entityClass, id);
		
		if ( entityManager.contains(e) ){
			entityManager.refresh(e);
		}
		
		return e;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<E> findAll(Class<E> clazz) {
		
		entityManager.unwrap(Session.class).setFlushMode(FlushMode.ALWAYS);
		getSession().getSessionFactory().getCache().evictAllRegions();
		getSession().getSessionFactory().getCache().evictCollectionRegions();
		
		Query query = entityManager.createNamedQuery(clazz.getSimpleName()+".findAll", (clazz));
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
		
	}
	
	public EntityManager getEntityManager(){
		return this.entityManager;
	}
	
}