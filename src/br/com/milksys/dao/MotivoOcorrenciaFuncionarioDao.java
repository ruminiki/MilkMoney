package br.com.milksys.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.MotivoOcorrenciaFuncionario;

@Component
public class MotivoOcorrenciaFuncionarioDao extends AbstractGenericDao<Integer, MotivoOcorrenciaFuncionario> {

	@SuppressWarnings("unchecked")
	public List<MotivoOcorrenciaFuncionario> findByDescricao(String param) {
		Query query = entityManager.createQuery("SELECT m FROM MotivoOcorrenciaFuncionario m WHERE m.descricao like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
	}

}