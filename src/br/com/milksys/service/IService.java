package br.com.milksys.service;

import java.util.List;

import br.com.milksys.dao.GenericDao;

public interface IService<K, E> {

    boolean save(E entity);
    boolean remove(E entity);
    E findById(K id);
    List<E> findAll();
}
