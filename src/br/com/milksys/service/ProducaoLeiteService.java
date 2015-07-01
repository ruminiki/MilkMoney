package br.com.milksys.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.ProducaoLeiteDao;
import br.com.milksys.model.PrecoLeite;
import br.com.milksys.model.ProducaoLeite;
import br.com.milksys.util.DateUtil;
import br.com.milksys.validation.ProducaoLeiteValidation;

@Service
public class ProducaoLeiteService implements IService<Integer, ProducaoLeite>{

	@Autowired public ProducaoLeiteDao dao;
	@Autowired private PrecoLeiteService precoLeiteService;

	@Override
	public boolean save(ProducaoLeite entity) {
		
		if ( entity.getId() <= 0 ){
			ProducaoLeite p = dao.findByDate(entity.getData());
			if ( p != null ){
				return true;
			}
		}
		
		int vacasOrdenhadas = entity.getNumeroVacasOrdenhadas();
		BigDecimal volumeProduzido = entity.getVolumeProduzido();
		
		if ( vacasOrdenhadas > 0 && volumeProduzido.compareTo(BigDecimal.ZERO) > 0 ){
			entity.setMediaProducao(volumeProduzido.divide(new BigDecimal(vacasOrdenhadas), 2, RoundingMode.HALF_UP));	
		}
		
		PrecoLeite precoLeite = precoLeiteService.findByMesAno(entity.getMes(), entity.getAno());
		if ( precoLeite != null ){
			entity.setValor(precoLeite.getValor().multiply(entity.getVolumeEntregue()));
		}
		
		ProducaoLeiteValidation.validate(entity);
		
		return dao.persist(entity);	
	}
	
	@Override
	public boolean remove(ProducaoLeite entity) {
		return dao.remove(entity);
	}

	@Override
	public ProducaoLeite findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<ProducaoLeite> findAll() {
		return dao.findAll(ProducaoLeite.class);
	}
	
	public ObservableList<ProducaoLeite> findAllByPeriodoAsObservableList(Date inicio, Date fim) {
		ObservableList<ProducaoLeite> list = FXCollections.observableArrayList();
		list.addAll(dao.findAllByPeriodo(inicio, fim));
		return list;
	}

	/**
	 * Método que percorre lista de objetos atualizando o valor com base no preço do leite do mês
	 * @param data
	 * @param mes
	 * @param ano
	 */
	public void recarregaPrecoLeite(ObservableList<ProducaoLeite> data, String mes, int ano){
		
		PrecoLeite precoLeite = precoLeiteService.findByMesAno(mes, ano);
		
		if ( precoLeite != null ){
			//varre a tabela atualizando os valores diários
			for ( int index = 0; index < data.size(); index++ ){
				ProducaoLeite producaoLeite = data.get(index);
				producaoLeite.setValor(precoLeite.getValor().multiply(producaoLeite.getVolumeEntregue()));
				data.set(index, producaoLeite);
			}
		}
		
	}

	/**
	 * Para cada mês selecionado, configura os dias para registro da produção diária.
	 * Os dias já são salvos no banco para a montagem da tabela. 
	 * 
	 * @param dataInicio
	 * @param dataFim
	 */
	public void configuraTabelaDiasMesSelecionado(Date dataInicio, Date dataFim){
		
		Calendar cDataInicio = Calendar.getInstance();
		cDataInicio.setTimeInMillis(dataInicio.getTime());
		
		Calendar cDataFim = Calendar.getInstance();
		cDataFim.setTimeInMillis(dataFim.getTime());
		
		while ( cDataInicio.before(cDataFim) || cDataInicio.equals(cDataFim) ){
			ProducaoLeite producaoLeite = new ProducaoLeite(DateUtil.asLocalDate(cDataInicio.getTime()), 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
			save(producaoLeite);
			cDataInicio.add(Calendar.DAY_OF_MONTH, 1);
		}
		
	}
	
	
}
