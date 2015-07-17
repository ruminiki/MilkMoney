package br.com.milksys.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.milksys.MainApp;
import br.com.milksys.model.Indicador;
import br.com.milksys.service.indicadores.AbstractCalculadorIndicador;

@Repository
public class IndicadorDao extends AbstractGenericDao<Integer, Indicador> {

	@Autowired ParametroDao parametroDao;
	
	@Override@SuppressWarnings("unchecked")
	public List<Indicador> findAll(Class<Indicador> clazz) {
		
		List<Indicador> indicadores = super.findAll(Indicador.class);

		for ( Indicador indicador : indicadores ){
			
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
		
		return indicadores;
		
	}
	
	
	/*
	 * 
		Dias pós-parto no primeiro serviço (primeira cobertura)
		O número de dias pós-parto no primeiro serviço é influenciado pelo período voluntário de espera (PVE), 
		como o próprio nome diz, por uma decisão de manejo, por isso esse índice varia muito entre os rebanhos. 
		Cada fazenda deve, a partir do final do PVE, definir o objetivo a ser alcançado para a média de dias pós-parto no primeiro serviço.
		Algumas vacas podem ser cobertas com 40 dias pós-parto, porém na maioria dos rebanhos de alta produção, 
		o máximo de fertilidade é alcançado por volta dos 60 dias pós-parto.
		http://www.milkpoint.com.br/radar-tecnico/reproducao/interpretacao-dos-indices-da-eficiencia-reprodutiva-41269n.aspx
	 */
	
	
}