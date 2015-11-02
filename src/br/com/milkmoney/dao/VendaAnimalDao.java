package br.com.milkmoney.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.VendaAnimal;

@Repository
public class VendaAnimalDao extends AbstractGenericDao<Integer, VendaAnimal> {

	public void removeByAnimal(Animal animal) {
        VendaAnimal vendaAnimal = findByAnimal(animal);
        if ( vendaAnimal != null ){
        	remove(vendaAnimal);
        }
	}
	
	public VendaAnimal findByAnimal(Animal animal){
		
		Query query = entityManager.createQuery("SELECT v FROM VendaAnimal v WHERE v.animal = :animal");
		query.setParameter("animal", animal);
		
		try{
			return ((VendaAnimal)query.getSingleResult());
		}catch(NoResultException e){
			return null;
		}
		
	}

	public VendaAnimal findByAnimalAfterDate(Date data, Animal animal) {
		
		Query query = entityManager.createQuery("SELECT va FROM VendaAnimal va where v.animal = :animal and va.dataVenda > :dataInicio order by va.dataVenda desc");
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
		Query query = entityManager.createQuery("SELECT va FROM VendaAnimal va inner join va.comprador c "
				+ "where va.animal.nome like :param or va.animal.numero like :param or "
				+ "c.nome like :param or "
				+ "va.motivoVendaAnimal.descricao like :param or "
				+ "va.destinacaoAnimal like :param order by va.dataVenda desc");
		query.setParameter("param", "%"+param+"%");
		
		return query.getResultList();
	}

	public VendaAnimal findByAnimalPeriodo(Animal animal, Date dataInicio, Date dataFim) {
		Query query = entityManager.createQuery("SELECT va FROM VendaAnimal va where va.animal = :animal and va.dataVenda between :dataInicio and :dataFim ");
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		query.setParameter("animal", animal);
		query.setMaxResults(1);
		
		try{
			return (VendaAnimal) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
}