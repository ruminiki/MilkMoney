package br.com.milkmoney.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.dao.FichaAnimalDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.service.fichaAnimal.AbstractFichaAnimal;
import br.com.milkmoney.service.fichaAnimal.DataUltimaCobertura;
import br.com.milkmoney.service.fichaAnimal.DataUltimoParto;
import br.com.milkmoney.service.fichaAnimal.DiasEmAbertoAnimal;
import br.com.milkmoney.service.fichaAnimal.DiasEmLactacaoAnimal;
import br.com.milkmoney.service.fichaAnimal.EficienciaReprodutivaAnimal;
import br.com.milkmoney.service.fichaAnimal.EficienciaTempoProducao;
import br.com.milkmoney.service.fichaAnimal.EncerramentoLactacao;
import br.com.milkmoney.service.fichaAnimal.IdadePrimeiraCobertura;
import br.com.milkmoney.service.fichaAnimal.IdadePrimeiroParto;
import br.com.milkmoney.service.fichaAnimal.IntervaloEntrePartosAnimal;
import br.com.milkmoney.service.fichaAnimal.LoteAnimal;
import br.com.milkmoney.service.fichaAnimal.MediaProducaoUltimaLactacao;
import br.com.milkmoney.service.fichaAnimal.NumeroCriasFemea;
import br.com.milkmoney.service.fichaAnimal.NumeroCriasMacho;
import br.com.milkmoney.service.fichaAnimal.NumeroPartos;
import br.com.milkmoney.service.fichaAnimal.NumeroServicosAtePrenhez;
import br.com.milkmoney.service.fichaAnimal.ProximoParto;
import br.com.milkmoney.service.fichaAnimal.ProximoServico;
import br.com.milkmoney.service.fichaAnimal.SituacaoUltimaCobertura;
import br.com.milkmoney.service.fichaAnimal.UltimoProcedimento;

@Service@SuppressWarnings("rawtypes")
public class FichaAnimalService{

	@Autowired private FichaAnimalDao dao;
	@Autowired private AnimalService animalService;
	
	public static final Class DATA_ULTIMA_COBERTURA = DataUltimaCobertura.class;
	public static final Class DATA_ULTIMO_PARTO = DataUltimoParto.class;
	public static final Class DIAS_EM_ABERTO_ANIMAL = DiasEmAbertoAnimal.class;
	public static final Class DIAS_EMLACTACAO_ANIMAL = DiasEmLactacaoAnimal.class;
	public static final Class EFICIENCIA_REPRODUTIVA_ANIMAL = EficienciaReprodutivaAnimal.class;
	public static final Class ENCERRAMENTO_LACTACAO = EncerramentoLactacao.class;
	public static final Class IDADE_PRIMEIRA_COBERTURA = IdadePrimeiraCobertura.class;
	public static final Class IDADE_PRIMEIRO_PARTO = IdadePrimeiroParto.class;
	public static final Class INTERVALO_ENTRE_PARTOS_ANIMAL = IntervaloEntrePartosAnimal.class;
	public static final Class LOTE_ANIMAL = LoteAnimal.class;
	public static final Class MEDIA_PRODUCAO_ULTIMA_LACTACAO = MediaProducaoUltimaLactacao.class;
	public static final Class NUMEROCRIAS_FEMEA = NumeroCriasFemea.class;
	public static final Class NUMERO_CRIAS_MACHO = NumeroCriasMacho.class;
	public static final Class NUMERO_PARTOS = NumeroPartos.class;
	public static final Class NUMERO_SERVICOS_ATE_PRENHEZ = NumeroServicosAtePrenhez.class;
	public static final Class PROXIMO_PARTO = ProximoParto.class;
	public static final Class PROXIMO_SERVICO = ProximoServico.class;
	public static final Class SITUACAO_ULTIMA_COBERTURA = SituacaoUltimaCobertura.class;
	public static final Class ULTIMO_PROCEDIMENTO = UltimoProcedimento.class;
	public static final Class EFICIENCIA_TEMPO_PRODUCAO = EficienciaTempoProducao.class;

	public ObservableList<FichaAnimal> findAllEventosByAnimal(Animal animal) {
		return FXCollections.observableArrayList(dao.findAllByAnimal(animal));
	}
	
