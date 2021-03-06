package br.com.milkmoney.service;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.dao.ParametroDao;
import br.com.milkmoney.dao.PartoDao;
import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.Limit;
import br.com.milkmoney.model.Parametro;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.AnimalValidation;

@Service
public class AnimalService implements IService<Integer, Animal>{

	@Autowired private AnimalDao dao;
	@Autowired private PartoDao partoDao;
	@Autowired private CoberturaDao coberturaDao;
	@Autowired private ParametroDao parametroDao;
	@Autowired private FichaAnimalService fichaAnimalService;
	
	public static final String FILTER_EFICIENCIA_REPRODUTIVA = "FILTER_EFICIENCIA_REPRODUTIVA";
	public static final String FILTER_SITUACAO_ANIMAL        = "FILTER_SITUACAO_ANIMAL";
	public static final String FILTER_SITUACAO_COBERTURA     = "FILTER_SITUACAO_COBERTURA";
	public static final String FILTER_LOTE                   = "FILTER_LOTE";
	public static final String FILTER_SEXO                   = "FILTER_SEXO";
	public static final String FILTER_RACA                   = "FILTER_RACA";
	public static final String FILTER_IDADE_DE               = "FILTER_IDADE_DE";
	public static final String FILTER_IDADE_ATE              = "FILTER_IDADE_ATE";
	public static final String FILTER_DIAS_POS_PARTO         = "FILTER_DIAS_POS_PARTO";
	public static final String FILTER_DIAS_POS_COBERTURA     = "FILTER_DIAS_POS_COBERTURA";
	public static final String FILTER_NUMERO_PARTOS          = "FILTER_NUMERO_PARTOS";
	public static final String FILTER_COBERTAS 			     = "FILTER_COBERTAS";
	public static final String FILTER_SECAR_EM_X_DIAS        = "FILTER_SECAR_EM_X_DIAS";
	public static final String FILTER_FINALIDADE_ANIMAL      = "FILTER_FINALIDADE_ANIMAL";
	public static final String FILTER_NUMERO_SERVICOS        = "FILTER_NUMERO_SERVICOS";
	
