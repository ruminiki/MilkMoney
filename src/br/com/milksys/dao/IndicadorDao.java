package br.com.milksys.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.Indicador;

@Component
public class IndicadorDao extends AbstractGenericDao<Integer, Indicador> {

	@Override
	public List<Indicador> findAll(Class<Indicador> clazz) {
		
		List<Indicador> indicadores = super.findAll(clazz);
		
		for ( Indicador indicador : indicadores ){
			
			if ( indicador.getQuery() != null && !indicador.getQuery().isEmpty()){
				Query query = entityManager.createNativeQuery(indicador.getQuery());
				
				if ( query.getSingleResult() != null ){
					indicador.setValorApurado( query.getSingleResult().toString() );
				}else{
					indicador.setValorApurado("--");
				}
				
			}else{
				indicador.setValorApurado("f�rmula n�o cadastrada");
			}
			
		}
		
		return indicadores;
		
	}

}