package br.com.milkmoney.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.ProducaoIndividualDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.PrecoLeite;
import br.com.milkmoney.model.ProducaoIndividual;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.ProducaoIndividualValidation;

@Service
public class ProducaoIndividualService implements IService<Integer, ProducaoIndividual>{

	@Autowired private ProducaoIndividualDao dao;
	@Autowired private PrecoLeiteService precoLeiteService;
	@Autowired private LactacaoService lactacaoService;
	@Autowired private AnimalDao animalDao;	
	

	@Override
	@Transactional
	public boolean save(ProducaoIndividual entity) {
		
		ProducaoIndividual producaoIndividual = findByAnimalAndData(entity.getAnimal(), entity.getData());
		
		if ( producaoIndividual != null ){
			/*producaoIndividual.setPrimeiraOrdenha(entity.getPrimeiraOrdenha());
			producaoIndividual.setSegundaOrdenha(entity.getSegundaOrdenha());
			producaoIndividual.setTerceiraOrdenha(entity.getTerceiraOrdenha());
			entity.setId(producaoIndividual.getId());*/
			
			entity = producaoIndividual;
			//ProducaoIndividualValidation.validateAnimal(entity.getAnimal(), animalDao.isInLactacao(entity.getData(), entity.getAnimal()));
		}
		ProducaoIndividualValidation.validate(entity);
		return dao.persist(entity);
		
	}
	
	/**
	 * Verifica se já existe produção para o animal no mesmo dia.
	 * Se existir atualiza o objeto passado por parâmetro para que ele
	 * seja salvo em seguida e seja mantida apenas um registro por animal por dia.
	 * @param producaoIndividual
	 * @return
	 */
	public ProducaoIndividual beforeSave(ProducaoIndividual producaoIndividual){
		
		ProducaoIndividual aux = findByAnimalAndData(producaoIndividual.getAnimal(), producaoIndividual.getData());
		
		if ( aux != null ){
			aux.setPrimeiraOrdenha(producaoIndividual.getPrimeiraOrdenha());
			aux.setSegundaOrdenha(producaoIndividual.getSegundaOrdenha());
			aux.setTerceiraOrdenha(producaoIndividual.getTerceiraOrdenha());
			producaoIndividual = aux;
		}
		
		return atualizaValor(producaoIndividual);
		
	}
	
	/**
	 * Busca o preco para o mês referente e atualiza na produção invididual
	 * @param producaoIndividual
	 * @return
	 */
	public ProducaoIndividual atualizaValor(ProducaoIndividual producaoIndividual){
		PrecoLeite precoLeite = precoLeiteService.findByMesAno(producaoIndividual.getMes(), producaoIndividual.getAno());
		if ( precoLeite != null ){
			producaoIndividual.setValor(precoLeite.getValor().multiply(producaoIndividual.getTotalProducaoDia()));
		}else{
			producaoIndividual.setValor(BigDecimal.ZERO);
		}
		return producaoIndividual;
	}

	/**
	 * Método que percorre lista de objetos atualizando o valor com base no preço do leite do mês
	 * @param data
	 */
	public void atualizaValorProducao(ObservableList<ProducaoIndividual> data){
		
		//varre a tabela atualizando os valores diários
		for ( int index = 0; index < data.size(); index++ ){
			ProducaoIndividual producaoIndividual = data.get(index);
			data.set(index, atualizaValor(producaoIndividual));
		}
		
	}
	
	@Override
	@Transactional
	public boolean remove(ProducaoIndividual entity) {
		return dao.remove(entity);
	}

	@Override
	public ProducaoIndividual findById(Integer id) {
		return dao.findById(ProducaoIndividual.class, id);
	}

	@Override
	public List<ProducaoIndividual> findAll() {
		return dao.findAll(ProducaoIndividual.class);
	}

	@Override
	public ObservableList<ProducaoIndividual> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(ProducaoIndividual.class));
	}
	
	@Override
	public ObservableList<ProducaoIndividual> defaultSearch(String param) {
		return null;
	}
	
	public ObservableList<ProducaoIndividual> findByDate(Date data) {
		return FXCollections.observableArrayList(dao.findByDate(data));
	}

	public List<ProducaoIndividual> findByAnimal(Animal animal) {
		return dao.findByAnimal(animal);
	}
	
	public ObservableList<ProducaoIndividual> findByAnimalPeriodo(Animal animal, Date dataInicio, Date dataFim) {
		return FXCollections.observableArrayList(dao.findByAnimalPeriodo(animal, dataInicio, dataFim));
	}

	public ProducaoIndividual findByAnimalAndData(Animal animal, Date data) {
		return dao.findByAnimalAndData(animal, data);
	}

	@Override
	public void validate(ProducaoIndividual entity) {
		
	}

	public ObservableList<Series<String, Number>> getDataChart(Animal animal, Date dataInicio, Date dataFim) {
		
		ObservableList<Series<String, Number>> series = FXCollections.observableArrayList();
    	XYChart.Series<String, Number> serie = new XYChart.Series<String, Number>();
    	
    	serie.setName("Produção Individual");
    	
    	for ( ProducaoIndividual producao : dao.findByAnimalPeriodo(animal, dataInicio, dataFim) ){
    		serie.getData().add(new XYChart.Data<String, Number>(DateUtil.format(producao.getData()), producao.getTotalProducaoDia()));
    	}
    	
    	series.add(serie);
    	
    	return series;
    	
	}
	
	public Float getMediaProducaoAnimal(Animal animal){
			
		float somaProducao = 0F;
		int   totalRegistros = 0;
		
		for ( ProducaoIndividual producao : dao.findByAnimal(animal) ){
			somaProducao += producao.getTotalProducaoDia().floatValue();
			totalRegistros++;
		}
		
		return somaProducao > 0 ? somaProducao/totalRegistros : somaProducao;
		
	}
	
	public Float getMediaAnimalPeriodo(Animal animal, Date inicio, Date fim){
		float somaProducao = 0F;
		int   totalRegistros = 0;
		
		for ( ProducaoIndividual producao : dao.findByAnimalPeriodo(animal, inicio, fim) ){
			somaProducao += producao.getTotalProducaoDia().floatValue();
			totalRegistros++;
		}
		
		return somaProducao > 0 ? somaProducao/totalRegistros : somaProducao;
	}

}
