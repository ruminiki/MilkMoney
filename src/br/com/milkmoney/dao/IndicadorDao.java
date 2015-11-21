package br.com.milkmoney.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.service.indicadores.AbstractCalculadorIndicador;

@Repository
public class IndicadorDao extends AbstractGenericDao<Integer, Indicador> {

	@Autowired ParametroDao parametroDao;
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Indicador refreshValorApurado(Indicador indicador){
		try {
			
			if ( indicador.getClasseCalculo() != null ){
				Class<AbstractCalculadorIndicador> calculadorClass = (Class<AbstractCalculadorIndicador>) Class.forName(indicador.getClasseCalculo());
				AbstractCalculadorIndicador calculador = (AbstractCalculadorIndicador) MainApp.getBean(calculadorClass);
				indicador.setValorApurado( calculador.getValue() );
				this.persist(indicador);
				return indicador;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	@Transactional
	public Indicador findById(Class<Indicador> clazz, Integer id) {
		Indicador indicador = super.findById(clazz, id);
		refreshValorApurado(indicador);
		return indicador;
	}

	@Transactional@SuppressWarnings("unchecked")
	public List<Indicador> findAllIndicadoresZootecnicos(boolean refreshValorApurado) {
		
		Query query = entityManager.createQuery("SELECT i FROM Indicador i WHERE tipo = 'Z' order by ordem");
		List<Indicador> indicadores = query.getResultList();

		if ( refreshValorApurado ){
			for ( Indicador indicador : indicadores ){
				refreshValorApurado(indicador);
			}
		}
		
		return indicadores;
		
	}
	
	@Transactional@SuppressWarnings("unchecked")
	public List<Indicador> findAllQuantitativosRebanho(boolean refreshValorApurado) {
		
		Query query = entityManager.createQuery("SELECT i FROM Indicador i WHERE tipo = 'R' order by ordem");
		List<Indicador> indicadores = query.getResultList();
		
		if ( refreshValorApurado ){
			for ( Indicador indicador : indicadores ){
				refreshValorApurado(indicador);
			}
		}
		
		return indicadores;
		
	}
	
	@Override @Transactional
	public List<Indicador> findAll(Class<Indicador> clazz) {
		List<Indicador> indicadores = new ArrayList<Indicador>();
		indicadores.addAll(findAllIndicadoresZootecnicos(true));
		indicadores.addAll(findAllQuantitativosRebanho(true));
		return indicadores;
	}
	
	
}