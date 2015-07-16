package br.com.milksys.dao;

import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.SituacaoCobertura;

@Repository
public class CoberturaDao extends AbstractGenericDao<Integer, Cobertura> {

	@SuppressWarnings("unchecked")
	public List<Cobertura> findAllByNumeroNomeAnimal(String param) {
		Query query = entityManager.createQuery("SELECT c FROM Cobertura c inner join c.femea a WHERE a.nome like :param or a.numero like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
	}
	
	public void removeServicoFromCobertura(Cobertura cobertura) {
		
		try{
			EntityTransaction entityTransaction = entityManager.getTransaction();
	        entityTransaction.begin();
			
	        cobertura.setServico(null);
	        entityManager.persist(cobertura);
	       
	        entityTransaction.commit();
		}catch(Exception e){
			entityManager.refresh(cobertura);
			throw e;
		}
		
	}

/*	public void removerParto(Cobertura cobertura) {
		
		try{
			EntityTransaction entityTransaction = entityManager.getTransaction();
	        entityTransaction.begin();
			
			if ( cobertura.getId() > 0 && cobertura.getParto() != null && cobertura.getParto().getId() > 0 ){
				
					cobertura.setParto(null);
					entityManager.persist(cobertura);
				
			}else{
				cobertura.setParto(null);
			}
	        
	        entityTransaction.commit();
		}catch(Exception e ){
			entityManager.refresh(cobertura);
			throw e;
		}
		
	}*/

	@SuppressWarnings("unchecked")
	public List<Cobertura> findByAnimal(Animal femea) {
		Query query = entityManager.createQuery("SELECT c FROM Cobertura c WHERE c.femea = :femea");
		query.setParameter("femea", femea);
		
		return query.getResultList();
	}

	public int findQuantidadeDosesSemenUtilizadasNaCobertura(Cobertura entity) {
		
		Query query = entityManager.createQuery("SELECT quantidadeDosesUtilizadas from Cobertura c where c.id =:id ");
		query.setParameter("id", entity.getId());
		
		try{
			return ((Integer)query.getSingleResult()).intValue();
		}catch(NoResultException e){
			return 0;
		}
		
	}

	public Cobertura findCoberturaAtivaByAnimal(Animal animal) {
		
		Query query = entityManager.createQuery("SELECT c FROM Cobertura c WHERE c.femea = :femea and "
				+ "c.situacaoCobertura in ('" + SituacaoCobertura.INDEFINIDA + "','" + SituacaoCobertura.PRENHA + "')");
		query.setParameter("femea", animal);
		query.setMaxResults(1);
		
		try{
			return ((Cobertura)query.getSingleResult());
		}catch(NoResultException e){
			return null;
		}
		
	}

	public Cobertura findLastCoberturaByAnimal(Animal femea) {
		
		Query query = entityManager.createQuery("SELECT c FROM Cobertura c WHERE c.femea = :femea order by c.data desc");
		query.setParameter("femea", femea);
		query.setMaxResults(1);
		
		try{
			return ((Cobertura)query.getSingleResult());
		}catch(NoResultException e){
			return null;
		}
		
	}

}