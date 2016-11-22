package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.ConfiguracaoIndicador;
import br.com.milkmoney.model.Indicador;

@Repository
public class ConfiguracaoIndicadorDao extends AbstractGenericDao<Integer, ConfiguracaoIndicador> {

	@SuppressWarnings("unchecked")
	public List<ConfiguracaoIndicador> defaultSearch(Object[] param) {

		Query query = entityManager.createQuery("SELECT r FROM ConfiguracaoIndicador r WHERE "
				+ "(r.indicador.descricao like :param or r.objetivo like :param) and ano = :ano order by r.indicador.ordem");
		query.setParameter("param", '%' + String.valueOf(param[0]) + '%');
		query.setParameter("ano", Integer.parseInt(String.valueOf(param[1])));
		return query.getResultList();
		
	}

	public ConfiguracaoIndicador findByYear(Indicador indicador, int ano) {

		Query query = entityManager.createQuery("SELECT r FROM ConfiguracaoIndicador r WHERE ano = :ano and r.indicador = :indicador order by r.indicador.ordem");
		query.setParameter("ano", ano);
		query.setParameter("indicador", indicador);
		
		return (ConfiguracaoIndicador) query.getSingleResult();
		
	}

	@SuppressWarnings("unchecked")
	public List<ConfiguracaoIndicador> findByYear(int ano) {

		Query query = entityManager.createQuery("SELECT r FROM ConfiguracaoIndicador r WHERE ano = :ano order by r.indicador.ordem");
		query.setParameter("ano", ano);
		
		return query.getResultList();
	}

}