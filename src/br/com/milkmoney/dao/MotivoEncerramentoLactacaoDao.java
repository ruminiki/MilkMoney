package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.MotivoEncerramentoLactacao;

@Repository
public class MotivoEncerramentoLactacaoDao extends AbstractGenericDao<Integer, MotivoEncerramentoLactacao> {

	@SuppressWarnings("unchecked")
	public List<MotivoEncerramentoLactacao> defaultSearch(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM MotivoEncerramentoLactacao r WHERE r.descricao like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

}