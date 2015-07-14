package br.com.milksys.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.Animal;
import br.com.milksys.model.MorteAnimal;

@Component
public class MorteAnimalDao extends AbstractGenericDao<Integer, MorteAnimal> {
	
	public Long countByCausa(String descricao) {
		
		Query query = entityManager.createQuery("SELECT count(*) FROM MorteAnimal m inner join m.causaMorteAnimal c WHERE c.descricao = :param");
		query.setParameter("param", descricao);
		
		return (Long) query.getSingleResult();
		
	}

	public void removeByAnimal(Animal animal) {

        entityManager.getTransaction().begin();
		Query query = entityManager.createNativeQuery("delete from morteAnimal where animal = :animal");
		query.setParameter("animal", animal);
		query.executeUpdate();
		entityManager.getTransaction().commit();
		
	}

}