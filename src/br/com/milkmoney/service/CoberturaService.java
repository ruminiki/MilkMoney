package br.com.milkmoney.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.Parametro;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.CoberturaValidation;

@Service
public class CoberturaService implements IService<Integer, Cobertura>{

	@Autowired private CoberturaDao dao;
	@Autowired private PartoService partoService;
	@Autowired private ParametroService parametroService;

	@Override
	@Transactional
	public boolean save(Cobertura cobertura) {
		
		CoberturaValidation.validate(cobertura);
		CoberturaValidation.validaSituacaoAnimal(cobertura.getFemea());
		CoberturaValidation.validaFemeaSelecionada(cobertura, findByAnimal(cobertura.getFemea(), new Date()), Integer.valueOf(parametroService.findBySigla(Parametro.IDADE_MINIMA_PARA_COBERTURA)));
		CoberturaValidation.validaSobreposicaoCoberturas(cobertura, dao.findLastCoberturaAnimal(cobertura.getFemea()));
		
		return dao.persist(cobertura);
	}
	
	@Transactional
	public void registrarParto(Cobertura cobertura) {
		
		try{
			cobertura.setSituacaoCobertura(SituacaoCobertura.PARIDA);
			dao.persist(cobertura);
		}catch(Exception e){
			cobertura.setParto(null);
			throw new RuntimeException(e);
		}
		
	}

