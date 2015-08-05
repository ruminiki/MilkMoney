package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.dao.ParametroDao;
import br.com.milkmoney.model.Parametro;

/**
 * Taxa de servi�o � definida como a porcentagem de vacas aptas a serem inseminadas no per�odo de 21 dias que s�o realmente inseminadas. 
 * A taxa de servi�o � reflexo da taxa de detec��o de cio, pois para a vaca ser inseminada, tem que ser detectada em cio antes. 
 * 
 * TDC - dividindo o n�mero de vacas inseminadas no per�odo de 21 dias pelo n�mero de vacas 
 * dispon�veis para serem inseminadas no mesmo per�odo.
 * 
 * http://www.milkpoint.com.br/radar-tecnico/reproducao/manejo-reprodutivo-do-rebanho-leiteiro-26245n.aspx
 * 
 * http://www.milkpoint.com.br/radar-tecnico/reproducao/estrategias-de-manejo-para-aumentar-a-eficiencia-reprodutiva-de-vacas-de-leite-28283n.aspx
 * 
 * PVE - Periodo volunt�rio de espera (dias ap�s o parto em que a vaca n�o deve ser enseminada)
 */

@Service
public class TaxaDeteccaoCio extends AbstractCalculadorIndicador{

	@Autowired private AnimalDao animalDao;
	@Autowired private ParametroDao parametroDao;
	@Autowired private CoberturaDao coberturaDao;
	
	@Override
	public BigDecimal getValue() {

		int diasIdadeMinimaParaCobertura = 0;
		try{
			//o parametro estara em meses, multiplicar por 30 para obter os dias
			diasIdadeMinimaParaCobertura = Integer.parseInt(parametroDao.findBySigla(Parametro.IDADE_MINIMA_PARA_COBERTURA)) * 30;
		}catch(Exception e){
			diasIdadeMinimaParaCobertura = 24 * 30;
		}
		
		int periodoVoluntarioEspera = 0;
		try{
			periodoVoluntarioEspera = Integer.parseInt(parametroDao.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA));
		}catch(Exception e){
			periodoVoluntarioEspera = 40;//default 40 dias
		}
		
		//vacas enseminadas ultimos 21 dias
		BigInteger vacasEnseminadas = coberturaDao.countCoberturasRealizadasUltimos21Dias();
		
		//vacas dispon�veis para serem cobertas:
		//(1) n�o vendidas, (2) n�o mortas, (3) que n�o estejam cobertas(prenhas) no per�odo, (3) n�o s�o rec�m paridas, (4) tem idade suficiente para cobertura
		BigInteger vacasDisponiveis = animalDao.countVacasDisponiveisParaCoberturaUltimos21Dias(diasIdadeMinimaParaCobertura, periodoVoluntarioEspera);
				
		if ( vacasEnseminadas.compareTo(BigInteger.ZERO) <= 0 ||
				vacasDisponiveis.compareTo(BigInteger.ZERO) <= 0 ){
			return BigDecimal.ZERO;
		}
		
		return BigDecimal.valueOf(vacasEnseminadas.divide(vacasDisponiveis).multiply(BigInteger.valueOf(100)).intValue());
		
	}
	
}