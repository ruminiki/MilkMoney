package br.com.milksys.dao;

import java.util.Date;
import java.util.List;

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

	public VendaAnimal findByAnimalAfterDate(Date data, Animal animal) {
		
		Query query = entityManager.createQuery("SELECT va FROM VendaAnimal va inner join va.animaisVendidos av where av.animal = :animal and va.dataVenda > :dataInicio order by va.dataVenda desc");
		query.setParameter("dataInicio", data);
		query.setParameter("animal", animal);
		query.setMaxResults(1);
		
		try{
			return (VendaAnimal) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<VendaAnimal> defaultSearch(String param) {
		Query query = entityManager.createQuery("SELECT va FROM VendaAnimal va inner join va.animaisVendidos av inner join va.comprador c "
				+ "where av.animal.nome like :param or av.animal.numero like :param or c.nome like :param order by va.dataVenda desc");
		query.setParameter("param", "%"+param+"%");
		
		return query.getResultList();
	}
	
}