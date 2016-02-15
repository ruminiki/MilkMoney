package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.model.ValorIndicador;

@Repository
public class ValorIndicadorDao extends AbstractGenericDao<Integer, ValorIndicador> {

	@SuppressWarnings("unchecked")
	public List<ValorIndicador> findByYear(Indicador indicador, int ano) {

		Query query = entityManager.createQuery("SELECT r FROM ValorIndicador r WHERE ano = :ano and indicador = :indicador order by r.indicador.ordem");
		query.setParameter("ano", ano);
		query.setParameter("indicador", indicador);
		
		return query.getResultList();
		
	}

}