package br.com.milksys.service;

import java.util.List;

import javafx.collections.ObservableList;

public interface IService<K, E> {

    boolean save(E entity);
    boolean remove(E entity);
    E findById(K id);
    List<E> findAll();
    ObservableList<E> findAllAsObservableList();
    ObservableList<E> defaultSearch(String param);
    void validate(E entity);
    
    
   
}
