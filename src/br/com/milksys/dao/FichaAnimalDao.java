package br.com.milksys.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.EncerramentoLactacao;
import br.com.milksys.model.FichaAnimal;
import br.com.milksys.model.Parto;

@Repository
public class FichaAnimalDao extends AbstractGenericDao<Integer, FichaAnimal> {

	@SuppressWarnings("unchecked")
	public List<FichaAnimal> findAllByAnimal(Animal animal) {
		
		List<FichaAnimal> fichasAnimal = new ArrayList<FichaAnimal>();
		
		Query query = entityManager.createQuery("SELECT c.id, c.data FROM Cobertura c WHERE c.femea = :animal order by c.data");
		query.setParameter("animal", animal);
		
		List<Object> result = query.getResultList();
		
		for ( int index = 0; index < result.size(); index++ ){
			Object[] o = (Object[]) result.get(index);
			fichasAnimal.add(new FichaAnimal((Date)o[1], "COBERTURA", Integer.parseInt(String.valueOf(o[0])), Cobertura.class));
		}
		
		
		query = entityManager.createQuery("SELECT c.id, c.dataConfirmacaoPrenhez, c.situacaoConfirmacaoPrenhez "
				+ "FROM Cobertura c WHERE c.femea = :animal and c.dataConfirmacaoPrenhez is not null order by c.dataConfirmacaoPrenhez");
		query.setParameter("animal", animal);
		result = query.getResultList();
		
		for ( int index = 0; index < result.size(); index++ ){
			Object[] o = (Object[]) result.get(index);
			fichasAnimal.add(new FichaAnimal((Date)o[1], 
					"DIAGNÓSTICO (" + String.valueOf(o[2]) + ")", Integer.parseInt(String.valueOf(o[0])), Cobertura.class));
		}
		

		query = entityManager.createQuery("SELECT c.id, c.dataReconfirmacaoPrenhez, c.situacaoReconfirmacaoPrenhez "
				+ "FROM Cobertura c WHERE c.femea = :animal and c.dataReconfirmacaoPrenhez is not null order by c.dataReconfirmacaoPrenhez");
		query.setParameter("animal", animal);
		result = query.getResultList();
		
		for ( int index = 0; index < result.size(); index++ ){
			Object[] o = (Object[]) result.get(index);
			fichasAnimal.add(new FichaAnimal((Date)o[1], 
					"DIAGNÓSTICO (" + String.valueOf(o[2]) + ")", Integer.parseInt(String.valueOf(o[0])), Cobertura.class));
		}
		

		query = entityManager.createQuery("SELECT c.id, c.dataRepeticaoCio "
				+ "FROM Cobertura c WHERE c.femea = :animal and c.dataRepeticaoCio is not null order by c.dataRepeticaoCio");
		query.setParameter("animal", animal);
		result = query.getResultList();
		
		for ( int index = 0; index < result.size(); index++ ){
			Object[] o = (Object[]) result.get(index);
			fichasAnimal.add(new FichaAnimal((Date)o[1], 
					"REPETIÇÃO CIO", Integer.parseInt(String.valueOf(o[0])), Cobertura.class));
		}

		
		query = entityManager.createQuery("SELECT p.id, p.data "
				+ "FROM Parto p WHERE p.cobertura.femea = :animal order by p.data");
		query.setParameter("animal", animal);
		result = query.getResultList();
		
		for ( int index = 0; index < result.size(); index++ ){
			Object[] o = (Object[]) result.get(index);
			fichasAnimal.add(new FichaAnimal((Date)o[1], "PARTO", Integer.parseInt(String.valueOf(o[0])), Parto.class));
		}
		
		
		query = entityManager.createQuery("SELECT e.id, e.data "
				+ "FROM EncerramentoLactacao e WHERE e.animal = :animal order by e.data");
		query.setParameter("animal", animal);
		result = query.getResultList();
		
		for ( int index = 0; index < result.size(); index++ ){
			Object[] o = (Object[]) result.get(index);
			fichasAnimal.add(new FichaAnimal((Date)o[1], "ENCERRAMENTO LACTAÇÃO", Integer.parseInt(String.valueOf(o[0])), EncerramentoLactacao.class));
		}
		
		Collections.sort(fichasAnimal, new Comparator<FichaAnimal>() {
	        @Override
	        public int compare(FichaAnimal f1, FichaAnimal f2){
	            return  f1.getData().compareTo(f2.getData());
	        }
	    });
		
		return fichasAnimal;
		
	}

}