	@Transactional
	public FichaAnimal generateFichaAnimal(Animal animal, List<AbstractFichaAnimal> fieldsToLoad){
		
		FichaAnimal fichaAnimal = dao.findFichaAnimal(animal);
		
		if (fichaAnimal == null){
			fichaAnimal = new FichaAnimal(animal);
		}
		
		for (AbstractFichaAnimal field : fieldsToLoad) {

			Object[] params = new Object[]{
		    		fichaAnimal,
		    		animal
		    };
		    
		    field.load(params);
		    
		}
		
		dao.persist(fichaAnimal);
		return fichaAnimal;
		
	}
	
	@Transactional
	public void generateFichaForAll(Object ... params){
		
		@SuppressWarnings({ "unchecked" })
		List<AbstractFichaAnimal> fieldsToLoad = (List) params[0];
		
		if ( fieldsToLoad == null ){
			fieldsToLoad = getAllFields();
		}
		
		List<Animal> animais = animalService.findAll();
		for ( Animal animal : animais ){
			generateFichaAnimal(animal, fieldsToLoad);
		}
		
	}
	
	public List<AbstractFichaAnimal> getAllFields(){
		return Arrays.asList(new AbstractFichaAnimal[] {
				(AbstractFichaAnimal)MainApp.getBean(DATA_ULTIMA_COBERTURA),
				(AbstractFichaAnimal)MainApp.getBean(DATA_ULTIMO_PARTO),
				(AbstractFichaAnimal)MainApp.getBean(DIAS_EM_ABERTO_ANIMAL),
				(AbstractFichaAnimal)MainApp.getBean(DIAS_EMLACTACAO_ANIMAL),
				(AbstractFichaAnimal)MainApp.getBean(EFICIENCIA_REPRODUTIVA_ANIMAL),
				(AbstractFichaAnimal)MainApp.getBean(ENCERRAMENTO_LACTACAO),
				(AbstractFichaAnimal)MainApp.getBean(IDADE_PRIMEIRA_COBERTURA),
				(AbstractFichaAnimal)MainApp.getBean(IDADE_PRIMEIRO_PARTO),
				(AbstractFichaAnimal)MainApp.getBean(INTERVALO_ENTRE_PARTOS_ANIMAL),
				(AbstractFichaAnimal)MainApp.getBean(LOTE_ANIMAL),
				(AbstractFichaAnimal)MainApp.getBean(MEDIA_PRODUCAO_ULTIMA_LACTACAO),
				(AbstractFichaAnimal)MainApp.getBean(NUMEROCRIAS_FEMEA),
				(AbstractFichaAnimal)MainApp.getBean(NUMERO_CRIAS_MACHO),
				(AbstractFichaAnimal)MainApp.getBean(NUMERO_PARTOS),
				(AbstractFichaAnimal)MainApp.getBean(NUMERO_SERVICOS_ATE_PRENHEZ),
				(AbstractFichaAnimal)MainApp.getBean(PROXIMO_PARTO),
				(AbstractFichaAnimal)MainApp.getBean(PROXIMO_SERVICO),
				(AbstractFichaAnimal)MainApp.getBean(SITUACAO_ULTIMA_COBERTURA),
				(AbstractFichaAnimal)MainApp.getBean(ULTIMO_PROCEDIMENTO),
				(AbstractFichaAnimal)MainApp.getBean(EFICIENCIA_TEMPO_PRODUCAO)
		});
	}
	
	public List<AbstractFichaAnimal> getField(Class field){
		return Arrays.asList(new AbstractFichaAnimal[] {
				(AbstractFichaAnimal)MainApp.getBean(field)
		});
	}
	
	@Transactional
	public List<FichaAnimal> generateFichaAnimal(List<Animal> animais, List<AbstractFichaAnimal> fieldsToLoad) {
		
		List<FichaAnimal> fichas = new ArrayList<FichaAnimal>();
		
		if ( fieldsToLoad == null ){
			fieldsToLoad = getAllFields();
		}
		
		for (Animal animal : animais){
			fichas.add(generateFichaAnimal(animal, fieldsToLoad));
		}
		
		return fichas;
		
		
	}

	public XYChart.Series<String, Number> getDataChart() {
		
    	XYChart.Series<String, Number> serieIndicador = new XYChart.Series<String, Number>();
    	serieIndicador.setName("Valor Indicador");
    	
    	List<FichaAnimal> result = dao.findAll(FichaAnimal.class);
    	
    	for ( FichaAnimal fichaAnimal : result ){
    		serieIndicador.getData().add(new XYChart.Data<String, Number>(fichaAnimal.getAnimal().getNumeroNome(), fichaAnimal.getEficienciaReprodutiva()));
    	}
    	
    	return serieIndicador;
    	
	}
	
}
