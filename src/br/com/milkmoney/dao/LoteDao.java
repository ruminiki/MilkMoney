package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Lote;

@Repository
public class LoteDao extends AbstractGenericDao<Integer, Lote> {

	@SuppressWarnings("unchecked")
	public List<Lote> defaultSearch(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM Lote r "
				+ "WHERE r.descricao like :param or "
				+ "r.finalidadeLote.descricao like :param ");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Lote> findByAnimal(Animal animal) {
		Query query = entityManager.createQuery("SELECT r FROM Lote r join r.animais a WHERE a = :animal and r.ativo = 'SIM' ");
		query.setParameter("animal", animal);
		
		return query.getResultList();
	}

}