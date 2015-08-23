package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Compra;

@Repository
public class CompraDao extends AbstractGenericDao<Integer, Compra> {

	@SuppressWarnings("unchecked")
	public List<Compra> defaultSearch(String param) {
		
		Query query = entityManager.createQuery("SELECT t FROM AquisicaoInsumo t "
				+ "join t.insumosAdquiridos i WHERE i.descricao like :param or "
				+ "t.fornecedor.nome like :param");
		
		query.setParameter("param", '%' + param + '%');
		query.setHint("org.hibernate.cacheable", "false");
		
		return query.getResultList();
		
	}

}