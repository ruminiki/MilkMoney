package br.com.milksys.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Propriedade;

@Repository
public class PropriedadeDao extends AbstractGenericDao<Integer, Propriedade> {

	@SuppressWarnings("unchecked")
	public List<Propriedade> findByDescricao(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM Propriedade r WHERE r.descricao like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

}