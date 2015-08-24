package br.com.milkmoney.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.CategoriaDespesa;

@Repository
public class CategoriaDespesaDao extends AbstractGenericDao<Integer, CategoriaDespesa> {

	@SuppressWarnings("unchecked")
	public List<CategoriaDespesa> findByDescricao(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM CategoriaDespesa r WHERE r.descricao like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

	public void configuraExclusaoCategoria(CategoriaDespesa categoriaDespesa) {
		
		Query query = entityManager.createQuery("update CategoriaDespesa c set c.categoriaSuperiora = :categoriaSuperiora where c.categoriaSuperiora = :categoriaASerExcluida ");
		query.setParameter("categoriaSuperiora", categoriaDespesa.getCategoriaSuperiora());
		query.setParameter("categoriaASerExcluida", categoriaDespesa);
		query.executeUpdate();
		
	}

}