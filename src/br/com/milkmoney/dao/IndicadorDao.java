package br.com.milkmoney.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.service.indicadores.AbstractCalculadorIndicador;

@Repository
public class IndicadorDao extends AbstractGenericDao<Integer, Indicador> {

	@Autowired ParametroDao parametroDao;
	
	@Override
	@Transactional
	public List<Indicador> findAll(Class<Indicador> clazz) {
		
		List<Indicador> indicadores = super.findAll(Indicador.class);

		for ( Indicador indicador : indicadores ){
			refreshValorApurado(indicador);
		}
		
		return indicadores;
		
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	private void refreshValorApurado(Indicador indicador){
		try {
			
			if ( indicador.getClasseCalculo() != null ){
				Class<AbstractCalculadorIndicador> calculadorClass = (Class<AbstractCalculadorIndicador>) Class.forName(indicador.getClasseCalculo());
				AbstractCalculadorIndicador calculador = (AbstractCalculadorIndicador) MainApp.getBean(calculadorClass);
				indicador.setValorApurado( calculador.getValue() );
				this.persist(indicador);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@Transactional
	public Indicador findById(Class<Indicador> clazz, Integer id) {
		Indicador indicador = super.findById(clazz, id);
		refreshValorApurado(indicador);
		return indicador;
	}
	
	
}