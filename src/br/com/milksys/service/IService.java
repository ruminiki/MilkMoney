package br.com.milksys.service;

import java.util.List;


public interface IService<K, E> {

    void save(E entity);
    void remove(E entity);
    E findById(K id);
    List<E> findAll();
	
}
