package br.com.milkmoney.dao;

import java.util.List;

public interface GenericDao<K, E> {

	boolean persist(E entity);
	boolean remove(E entity);
	E findById(Class<E> clazz, K id);
	List<E> findAll(Class<E> clazz);
	
}