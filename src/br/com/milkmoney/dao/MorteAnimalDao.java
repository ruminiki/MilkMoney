package br.com.milkmoney.dao;

import java.util.Date;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.MorteAnimal;

@Repository
public class MorteAnimalDao extends AbstractGenericDao<Integer, MorteAnimal> {
	
	public Long countByCausa(String descricao) {
		
		Query query = entityManager.createQuery("SELECT count(*) FROM MorteAnimal m inner join m.causaMorteAnimal c WHERE c.descricao = :param");
		query.setParameter("param", descricao);
		
		return (Long) query.getSingleResult();
		
	}

	public void removeByAnimal(Animal animal) {

		Query query = entityManager.createNativeQuery("delete from morteAnimal where animal = :animal");
		query.setParameter("animal", animal);
		query.executeUpdate();
		
	}

	public MorteAnimal findByAnimalAfterDate(Date data, Animal animal) {
		Query query = entityManager.createQuery("SELECT m FROM MorteAnimal m where m.animal = :animal and m.dataMorte > :dataInicio order by m.dataMorte desc");
		query.setParameter("dataInicio", data);
		query.setParameter("animal", animal);
		query.setMaxResults(1);
		
		try{
			return (MorteAnimal) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

}