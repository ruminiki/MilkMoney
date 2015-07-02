package br.com.milksys.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.Animal;

@Component
public class AnimalDao extends AbstractGenericDao<Integer, Animal> {

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasAtivas() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a join a.situacaoAnimal s WHERE a.sexo = 'FÊMEA' and s.animalAtivo = true");
		
		return query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllReprodutoresAtivos() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a join a.situacaoAnimal s WHERE a.sexo = 'MACHO' and s.animalAtivo = true and a.finalidadeAnimal = 'REPRODUÇÃO'");
		
		return query.getResultList();
		
	}

}