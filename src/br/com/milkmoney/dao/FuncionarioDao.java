package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Funcionario;

@Repository
public class FuncionarioDao extends AbstractGenericDao<Integer, Funcionario> {

	@SuppressWarnings("unchecked")
	public List<Funcionario> defaultSearch(String param) {
		
		Query query = entityManager.createQuery("SELECT f FROM Funcionario f WHERE f.nome like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}
	
	
}