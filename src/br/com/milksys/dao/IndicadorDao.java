package br.com.milksys.dao;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jdk.nashorn.internal.ir.BreakableNode;

import org.springframework.stereotype.Component;

import br.com.milksys.model.Indicador;
import br.com.milksys.util.DateUtil;

@Component
public class IndicadorDao extends AbstractGenericDao<Integer, Indicador> {

	@Override
	public List<Indicador> findAll(Class<Indicador> clazz) {
		
		List<Indicador> indicadores = new ArrayList<Indicador>();
		indicadores.add(new Indicador("DIAS EM LACTAÇÃO", "DEL", getDiasEmLactacao()));
		
		return indicadores;
		
		
		/*for ( Indicador indicador : indicadores ){
			
			if ( indicador.getQuery() != null && !indicador.getQuery().isEmpty()){
				
				Query query = entityManager.createNativeQuery(indicador.getQuery());
				Object value = query.getSingleResult();
				
				if ( value != null ){
					indicador.setValorApurado( value.toString() );
				}else{
					indicador.setValorApurado("--");
				}
				
			}else{
				indicador.setValorApurado("fórmula não cadastrada");
			}
			
			
			return indicadores;
			
			
		}*/
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	private long getDiasEmLactacao(){
		
		List<Object[]> partos        = entityManager.createQuery("SELECT p.cobertura.femea.id, p.data FROM Parto p order by p.data").getResultList();
		List<Object[]> encerramentos = entityManager.createQuery("SELECT e.animal.id, e.data FROM EncerramentoLactacao e order by e.data").getResultList();
		
		long diasEmLactacao = 0;
		
		for ( Object[] parto : partos ){
			
			int parto_animal = (int)parto[0];
		    Date parto_data  = (Date)parto[1];
		    
			Date data_proximo_encerramento = null;
			int index = 0;
			
			for ( Object[] encerramento : encerramentos ){
				
				int encerramento_animal = (int)encerramento[0];
			    Date encerramento_data  = (Date)encerramento[1];
			    
			    if ( encerramento_animal == parto_animal && parto_data.before(encerramento_data) ){
			    	data_proximo_encerramento = encerramento_data;
			    	break;
			    }
			    index++;
			}
			
			encerramentos.remove(index);
			    
		    if ( data_proximo_encerramento != null ){
		    	diasEmLactacao += ChronoUnit.DAYS.between(DateUtil.asLocalDate(parto_data), DateUtil.asLocalDate(data_proximo_encerramento));
		    }else{
		    	diasEmLactacao += ChronoUnit.DAYS.between(DateUtil.asLocalDate(parto_data), LocalDate.now());
		    }
			
		}
		
		return diasEmLactacao;
		
	}
	
}