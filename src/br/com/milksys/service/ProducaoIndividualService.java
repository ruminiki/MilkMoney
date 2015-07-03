package br.com.milksys.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.ProducaoIndividualDao;
import br.com.milksys.model.Animal;
import br.com.milksys.model.PrecoLeite;
import br.com.milksys.model.ProducaoIndividual;
import br.com.milksys.validation.ProducaoIndividualValidation;

@Service
public class ProducaoIndividualService implements IService<Integer, ProducaoIndividual>{

	@Autowired private ProducaoIndividualDao dao;
	@Autowired private PrecoLeiteService precoLeiteService;
	

	@Override
	public boolean save(ProducaoIndividual entity) {
		
		ProducaoIndividual producaoIndividual = findByAnimalAndData(entity.getAnimal(), entity.getData());
		
		if ( producaoIndividual != null ){
			producaoIndividual.setPrimeiraOrdenha(entity.getPrimeiraOrdenha());
			producaoIndividual.setSegundaOrdenha(entity.getSegundaOrdenha());
			producaoIndividual.setTerceiraOrdenha(entity.getTerceiraOrdenha());
			entity.setId(producaoIndividual.getId());
			
			ProducaoIndividualValidation.validate(entity);
			
			return dao.persist(atualizaValor(producaoIndividual));	
		}else{
			
			ProducaoIndividualValidation.validate(entity);
			return dao.persist(entity);
			
		}
		
	}
	
	/**
	 * Verifica se j� existe produ��o para o animal no mesmo dia.
	 * Se existir atualiza o objeto passado por par�metro para que ele
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
	 * Busca o preco para o m�s referente e atualiza na produ��o invididual
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
	 * M�todo que percorre lista de objetos atualizando o valor com base no pre�o do leite do m�s
	 * @param data
	 */
	public void atualizaValorProducao(ObservableList<ProducaoIndividual> data){
		
		//varre a tabela atualizando os valores di�rios
		for ( int index = 0; index < data.size(); index++ ){
			ProducaoIndividual producaoIndividual = data.get(index);
			data.set(index, atualizaValor(producaoIndividual));
		}
		
	}
	
	@Override
	public boolean remove(ProducaoIndividual entity) {
		return dao.remove(entity);
	}

	@Override
	public ProducaoIndividual findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<ProducaoIndividual> findAll() {
		return dao.findAll(ProducaoIndividual.class);
	}

	public List<ProducaoIndividual> findByDate(Date data) {
		return dao.findByDate(data);
	}

	public List<ProducaoIndividual> findByAnimal(Animal animal) {
		return dao.findByAnimal(animal);
	}

	public ProducaoIndividual findByAnimalAndData(Animal animal, Date data) {
		return dao.findByAnimalAndData(animal, data);
	}

	@Override
	public void validate(ProducaoIndividual entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
