package br.com.milkmoney.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.service.indicadores.AbstractCalculadorIndicador;

@Repository
public class IndicadorDao extends AbstractGenericDao<Integer, Indicador> {

	@Autowired ParametroDao parametroDao;
	
	@Override
	public List<Indicador> findAll(Class<Indicador> clazz) {
		
		List<Indicador> indicadores = super.findAll(Indicador.class);

		for ( Indicador indicador : indicadores ){
			
			refreshValorApurado(indicador);
			
		}
		
		return indicadores;
		
	}
	
	@SuppressWarnings("unchecked")
	private void refreshValorApurado(Indicador indicador){
		try {
			
			if ( indicador.getClasseCalculo() != null ){
				Class<AbstractCalculadorIndicador> calculadorClass = (Class<AbstractCalculadorIndicador>) Class.forName(indicador.getClasseCalculo());
				AbstractCalculadorIndicador calculador = (AbstractCalculadorIndicador) MainApp.getBean(calculadorClass);
				indicador.setValorApurado( calculador.getValue() );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Indicador findById(Class<Indicador> clazz, Integer id) {
		Indicador indicador = super.findById(clazz, id);
		refreshValorApurado(indicador);
		return indicador;
	}
	
	
}