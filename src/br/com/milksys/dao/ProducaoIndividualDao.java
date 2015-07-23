package br.com.milksys.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Animal;
import br.com.milksys.model.ProducaoIndividual;

@Repository
public class ProducaoIndividualDao extends AbstractGenericDao<Integer, ProducaoIndividual> {

	@SuppressWarnings("unchecked")
	public List<ProducaoIndividual> findByDate(Date data) {
		
		Query query = entityManager.createQuery("SELECT c FROM ProducaoIndividual c WHERE c.data = :data");
		query.setParameter("data", data);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ProducaoIndividual> findAllByPeriodo(Date inicio, Date fim) {
		
		Query query = entityManager.createQuery("SELECT c FROM ProducaoIndividual c WHERE c.data between :inicio and :fim order by data");
		query.setParameter("inicio", inicio);
		query.setParameter("fim", fim);
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<ProducaoIndividual> findByAnimal(Animal animal) {
		
		//recupera os registros com o preco do leite no m�s do lan�amento da produ��o
		/*String hql = "SELECT c, "
				   + "(SELECT coalesce(p.valorRecebido, p.valorMaximoPraticado) * (c.primeiraOrdenha + c.segundaOrdenha + c.terceiraOrdenha) FROM PrecoLeite p "
				   + "where p.codigoMes = month(c.data) and anoReferencia = year(c.data)) as valor "
				   + "FROM ProducaoIndividual c "
				   + "WHERE c.animal = :animal order by c.data";*/
		
		Query query = entityManager.createQuery("SELECT c FROM ProducaoIndividual c WHERE c.animal = :animal order by data");
		query.setParameter("animal", animal);
		
		return query.getResultList();
		
	}

	public ProducaoIndividual findByAnimalAndData(Animal animal, Date data) {
		
		Query query = entityManager.createQuery("SELECT c FROM ProducaoIndividual c WHERE c.animal = :animal and c.data = :data order by data");
		query.setParameter("animal", animal);
		query.setParameter("data", data);
		
		try{
			return (ProducaoIndividual)query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<ProducaoIndividual> findByAnimalPeriodo(Animal animal, Date dataInicio, Date dataFim) {
		
		Query query = entityManager.createQuery("SELECT c FROM ProducaoIndividual c WHERE c.animal = :animal and "
				+ "(c.data between :dataInicio and :dataFim or (c.data >= :dataInicio and :dataFim is null)) order by data");
		
		query.setParameter("animal", animal);
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		
		return query.getResultList();
		
	}

}