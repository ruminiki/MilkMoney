package br.com.milksys.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.Fornecedor;

@Component
public class FornecedorDao extends AbstractGenericDao<Integer, Fornecedor> {

	@SuppressWarnings("unchecked")
	public List<Fornecedor> findByNome(String param) {
		
		Query query = entityManager.createQuery("SELECT f FROM Fornecedor f WHERE f.nome like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

}