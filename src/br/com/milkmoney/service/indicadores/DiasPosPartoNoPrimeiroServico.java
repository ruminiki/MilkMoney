package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.dao.PartoDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.util.DateUtil;

/**
	O n�mero de dias p�s-parto no primeiro servi�o � influenciado pelo per�odo volunt�rio de espera (PVE), 
	como o pr�prio nome diz, por uma decis�o de manejo, por isso esse �ndice varia muito entre os rebanhos. 
	Cada fazenda deve, a partir do final do PVE, definir o objetivo a ser alcan�ado para a m�dia de dias p�s-parto no primeiro servi�o.
	
	Algumas vacas podem ser cobertas com 40 dias p�s-parto, por�m na maioria dos rebanhos de alta produ��o, 
	o m�ximo de fertilidade � alcan�ado por volta dos 60 dias p�s-parto.
	
	O ideal � que a libera��o das vacas para o in�cio das coberturas seja feita depois de um exame pelo veterin�rio, 
	para que as vacas com problemas sejam diagnosticadas e tratadas. O in�cio das insemina��es nos animais com infec��o 
	uterina � atrasado at� que a infec��o seja eliminada e o �tero esteja saud�vel.
	
	A m�dia de dias p�s-parto no primeiro servi�o tamb�m � influenciado pelo retorno a 
	ciclicidade p�s-parto e pela efici�ncia da observa��o de cio.
	
	O c�lculo dos dias p�s-parto no primeiro servi�o � feito da seguinte forma:
	
	C�lculo do n�mero de dias do parto at� o primeiro servi�o para as vacas inseminadas
	A m�dia de dias p�s-parto no primeiro servi�o do rebanho � feito pela soma dos dias p�s-parto no 
	primeiro servi�o de cada vaca, dividido pelo n�mero de vacas do rebanho.
	
	Ap�s determinada a m�dia, deve ser feita a diferen�a desta para o objetivo.
	
	< 18 dias = sem problemas
	19 a 26 dias = problemas moderados
	> 26 dias = problemas severos
	http://www.milkpoint.com.br/radar-tecnico/reproducao/interpretacao-dos-indices-da-eficiencia-reprodutiva-41269n.aspx
 * 
 * @author ruminiki
 *
 */


@Service
public class DiasPosPartoNoPrimeiroServico extends AbstractCalculadorIndicador{

	@Autowired private PartoDao partoDao;
	@Autowired private AnimalDao animalDao;
	@Autowired private CoberturaDao coberturaDao;
	
	@Override
	public String getValue() {
		BigDecimal diasEntrePartoEPrimeiroServico = BigDecimal.ZERO;
		
		List<Animal> animais = animalDao.findAnimaisComParto();
		
		for ( Animal animal : animais ){
			
			Parto parto = partoDao.findLastParto(animal);
			if ( parto != null ){
				Cobertura cobertura = coberturaDao.findFirstAfterDate(animal, parto.getData());
				diasEntrePartoEPrimeiroServico = diasEntrePartoEPrimeiroServico.add(
						BigDecimal.valueOf(ChronoUnit.DAYS.between(DateUtil.asLocalDate(parto.getData()), DateUtil.asLocalDate(cobertura.getData()))));
			}
			
		}
		
		if ( diasEntrePartoEPrimeiroServico.compareTo(BigDecimal.ZERO) > 0 ){
			diasEntrePartoEPrimeiroServico = diasEntrePartoEPrimeiroServico.divide(BigDecimal.valueOf(animais.size()), 2, RoundingMode.HALF_EVEN);
		}
		
		return String.valueOf(diasEntrePartoEPrimeiroServico);
		
	}
	
}
