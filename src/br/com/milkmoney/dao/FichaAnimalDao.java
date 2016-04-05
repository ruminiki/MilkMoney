package br.com.milkmoney.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;

@Repository
public class FichaAnimalDao extends AbstractGenericDao<Integer, FichaAnimal> {

	@SuppressWarnings("unchecked")
	public List<FichaAnimal> findAllByAnimal(Animal animal) {
		
		List<FichaAnimal> fichasAnimal = new ArrayList<FichaAnimal>();
		
		Query query = entityManager.createQuery("SELECT coalesce(p.dataRealizacao, p.dataAgendada) as data, "
				+ "p.tipoProcedimento.descricao FROM Procedimento p inner join p.animais a "
				+ "WHERE a = :animal order by 1");
		query.setParameter("animal", animal);
		
		List<Object> result = query.getResultList();
		
		for ( int index = 0; index < result.size(); index++ ){
			if ( result.get(index) != null ){
				Object[] o = (Object[]) result.get(index);
				fichasAnimal.add(new FichaAnimal(new Date(((java.sql.Date)o[0]).getTime()), String.valueOf(o[1]) ));
			}
		}
		
		query = entityManager.createQuery("SELECT c.data FROM Cobertura c WHERE c.femea = :animal order by c.data");
		query.setParameter("animal", animal);
		
		result = query.getResultList();
		
		for ( int index = 0; index < result.size(); index++ ){
			if ( result.get(index) != null )
				fichasAnimal.add(new FichaAnimal(new Date(((java.sql.Date)result.get(index)).getTime()), "COBERTURA"));
		}

		
		query = entityManager.createQuery("SELECT c.dataConfirmacaoPrenhez, c.situacaoConfirmacaoPrenhez "
				+ "FROM Cobertura c WHERE c.femea = :animal and c.dataConfirmacaoPrenhez is not null order by c.dataConfirmacaoPrenhez");
		query.setParameter("animal", animal);
		result = query.getResultList();
		
		for ( int index = 0; index < result.size(); index++ ){
			if ( result.get(index) != null ){
				Object[] o = (Object[]) result.get(index);
				fichasAnimal.add(new FichaAnimal((Date)o[0], "DIAGNÓSTICO (" + String.valueOf(o[1]) + ")"));
			}
		}
		
		query = entityManager.createQuery("SELECT p.data FROM Parto p WHERE p.cobertura.femea = :animal order by p.data");
		query.setParameter("animal", animal);
		result = query.getResultList();
		
		for ( int index = 0; index < result.size(); index++ ){
			if ( result.get(index) != null )
				fichasAnimal.add(new FichaAnimal(new Date(((java.sql.Date)result.get(index)).getTime()), "PARTO"));
		}
		
		
		query = entityManager.createQuery("SELECT e.dataInicio FROM Lactacao e WHERE e.animal = :animal order by e.dataInicio");
		query.setParameter("animal", animal);
		result = query.getResultList();
		
		for ( int index = 0; index < result.size(); index++ ){
			if ( result.get(index) != null )
				fichasAnimal.add(new FichaAnimal(new Date(((java.sql.Date)result.get(index)).getTime()), "INICIO LACTAÇÃO"));
		}
		
		
		query = entityManager.createQuery("SELECT e.dataFim FROM Lactacao e WHERE e.animal = :animal order by e.dataFim");
		query.setParameter("animal", animal);
		result = query.getResultList();
		
		for ( int index = 0; index < result.size(); index++ ){
			if ( result.get(index) != null )
				fichasAnimal.add(new FichaAnimal(new Date(((java.sql.Date)result.get(index)).getTime()), "ENCERRAMENTO LACTAÇÃO"));
		}
		
		
		Collections.sort(fichasAnimal, new Comparator<FichaAnimal>() {
	        @Override
	        public int compare(FichaAnimal f1, FichaAnimal f2){
	            return  f1.getData().compareTo(f2.getData());
	        }
	    });
		
		return fichasAnimal;
		
	}

	public FichaAnimal findFichaAnimal(Animal animal) {
		Query query = entityManager.createQuery("SELECT f FROM FichaAnimal f WHERE f.animal = :animal");
		query.setParameter("animal", animal);
		
		try{
			return (FichaAnimal) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
		
	}

}