	@Override
	@Transactional
	public boolean save(Animal entity) {
		AnimalValidation.validate(entity);
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(Animal entity) {
		
		if ( entity.isNascimentoCadastrado() ){
			throw new ValidationException("Valida��o", "O animal n�o pode ser exclu�do pois ele est� associado a um nascimento. "
					+ "Remova o parto que deu origem ao animal e ele tamb�m ser� exclu�do.");
		}
		
		return dao.remove(entity);
	}

	@Override
	public Animal findById(Integer id) {
		return dao.findById(Animal.class, id);
	}

	@Override
	public List<Animal> findAll() {
		Instant start = Instant.now();
		List<Animal> list = dao.findAll(Animal.class);
		Instant end = Instant.now();
		System.out.println("findAll: " + Duration.between(start, end));
		return list;
	}

	public ObservableList<Animal> findAllAsObservableList() {
		Instant start = Instant.now();
		ObservableList<Animal> list = FXCollections.observableArrayList(findAll());
		Instant end = Instant.now();
		System.out.println("findAllAsObservableList: " + Duration.between(start, end));
		return list;
	}
	
	@Override
	public ObservableList<Animal> defaultSearch(String param, int limit) {
		Instant start = Instant.now();
		ObservableList<Animal> result = FXCollections.observableArrayList(dao.defaultSearch(param, limit));
		Instant end = Instant.now();
		System.out.println("defaultSearch: " + Duration.between(start, end));
		return result;
	}
	
	public ObservableList<Animal> findAllFemeasAtivasAsObservableList(Date data, int limit) {
		Instant start = Instant.now();
		ObservableList<Animal> result = FXCollections.observableArrayList(dao.findAllFemeasAtivas(data, limit));
		Instant end = Instant.now();
		System.out.println("findAllFemeasAtivasAsObservableList: " + Duration.between(start, end));
		return result;
	}
	
	public List<Animal> findAllFemeasAtivas(Date data, int limit) {
		Instant start = Instant.now();
		List<Animal> result = dao.findAllFemeasAtivas(data, limit);
		Instant end = Instant.now();
		System.out.println("findAllFemeasAtivas: " + Duration.between(start, end));
		return result;
	}

	@Override
	public void validate(Animal entity) {
		AnimalValidation.validate(entity);
	}

	public BigInteger countAnimaisEmLactacao(Date data) {
		return dao.countAllFemeasEmLactacao(data);
	}
	
	public BigInteger countAllFemeasEmLactacao(Date data) {
		return dao.countAllFemeasEmLactacao(data);
	}
	
	public BigInteger countAllVacasAtivas(Date data) {
		return dao.countAllVacasAtivas(data);
	}
	
	public BigInteger countAllFemeasSecas(Date data) {
		return dao.countAllFemeasSecas(data);
	}
	
	public BigInteger countAllNovilhas(Date data) {
		int idadeMinima = 0;
		
		try{
			idadeMinima = Integer.parseInt(parametroDao.findBySigla(Parametro.IDADE_MAXIMA_ANIMAL_CONSIDERADO_BEZERRO));
		}catch(Exception e){
			idadeMinima = 0;
		}
		
		return dao.countAllNovilhasIdadeAcimaXMeses(idadeMinima, data);
	}
	
	public BigInteger countAllBezerras(Date data) {
		int idadeMinima = 0;
		
		try{
			idadeMinima = Integer.parseInt(parametroDao.findBySigla(Parametro.IDADE_MAXIMA_ANIMAL_CONSIDERADO_BEZERRO));
		}catch(Exception e){
			idadeMinima = 0;
		}
		
		return dao.countAllNovilhasIdadeAteXMeses(idadeMinima, data);
	}
	
	/*
	 * Vacas dispon�veis para serem cobertas:
	 * (1) n�o vendidas, 
	 * (2) n�o mortas, 
	 * (3) que n�o estejam cobertas(prenhas)
	 * (3) n�o s�o rec�m paridas,  
	 * (4) tem idade suficiente para cobertura
	 */
	public List<Animal> findAnimaisDisponiveisParaCobertura(Date dataInicio, Date dataFim) {
			
		List<Animal> result = new ArrayList<Animal>();
		
		int idadeMinimaCobertura = 0;
		try{
			//o parametro estara em meses, multiplicar por 30 para obter os dias
			idadeMinimaCobertura = Integer.parseInt(parametroDao.findBySigla(Parametro.IDADE_MINIMA_PARA_COBERTURA));
		}catch(Exception e){
			idadeMinimaCobertura = 13;
		}
		
		int periodoVoluntarioEspera = 0;
		try{
			periodoVoluntarioEspera = Integer.parseInt(parametroDao.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA));
		}catch(Exception e){
			periodoVoluntarioEspera = 40;//default 40 dias
		}
		
		//busca animais que tinha idade suficiente e n�o estavam mortos nem vendidos
		List<Animal> animais = dao.findAnimaisAtivosComIdadeParaServicoNoPeriodo(idadeMinimaCobertura, dataInicio, dataFim, Limit.UNLIMITED);
		
		for ( Animal animal : animais ){
			//buscar ultima cobertura antes data inicio
			Cobertura cobertura = coberturaDao.findFirstBeforeDate(animal, dataInicio);
			if ( cobertura != null ){
				//se a cobertura tiver parto, verificar se a data do parto + pve fica dentro ou antes do periodo inicial
				if ( cobertura.getParto() != null ){
					if ( DateUtil.asLocalDate(cobertura.getParto().getData()).plusDays(periodoVoluntarioEspera).compareTo(DateUtil.asLocalDate(dataFim)) <= 0 ){
						result.add(animal);
					}
					continue;
				}
				//se n�o tiver parto verificar se estiver vazia ou abortada
				if ( cobertura.getSituacaoCobertura().matches(SituacaoCobertura.VAZIA + "|" + SituacaoCobertura.ABORTADA) ){
					//se sim, somar a data da cobertura + 18 dias e verificar se est� dentro ou antes do periodo
					if ( DateUtil.asLocalDate(cobertura.getData()).plusDays(18).compareTo(DateUtil.asLocalDate(dataFim)) <= 0 ){
						result.add(animal);
					}
					continue;
				}
				
				//se n�o tiver confirma��o, verificar se a data inicial + o tempo de gesta��o + pve fica antes ou dentro do periodo
				if ( DateUtil.asLocalDate(cobertura.getData()).plusDays(animal.getRaca().getDuracaoGestacao() + periodoVoluntarioEspera).compareTo(DateUtil.asLocalDate(dataFim)) <= 0 ){
					result.add(animal);
				}
				
				continue;
				
			}else{
				result.add(animal);
			}
			
		}
		
		return result;
	}

	public int getIdadePrimeiroParto(Animal animal) {
		Parto parto = partoDao.findFirstParto(animal);
		int idadePrimeiroParto = 0;
		if ( parto != null ){
			idadePrimeiroParto = (int) ChronoUnit.MONTHS.between(DateUtil.asLocalDate(animal.getDataNascimento()), DateUtil.asLocalDate(parto.getData()));
		}
		return idadePrimeiroParto;
	}
	
	public int getIdadePrimeiraCobertura(Animal animal) {
		Cobertura cobertua = coberturaDao.findFirstCobertura(animal);
		int idadePrimeiroCobertura = 0;
		if ( cobertua != null ){
			idadePrimeiroCobertura = (int) ChronoUnit.MONTHS.between(DateUtil.asLocalDate(animal.getDataNascimento()), DateUtil.asLocalDate(cobertua.getData()));
		}
		return idadePrimeiroCobertura;
	}

	public Animal findByNumero(String numero) {
		return dao.findByNumero(numero);
	}

	public Animal findMae(Animal animal) {
		return dao.findMae(animal);
	}
	/**
	 * Retorna um Touro (insemina��o artificial) ou Animal (monta natural)
	 * @param animal
	 * @return
	 */
	public Object findPai(Animal animal) {
		return dao.findPai(animal);
	}

	public List<Animal> fill(HashMap<String, String> params) {
		//se buscar pela efici�ncia, atualiza o c�lculo antes
		if ( params.get(AnimalService.FILTER_EFICIENCIA_REPRODUTIVA) != null ){
			fichaAnimalService.generateFichaAnimal(findAll(),fichaAnimalService.getField(FichaAnimalService.EFICIENCIA_REPRODUTIVA_ANIMAL));
		}
		return FXCollections.observableArrayList(dao.superSearch(params, Limit.UNLIMITED));
	}

	public String getImagePath(Animal animal) {
		return dao.getImagePath(animal);
	}

}
