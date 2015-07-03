package br.com.milksys.dao;

import javax.persistence.EntityTransaction;

import org.springframework.stereotype.Component;

import br.com.milksys.model.Cobertura;
import br.com.milksys.model.Parto;
import br.com.milksys.model.Servico;

@Component
public class CoberturaDao extends AbstractGenericDao<Integer, Cobertura> {

	public void removeServicoFromCobertura(Cobertura cobertura) {
		
		EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
		
        Servico servico = cobertura.getServico();
		if ( servico != null && servico.getId() > 0 ){
			cobertura.setServico(null);
			if ( cobertura.getId() > 0 ){
				entityManager.persist(cobertura);
			}
			entityManager.remove(servico);
		}
        
        entityTransaction.commit();
		
		
	}

	public void removerParto(Cobertura cobertura) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
		
        Parto parto = cobertura.getParto();
		if ( parto != null && parto.getId() > 0 ){
			
			cobertura.setParto(null);
			
			if ( cobertura.getId() > 0 ){
				entityManager.persist(cobertura);
			}
			
			entityManager.remove(parto);
			
		}
        
        entityTransaction.commit();
	}

}