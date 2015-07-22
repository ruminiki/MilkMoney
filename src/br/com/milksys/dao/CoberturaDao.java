package br.com.milksys.dao;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.SituacaoCobertura;

@Repository
public class CoberturaDao extends AbstractGenericDao<Integer, Cobertura> {

	@SuppressWarnings("unchecked")
	public List<Cobertura> defaultSearch(String param) {
		
		Query query = entityManager.createQuery(
				"select c from Cobertura c "
				+ "left join c.touro t "
				+ "left join c.semen s "
				+ "WHERE (c.situacaoCobertura like :param or "
				+ "t.numero like :param or "
				+ "t.nome like :param or "
				+ "s.touro.nome like :param or "
				+ "c.tipoCobertura like :param) ");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
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

	public BigInteger countCoberturasRealizadasUltimos21Dias() {
		return (BigInteger) entityManager.createNativeQuery("select count(*) from cobertura c where  DATEDIFF(current_date(), c.data) between 0 and 21 ").getSingleResult();
	}

}