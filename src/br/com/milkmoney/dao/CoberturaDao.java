package br.com.milkmoney.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.SituacaoCobertura;

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
				+ "c.tipoCobertura like :param) order by c.data desc ");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Cobertura> findByAnimal(Animal femea) {
		Query query = entityManager.createQuery("SELECT c FROM Cobertura c WHERE c.femea = :femea order by c.data desc");
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
				+ "c.situacaoCobertura in ('" + SituacaoCobertura.NAO_CONFIRMADA + "','" + SituacaoCobertura.PRENHA + "')");
		query.setParameter("femea", animal);
		query.setMaxResults(1);
		
		try{
			return ((Cobertura)query.getSingleResult());
		}catch(NoResultException e){
			return null;
		}
		
	}

	public Cobertura findLastCoberturaAnimal(Animal femea) {
		
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

	//recupera a primeira cobertura após uma determinada data
	public Cobertura findFirstAfterDate(Animal femea, Date data) {
		
		Query query = entityManager.createQuery("SELECT c FROM Cobertura c WHERE c.femea = :femea and c.data > :data order by c.data");
		query.setParameter("femea", femea);
		query.setParameter("data", data);
		query.setMaxResults(1);
		
		try{
			return ((Cobertura)query.getSingleResult());
		}catch(NoResultException e){
			return null;
		}
	}
	
	public Cobertura findFirstBeforeDate(Animal femea, Date data) {
		Query query = entityManager.createQuery("SELECT c FROM Cobertura c WHERE c.femea = :femea and c.data < :data order by c.data desc");
		query.setParameter("femea", femea);
		query.setParameter("data", data);
		query.setMaxResults(1);
		
		try{
			return ((Cobertura)query.getSingleResult());
		}catch(NoResultException e){
			return null;
		}
	}
	
	//recupera a primeira cobertura após uma determinada data
	@SuppressWarnings("unchecked")
	public List<Cobertura> findAllAfterDate(Animal femea, Date data) {
		
		Query query = entityManager.createQuery("SELECT c FROM Cobertura c WHERE c.femea = :femea and c.data > :data order by c.data");
		query.setParameter("femea", femea);
		query.setParameter("data", data);
		
		return query.getResultList();
		
	}
	
	public Cobertura findFirstCobertura(Animal animal) {
		
		Query query = entityManager.createQuery("SELECT c FROM Cobertura c where c.femea = :animal order by c.data asc");
		query.setParameter("animal", animal);
		query.setMaxResults(1);
		
		try{
			return (Cobertura) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}

	public Long countAllWithPrevisaoPartoIn(Date dataInicio, Date dataFim) {
		
		Query query = entityManager.createQuery("SELECT count(c) FROM Cobertura c "
				+ "WHERE c.previsaoParto between :dataInicio and :dataFim and "
				+ "c.situacaoCobertura in ('" + SituacaoCobertura.NAO_CONFIRMADA + "','" + SituacaoCobertura.PRENHA + "') "
				+ "order by c.data");
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		
		return (Long) query.getSingleResult();
		
	}

	public Long countAllWithPrevisaoEncerramentoIn(Date dataInicio, Date dataFim) {
		
		Query query = entityManager.createQuery("SELECT count(c) FROM Cobertura c "
				+ "WHERE (adddate(c.data, 210) between :dataInicio and :dataFim or datediff(c.previsaoParto, :dataFim) between 0 and 60 ) and "
				+ "c.situacaoCobertura in ('" + SituacaoCobertura.NAO_CONFIRMADA + "','" + SituacaoCobertura.PRENHA + "') "  
				+ "order by c.data");
		
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		
		return (Long) query.getSingleResult();
	}


}