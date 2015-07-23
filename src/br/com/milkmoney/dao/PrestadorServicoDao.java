package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.PrestadorServico;

@Repository
public class PrestadorServicoDao extends AbstractGenericDao<Integer, PrestadorServico> {

	@SuppressWarnings("unchecked")
	public List<PrestadorServico> defaultSearch(String param) {
		
		Query query = entityManager.createQuery("SELECT p FROM PrestadorServico p WHERE p.nome like :param or "
				+ "p.telefonePrincipal like :param or p.email like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

}