package br.com.milkmoney.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.model.ValorIndicador;
import br.com.milkmoney.service.indicadores.AbstractCalculadorIndicador;

@Repository
public class IndicadorDao extends AbstractGenericDao<Integer, Indicador> {

	@Autowired ParametroDao parametroDao;
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ValorIndicador refreshValorApurado(ValorIndicador valorIndicador, Date data){
		try {
			
			if ( valorIndicador.getIndicador().getClasseCalculo() != null ){
				
				Class<AbstractCalculadorIndicador> calculadorClass = (Class<AbstractCalculadorIndicador>) Class.forName(valorIndicador.getIndicador().getClasseCalculo());
				AbstractCalculadorIndicador calculador = (AbstractCalculadorIndicador) MainApp.getBean(calculadorClass);
				
				valorIndicador.setValor( calculador.getValue(data) );
				
				return valorIndicador;
				
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
		return indicador;
	}

	@Transactional@SuppressWarnings("unchecked")
	public List<Indicador> findAllIndicadoresZootecnicos(boolean refreshValorApurado, Date data) {
		
		Query query = entityManager.createQuery("SELECT i FROM Indicador i WHERE tipo = 'Z' order by ordem");
		List<Indicador> indicadores = query.getResultList();

		if ( refreshValorApurado ){
			for ( Indicador indicador : indicadores ){
				//refreshValorApurado(indicador, data);
			}
		}
		
		return indicadores;
		
	}
	
	@Transactional@SuppressWarnings("unchecked")
	public List<Indicador> findAllQuantitativosRebanho(boolean refreshValorApurado, Date data) {
		
		Query query = entityManager.createQuery("SELECT i FROM Indicador i WHERE tipo = 'R' order by ordem");
		List<Indicador> indicadores = query.getResultList();
		
		if ( refreshValorApurado ){
			for ( Indicador indicador : indicadores ){
				//refreshValorApurado(indicador, data);
			}
		}
		
		return indicadores;
		
	}
	
}