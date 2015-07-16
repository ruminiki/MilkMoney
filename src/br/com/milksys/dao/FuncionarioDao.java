package br.com.milksys.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Funcionario;

@Repository
public class FuncionarioDao extends AbstractGenericDao<Integer, Funcionario> {

	@SuppressWarnings("unchecked")
	public List<Funcionario> findByNome(String param) {
		
		Query query = entityManager.createQuery("SELECT f FROM Funcionario f WHERE f.nome like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

}