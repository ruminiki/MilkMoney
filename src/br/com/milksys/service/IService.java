package br.com.milksys.service;

import java.util.List;

public interface IService<K, E> {

    boolean save(E entity);
    boolean remove(E entity);
    E findById(K id);
    List<E> findAll();
    void validate(E entity);
   
}
