package br.com.milksys.service.indicadores;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.AnimalDao;
import br.com.milksys.dao.CoberturaDao;
import br.com.milksys.dao.ParametroDao;
import br.com.milksys.model.Parametro;

/**
 * Taxa de serviço é definida como a porcentagem de vacas aptas a serem inseminadas no período de 21 dias que são realmente inseminadas. 
 * A taxa de serviço é reflexo da taxa de detecção de cio, pois para a vaca ser inseminada, tem que ser detectada em cio antes. 
 * 
 * TDC - dividindo o número de vacas inseminadas no período de 21 dias pelo número de vacas 
 * disponíveis para serem inseminadas no mesmo período.
 * 
 * http://www.milkpoint.com.br/radar-tecnico/reproducao/manejo-reprodutivo-do-rebanho-leiteiro-26245n.aspx
 * 
 * http://www.milkpoint.com.br/radar-tecnico/reproducao/estrategias-de-manejo-para-aumentar-a-eficiencia-reprodutiva-de-vacas-de-leite-28283n.aspx
 * 
 * PVE - Periodo voluntário de espera (dias após o parto em que a vaca não deve ser enseminada)
 */

@Service
public class TaxaDeteccaoCio extends AbstractCalculadorIndicador{

	@Autowired AnimalDao animalDao;
	@Autowired ParametroDao parametroDao;
	@Autowired CoberturaDao coberturaDao;
	
	@Override
	public String getValue() {

		int diasIdadeMinimaParaCobertura = 0;
		try{
			//o parametro estara em meses, multiplicar por 30 para obter os dias
			diasIdadeMinimaParaCobertura = Integer.parseInt(parametroDao.findBySigla(Parametro.IDMC)) * 30;
		}catch(Exception e){
			diasIdadeMinimaParaCobertura = 24 * 30;
		}
		
		int periodoVoluntarioEspera = 0;
		try{
			periodoVoluntarioEspera = Integer.parseInt(parametroDao.findBySigla(Parametro.PVE));
		}catch(Exception e){
			periodoVoluntarioEspera = 40;//default 40 dias
		}
		
		//vacas enseminadas ultimos 21 dias
		BigInteger vacasEnseminadas = coberturaDao.countCoberturasRealizadasUltimos21Dias();
		
		//vacas disponíveis para serem cobertas:
		//(1) não vendidas, (2) não mortas, (3) que não estejam cobertas(prenhas) no período, (3) não são recém paridas, (4) tem idade suficiente para cobertura
		BigInteger vacasDisponiveis = animalDao.countVacasDisponiveisParaCoberturaUltimos21Dias(diasIdadeMinimaParaCobertura, periodoVoluntarioEspera);
				
		if ( vacasEnseminadas.compareTo(BigInteger.ZERO) <= 0 ||
				vacasDisponiveis.compareTo(BigInteger.ZERO) <= 0 ){
			return String.valueOf(BigDecimal.ZERO);
		}
		
		return String.valueOf(vacasEnseminadas.divide(vacasDisponiveis).multiply(BigInteger.valueOf(100)));
		
	}
	
}
