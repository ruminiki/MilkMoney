package br.com.milksys.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.Funcionario;
import br.com.milksys.model.OcorrenciaFuncionario;

@Component
public class OcorrenciaFuncionarioDao extends AbstractGenericDao<Integer, OcorrenciaFuncionario> {

	@SuppressWarnings("unchecked")
	public List<OcorrenciaFuncionario> findByFuncionario(Funcionario funcionario) {
		
		Query query = entityManager.createQuery("SELECT c FROM OcorrenciaFuncionario c WHERE c.funcionario = :funcionario order by data");
		query.setParameter("funcionario", funcionario);
		
		return query.getResultList();
		
	}

}
