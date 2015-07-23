package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Servico;

@Repository
public class ServicoDao extends AbstractGenericDao<Integer, Servico> {

	@SuppressWarnings("unchecked")
	public List<Servico> defaultSearch(String param) {
		Query query = entityManager.createQuery("SELECT s FROM Servico s "
				+ "left join s.prestadorServico p "
				+ "WHERE s.descricao like :param or "
				+ "p.nome like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
	}

}