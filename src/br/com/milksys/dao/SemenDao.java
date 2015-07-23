package br.com.milksys.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Semen;

@Repository
public class SemenDao extends AbstractGenericDao<Integer, Semen> {

	public int findQuantidadeUtilizada(Semen semen) {
		
		try{
			Query query = entityManager.createQuery("SELECT sum(quantidadeDosesUtilizadas) from Cobertura c where c.semen =:semen ");
			query.setParameter("semen", semen);

			return ((Long)query.getSingleResult()).intValue();
			
		}catch(NoResultException | NullPointerException e){
			return 0;
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<Semen> findAllComEstoque() {
		
		Query query = entityManager.createQuery("SELECT s FROM Semen s "
				+ "WHERE (s.quantidade - coalesce((SELECT sum(quantidadeDosesUtilizadas) from Cobertura c where c.semen.id = s.id), 0)) > 0 ");

		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Semen> defaultSearch(String param) {
		Query query = entityManager.createQuery("SELECT s FROM Semen s left join s.fornecedor f left join s.touro t "
				+ "WHERE t.codigo like :param or t.nome like :param or "
				+ "f.nome like :param");
		query.setParameter("param", '%' + param + '%');
		return query.getResultList();
	}

}