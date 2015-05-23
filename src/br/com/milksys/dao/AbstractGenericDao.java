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

	public void persist(E entity) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
		entityManager.persist(entity);
		entityTransaction.commit();
	}

	public void remove(E entity) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
		entityManager.remove(entity);
		entityTransaction.commit();
	}

	public E findById(K id) {
		return entityManager.find(entityClass, id);
	}
	
	public List<E> findAll(Class<E> clazz) {
		return entityManager.createNamedQuery(clazz.getSimpleName()+".findAll", (clazz)).getResultList();
	}
}