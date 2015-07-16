package br.com.milksys.dao;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Animal;
import br.com.milksys.model.AnimalVendido;
import br.com.milksys.model.VendaAnimal;

@Repository
public class VendaAnimalDao extends AbstractGenericDao<Integer, VendaAnimal> {

	public void removeByAnimal(Animal animal) {

        VendaAnimal vendaAnimal = findByAnimal(animal);
        if ( vendaAnimal != null ){
        	int index = 0;
        	for ( AnimalVendido av : vendaAnimal.getAnimaisVendidos() ) {
        		
        		if ( av.getAnimal().getId() == animal.getId() ){
        			if ( vendaAnimal.getAnimaisVendidos().size() == 1 ){
                		remove(vendaAnimal);
                	}else{
                		vendaAnimal.getAnimaisVendidos().remove(index);
                		persist(vendaAnimal);
                	}        			
        		}
        		index++;
        	}
        }

	}
	
	public VendaAnimal findByAnimal(Animal animal){
		
		Query query = entityManager.createQuery("SELECT v FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal = :animal");
		query.setParameter("animal", animal);
		
		try{
			return ((VendaAnimal)query.getSingleResult());
		}catch(NoResultException e){
			return null;
		}
		
	}
	
}