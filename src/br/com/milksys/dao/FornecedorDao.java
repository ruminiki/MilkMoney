package br.com.milksys.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Fornecedor;

@Repository
public class FornecedorDao extends AbstractGenericDao<Integer, Fornecedor> {

	@SuppressWarnings("unchecked")
	public List<Fornecedor> defaultSearch(String param) {
		
		Query query = entityManager.createQuery("SELECT f FROM Fornecedor f WHERE f.nome like :param or "
				+ "f.email like :param or "
				+ "f.telefonePrincipal like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

}