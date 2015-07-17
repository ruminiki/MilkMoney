package br.com.milksys.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Parametro;

@Repository
public class ParametroDao extends AbstractGenericDao<Integer, Parametro> {

	public String findBySigla(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM Parametro r WHERE r.sigla = :param");
		query.setParameter("param", param);
		query.setMaxResults(1);
		
		try{
			return ((Parametro) query.getSingleResult()).getValor();
		}catch(NoResultException e){
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<Parametro> findByDescricaoSigla(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM Parametro r WHERE r.sigla like :param or r.descricao like :param ");
		query.setParameter("param", '%' + param + '%');
		
		return (List<Parametro>)query.getResultList();
		
	}

}