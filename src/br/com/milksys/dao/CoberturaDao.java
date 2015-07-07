package br.com.milksys.dao;

import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;

@Component
public class CoberturaDao extends AbstractGenericDao<Integer, Cobertura> {

	public void removeServicoFromCobertura(Cobertura cobertura) {
		
		try{
			EntityTransaction entityTransaction = entityManager.getTransaction();
	        entityTransaction.begin();
			
	        cobertura.setServico(null);
	        entityManager.persist(cobertura);
	       
	        /*Servico servico = cobertura.getServico();
			if ( servico != null && servico.getId() > 0 ){
				
				cobertura.setServico(null);
				entityManager.remove(servico);
				
				if ( cobertura.getId() > 0 ){
					entityManager.persist(cobertura);
				}
				
			}*/
	        
	        entityTransaction.commit();
		}catch(Exception e){
			entityManager.refresh(cobertura);
			throw e;
		}
		
	}

	public void removerParto(Cobertura cobertura) {
		
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
		
	}

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

}