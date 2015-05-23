package br.com.milksys.dao;

import java.util.List;

public interface GenericDao<K, E> {
      void persist(E entity);
      void remove(E entity);
      E findById(K id);
      List<E> findAll(Class<E> _class);
}