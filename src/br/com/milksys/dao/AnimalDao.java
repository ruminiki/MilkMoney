package br.com.milksys.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.Animal;
import br.com.milksys.model.FinalidadeAnimal;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.SituacaoAnimal;
import br.com.milksys.model.SituacaoCobertura;

@Component
public class AnimalDao extends AbstractGenericDao<Integer, Animal> {

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasAtivas() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = 'FÊMEA'");
		
		return query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllReprodutoresAtivos() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = 'MACHO' and a.finalidadeAnimal = '" + FinalidadeAnimal.REPRODUCAO + "'");
		
		return query.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllMachosAtivos() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = 'MACHO'");
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasCobertas() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Cobertura c inner join c.femea a "
				+ "WHERE c.situacaoCobertura in ('" + SituacaoCobertura.INDEFINIDA + "','" + SituacaoCobertura.PRENHA + "')");
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasNaoCobertas() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Cobertura c inner join c.femea a "
				+ "WHERE c.situacaoCobertura not in ('" + SituacaoCobertura.INDEFINIDA + "','" + SituacaoCobertura.PRENHA + "')");
		
		List<Animal> animaisNaoCobertos = query.getResultList();
		
		query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a not in (select c.femea from Cobertura c) and a.sexo = '" + Sexo.FEMEA + "'");

		animaisNaoCobertos.addAll(query.getResultList());
		
		return animaisNaoCobertos;
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasLactacao30Dias() {
		
		Query query = entityManager.createQuery("SELECT a FROM Parto p inner join p.cobertura.femea a "
				+ "WHERE (p.data - current_date()) <= 30 ");
		
		return query.getResultList();
		
	}
	
	public String getSituacaoAnimal(Animal animal) {
		
		//verifica se o animal está vendido
		Query query = entityManager.createQuery("SELECT v FROM VendaAnimal v inner join v.animaisVendidos a WHERE a = :animal limit 1");
		query.setParameter("animal", animal);
		
		try{
			if ( query.getSingleResult() != null ){
				return SituacaoAnimal.VENDIDO;
			}
		}catch(NoResultException e){
			
		}
		
		//verifica se o animal está morto
		query = entityManager.createQuery("SELECT m FROM MorteAnimal m WHERE m.animal = :animal limit 1");
		query.setParameter("animal", animal);
		
		try{
			if ( query.getSingleResult() != null ){
				return SituacaoAnimal.MORTO;
			}
		}catch(NoResultException e){
			
		}
		
		//verifica se o animal está seco
		/*query = entityManager.createQuery("SELECT m FROM MorteAnimal m WHERE m.animal = :animal limit 1");
		query.setParameter("animal", animal);
		
		try{
			if ( query.getSingleResult() != null ){
				return SituacaoAnimal.MORTO;
			}
		}catch(NoResultException e){
			
		}*/
		
		//verifica se o animal está em lactação
		/*query = entityManager.createQuery("SELECT m FROM MorteAnimal m WHERE m.animal = :animal limit 1");
		query.setParameter("animal", animal);
		
		try{
			if ( query.getSingleResult() != null ){
				return SituacaoAnimal.MORTO;
			}
		}catch(NoResultException e){
			
		}*/
		
		return SituacaoAnimal.EM_CRIACAO;
		
	}
	
}