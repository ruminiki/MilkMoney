package br.com.milkmoney.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import br.com.milkmoney.dao.MorteAnimalDao;
import br.com.milkmoney.dao.PartoDao;
import br.com.milkmoney.dao.VendaAnimalDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.model.MorteAnimal;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.VendaAnimal;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.PartoValidation;

@Service
public class PartoService implements IService<Integer, Parto>{

	@Autowired private PartoDao dao;
	@Autowired private LactacaoService lactacaoService;
	@Autowired private VendaAnimalDao vendaAnimalDao;
	@Autowired private MorteAnimalDao morteAnimalDao;

	@Override
	@Transactional
	public boolean save(Parto parto) {
		throw new NotImplementedException();
	}

	@Override
	@Transactional
	public boolean remove(Parto parto) {
		throw new NotImplementedException();
	}

	@Override
	public Parto findById(Integer id) {
		return dao.findById(Parto.class, id);
	}

	@Override
	public List<Parto> findAll() {
		return dao.findAll(Parto.class);
	}
	
	public ObservableList<Parto> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Parto.class));
	}
	
	@Override
	public ObservableList<Parto> defaultSearch(String param) {
		return null;
	}

	@Override
	public void validate(Parto parto) {
		PartoValidation.validate(parto);
		PartoValidation.validaEncerramentoLactacao(parto, lactacaoService.findLastBeforeDate(parto.getCobertura().getFemea(), parto.getCobertura().getData()));
	}

	public Parto findLastParto(Animal animal, Date data) {
		return dao.findLastParto(animal, data);
	}

	//------FICHA ANIMAL
	public int countByAnimal(Animal animal) {
		return (int) dao.countByAnimal(animal);
	}
	
	public int countCriasByAnimalAndSexo(Animal animal, String sexo){
		return (int) dao.countCriasByAnimalAndSexo(animal, sexo);
	}
	
	public int contaDiasLactacaoParto(Parto parto, Date data){
		
		BigDecimal diasEmLactacao = BigDecimal.ZERO;
		
		Lactacao lactacao = parto.getLactacao();
		
		if ( lactacao != null ){
			diasEmLactacao = diasEmLactacao.add(BigDecimal.valueOf(lactacao.getDiasLactacao(data)));
		}else{
			
			//Procura registro venda animal ap�s o parto
			VendaAnimal vendaAnimal = vendaAnimalDao.findByAnimalAfterDate(parto.getData(), parto.getCobertura().getFemea());
			
			if ( vendaAnimal != null ){
				long diasEntreVendaEInicioPeriodo = ChronoUnit.DAYS.between(DateUtil.asLocalDate(parto.getData()), DateUtil.asLocalDate(vendaAnimal.getDataVenda()));
				if ( diasEntreVendaEInicioPeriodo > 0 ){//a lacta��o avan�ou pelo per�odo
					diasEmLactacao = diasEmLactacao.add(BigDecimal.valueOf(diasEntreVendaEInicioPeriodo));
				}
			}
			
			//Procura registro morte animal ap�s o �ltimo parto
			MorteAnimal morteAnimal = morteAnimalDao.findByAnimalAfterDate(parto.getData(), parto.getCobertura().getFemea());
			if ( morteAnimal != null ){
				long diasEntreMorteEInicioPeriodo = ChronoUnit.DAYS.between(DateUtil.asLocalDate(parto.getData()), DateUtil.asLocalDate(morteAnimal.getDataMorte()));
				if ( diasEntreMorteEInicioPeriodo > 0 ){//a lacta��o avan�ou pelo per�odo
					diasEmLactacao = diasEmLactacao.add(BigDecimal.valueOf(diasEntreMorteEInicioPeriodo));
				}
			}
			
		}
		
		if ( diasEmLactacao.compareTo(BigDecimal.ZERO) <= 0 ){
			//se retornou zero � porque o ultimo parto n�o teve encerramento da lacta��o
			//e o animal n�o foi vendido nem est� morto.
			//Nesse caso utilizar a data corrente para c�lculo dos dias em lacta��o
			diasEmLactacao = BigDecimal.valueOf(ChronoUnit.DAYS.between(DateUtil.asLocalDate(parto.getData()), DateUtil.asLocalDate(data)));
		}
		
		return diasEmLactacao.intValue();
		
	}

	public int getDiasEmLactacao(Animal animal, Date dataFim) {
		List<Parto> partos = dao.findByAnimal(animal);
		int diasEmLactacao = 0;
		
		for ( Parto parto : partos ){
			diasEmLactacao += contaDiasLactacaoParto(parto, dataFim);
		}
		if ( partos != null && partos.size() > 0 )
			diasEmLactacao = BigDecimal.valueOf(diasEmLactacao).divide(BigDecimal.valueOf(partos.size()), 2, RoundingMode.HALF_EVEN).intValue();
		return diasEmLactacao;
	}

	public int getIntervaloEntrePartos(Animal animal, Date data) {
		
		List<Parto> partos = dao.findUltimos2PartosBeforeDate(animal, data);
		int intervaloEntrePartos = 0;
		
		if ( partos != null && partos.size() == 2 ){
			//soma o intervalo de meses entre um e outro
			intervaloEntrePartos = BigDecimal.valueOf(ChronoUnit.MONTHS.between(DateUtil.asLocalDate(partos.get(1).getData()), 
					DateUtil.asLocalDate(partos.get(0).getData()))).intValue();
		}
		
		return intervaloEntrePartos;
		
	}

	public List<Parto> findByAnimal(Animal animal) {
		return dao.findByAnimal(animal);
	}
	
}
