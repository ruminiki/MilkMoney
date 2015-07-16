package br.com.milksys.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.MotivoVendaAnimal;

@Repository
public class MotivoVendaAnimalDao extends AbstractGenericDao<Integer, MotivoVendaAnimal> {
	
	@SuppressWarnings("unchecked")
	public List<MotivoVendaAnimal> findByDescricao(String param) {
		Query query = entityManager.createQuery("SELECT m FROM MotivoVendaAnimal m WHERE m.descricao like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
	}

}