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
	O número de dias pós-parto no primeiro serviço é influenciado pelo período voluntário de espera (PVE), 
	como o próprio nome diz, por uma decisão de manejo, por isso esse índice varia muito entre os rebanhos. 
	Cada fazenda deve, a partir do final do PVE, definir o objetivo a ser alcançado para a média de dias pós-parto no primeiro serviço.
	
	Algumas vacas podem ser cobertas com 40 dias pós-parto, porém na maioria dos rebanhos de alta produção, 
	o máximo de fertilidade é alcançado por volta dos 60 dias pós-parto.
	
	O ideal é que a liberação das vacas para o início das coberturas seja feita depois de um exame pelo veterinário, 
	para que as vacas com problemas sejam diagnosticadas e tratadas. O início das inseminações nos animais com infecção 
	uterina é atrasado até que a infecção seja eliminada e o útero esteja saudável.
	
	A média de dias pós-parto no primeiro serviço também é influenciado pelo retorno a 
	ciclicidade pós-parto e pela eficiência da observação de cio.
	
	O cálculo dos dias pós-parto no primeiro serviço é feito da seguinte forma:
	
	Cálculo do número de dias do parto até o primeiro serviço para as vacas inseminadas
	A média de dias pós-parto no primeiro serviço do rebanho é feito pela soma dos dias pós-parto no 
	primeiro serviço de cada vaca, dividido pelo número de vacas do rebanho.
	
	Após determinada a média, deve ser feita a diferença desta para o objetivo.
	
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