	@Transactional
	public void removerParto(Cobertura cobertura) {
		
		try{
			cobertura.setParto(null);
			cobertura.setSituacaoCobertura(cobertura.getSituacaoConfirmacaoPrenhez());
			dao.persist(cobertura);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	@Transactional
	public void desfazerConfirmacaoPrenhez(Cobertura cobertura) {
		
		try{
			cobertura.setDataConfirmacaoPrenhez(null);
			cobertura.setMetodoConfirmacaoPrenhez(null);
			cobertura.setObservacaoConfirmacaoPrenhez(null);
			cobertura.setSituacaoConfirmacaoPrenhez(null);
			cobertura.setSituacaoCobertura(SituacaoCobertura.NAO_CONFIRMADA);
			dao.persist(cobertura);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	@Transactional
	public void desfazerRegistroAborto(Cobertura cobertura) {
		try{
			cobertura.setAborto(null);
			cobertura.setSituacaoCobertura(cobertura.getSituacaoConfirmacaoPrenhez());
			dao.persist(cobertura);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	public ObservableList<Cobertura> findByPeriodo(Date dataInicio, Date dataFim){
		return FXCollections.observableArrayList(dao.findAllCoberturasPeriodo(dataInicio, dataFim));
	}
	
	public List<Cobertura> findByAnimal(Animal animal, Date data){
		return dao.findByAnimal(animal, data);
	}
	
	@Override
	@Transactional
	public boolean remove(Cobertura cobertura) {
		return dao.remove(cobertura);
	}
	
	@Override
	public Cobertura findById(Integer id) {
		return dao.findById(Cobertura.class, id);
	}

	@Override
	public List<Cobertura> findAll() {
		return dao.findAll(Cobertura.class);
	}
	
	public ObservableList<Cobertura> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Cobertura.class));
	}
	
	@Override
	public ObservableList<Cobertura> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	public ObservableList<Cobertura> defaultSearch(String param, Animal animal) {
		return FXCollections.observableArrayList(dao.defaultSearch(param, animal));
	}
	
	public ObservableList<Cobertura> defaultSearch(String param, Date dataInicio, Date dataFim) {
		return FXCollections.observableArrayList(dao.defaultSearch(param, dataInicio, dataFim));
	}
	
	@Override
	public void validate(Cobertura cobertura) {
		CoberturaValidation.validate(cobertura);
	}
	
	public Cobertura findLastCoberturaAnimal(Animal animal){
		return dao.findLastCoberturaAnimal(animal);
	}

	public Cobertura findCoberturaAtivaByAnimal(Animal animal){
		return dao.findCoberturaAtivaByAnimal(animal);
	}

	//------------FICHA ANIMAL
	public Date getDataUltimaCoberturaAnimal(Animal animal) {
		Cobertura cobertura = dao.findLastCoberturaAnimal(animal);
		return cobertura != null ? cobertura.getData() : null;
	}
	
	public int getNumeroServicosPorConcepcao(Animal animal, Date data) {

		Parto parto = partoService.findLastParto(animal, data);
		if ( parto != null ){
			List<Cobertura> coberturas = dao.findAllAfterDate(animal, parto.getData());
			
			//se o animal tem coberturas após o parto, conta elas, senão tenta buscar do parto anterior
			if ( coberturas.size() > 0 ){
				return coberturas.size();
			}
			
		}
		
		List<Cobertura> coberturas = dao.findByAnimal(animal, data);
		int index = 0;
		
		for (Cobertura cobertura : coberturas){
			//conta quantas coberturas não tiveram parto, antes da última
			//se a ultima cobertura é a que gerou o parto ou aborto, conta ela e continua voltando nas coberturas até encontrar
			//uma cobertura parida/abortada, que não deverá ser contada
			if ( index == 0 ){
				if ( cobertura.getSituacaoCobertura().matches(SituacaoCobertura.PARIDA + "|" + SituacaoCobertura.ABORTADA) ){
					index++;
					continue;
				}
			}
			
			if ( cobertura.getSituacaoCobertura().matches(SituacaoCobertura.PARIDA + "|" + SituacaoCobertura.ABORTADA) ){
				break;
			}
			
			index++;
			
		}
		
		return index;
		
	}
	
/*	public BigDecimal getMediaIntervaloPrimeiraCoberturaAtePrenhez(Animal animal) {
		
		List<Cobertura> coberturas = dao.findByAnimal(animal);
		int index = 0;
		
		Date dataPrimeiraCobertura = null;
		int intervaloEntreCoberturas = 0;
		
		for (Cobertura cobertura : coberturas){
			
			//conta quantas coberturas não tiveram parto, antes da última
			if ( !cobertura.getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
				index++;
				if ( dataPrimeiraCobertura == null ){
					dataPrimeiraCobertura = cobertura.getData();
				}else{
					intervaloEntreCoberturas += (int) ChronoUnit.DAYS.between(DateUtil.asLocalDate(dataPrimeiraCobertura), DateUtil.asLocalDate(cobertura.getData()));
				}
			}
			else break;
		}
		
		if ( intervaloEntreCoberturas > 0 ){
			return BigDecimal.valueOf(intervaloEntreCoberturas).divide(BigDecimal.valueOf(index), 2, RoundingMode.HALF_EVEN);	
		}
		
		return BigDecimal.ZERO;
		
	}*/

	/*
	 * Se a última cobertura estava prenha - 
	 */
	public Date getProximoServico(Animal animal) {
		Cobertura cobertura = dao.findLastCoberturaAnimal(animal);
		
		if ( cobertura != null ){
			
			int periodoVoluntarioEspera = Integer.parseInt(parametroService.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA));
			
			if ( cobertura.getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
				return DateUtil.asDate(DateUtil.asLocalDate(cobertura.getParto().getData()).plusDays(periodoVoluntarioEspera));
			}
			
			if ( cobertura.getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) ){
				return DateUtil.asDate(DateUtil.asLocalDate(cobertura.getPrevisaoParto()).plusDays(periodoVoluntarioEspera));
			}
			
			if ( cobertura.getSituacaoCobertura().equals(SituacaoCobertura.VAZIA) ){
				if ( cobertura.getDataConfirmacaoPrenhez() != null )
					return DateUtil.asDate(DateUtil.asLocalDate(cobertura.getDataConfirmacaoPrenhez()).plusDays(21));
			}
			
			if ( cobertura.getSituacaoCobertura().equals(SituacaoCobertura.ABORTADA) ){
				if ( cobertura.getAborto() != null )
					return DateUtil.asDate(DateUtil.asLocalDate(cobertura.getAborto().getData()).plusDays(21));
			}
		}else{
			//se o animal nunca teve coberturas verifica a idade
			String idadeMinimaCobertura = parametroService.findBySigla(Parametro.IDADE_MINIMA_PARA_COBERTURA);
			
			if ( idadeMinimaCobertura != null ){
				try{
					long idadeMinima = Long.parseLong(idadeMinimaCobertura);
					//o animal já deveria ter sido coberto
					if ( (animal.getIdade() - idadeMinima) >= 0 ){
						return DateUtil.asDate(LocalDate.now().minusMonths(animal.getIdade() - idadeMinima));
					}else{
						//o próximo serviço é futuro
						return DateUtil.asDate(LocalDate.now().plusMonths(idadeMinima - animal.getIdade()));
					}
				}catch(Exception e){
					System.out.println("Parâmetro idade mínima para cobertura cadastrado no formato errado.");
				}
			}
		}
		
		return null;
	}

	/*
	 * Para o cálculo do dias em aberto devemos considerar o número de dias do último parto até:
	 * A data da concepção das vacas gestantes
	 * A data da última cobertura das vacas ainda não confirmadas gestantes
	 * Ou a data em que o cálculo foi realizado.
	 */
	public int getDiasEmAberto(Animal animal, Date data) {
		Parto ultimoParto = partoService.findLastParto(animal, data);
		int diasEmAberto = 0;
		if ( ultimoParto != null ){
			
			List<Cobertura> coberturasAposParto = dao.findAllAfterDate(animal, ultimoParto.getData());
			
			for ( Cobertura c : coberturasAposParto ){
				
				if ( c != null && DateUtil.before(c.getData(), data) ){
					//A data da cobertura que gerou a concepção
					if ( c.getSituacaoConfirmacaoPrenhez().matches(SituacaoCobertura.PRENHA + "|" + SituacaoCobertura.NAO_CONFIRMADA) ){
						diasEmAberto = (int) ChronoUnit.DAYS.between(DateUtil.asLocalDate(ultimoParto.getData()), DateUtil.asLocalDate(c.getData()));
					}else{
						//a data atual para vacas vazias
						if ( c.getSituacaoConfirmacaoPrenhez().equals(SituacaoCobertura.VAZIA) ){
							diasEmAberto = (int) ChronoUnit.DAYS.between(DateUtil.asLocalDate(ultimoParto.getData()), DateUtil.asLocalDate(data));
						}
					}
				}else{
					diasEmAberto = (int) ChronoUnit.DAYS.between(DateUtil.asLocalDate(ultimoParto.getData()), DateUtil.asLocalDate(data));
				}
				
			}
			
			if ( coberturasAposParto == null || coberturasAposParto.size() <= 0 ){
				//o animal não teve cobertura após o parto
				diasEmAberto = (int) ChronoUnit.DAYS.between(DateUtil.asLocalDate(ultimoParto.getData()), DateUtil.asLocalDate(data));
			}
			
		}
		
		return diasEmAberto;
	}

	public List<Cobertura> findAllNaoConfirmadas() {
		return dao.findAllNaoConfirmadas();
	}

}
