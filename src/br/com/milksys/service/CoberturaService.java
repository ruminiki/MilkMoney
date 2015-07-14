package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.CoberturaDao;
import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.util.DateUtil;
import br.com.milksys.validation.CoberturaValidation;
import br.com.milksys.validation.Validator;

@Service
public class CoberturaService implements IService<Integer, Cobertura>{

	@Autowired private CoberturaDao dao;

	@Override
	public boolean save(Cobertura entity) {
		
		if ( entity.getParto() != null ){
			throw new ValidationException(Validator.REGRA_NEGOCIO, "A cobertura já tem parto registrado, não sendo possível executar essa operação.");
		}

		CoberturaValidation.validate(entity);
		CoberturaValidation.validaSituacaoAnimal(entity.getFemea());
		CoberturaValidation.validaFemeaSelecionada(entity, findByAnimal(entity.getFemea()));
		CoberturaValidation.validaEnseminacaoArtificial(entity, (entity.getQuantidadeDosesUtilizadas() > dao.findQuantidadeDosesSemenUtilizadasNaCobertura(entity)));
		CoberturaValidation.validaSobreposicaoCoberturas(entity, dao.findLastCoberturaByAnimal(entity.getFemea()));
		
		return dao.persist(entity);
	}
	
	public void registrarConfirmacaoPrenhez(Cobertura entity){
		
		CoberturaValidation.validateRegistroConfirmacaoPrenhez(entity);
		configureSituacaoCobertura(entity);
		
		save(entity);
	}
	

	public void removerRegistroConfirmacaoPrenhez(Cobertura entity) {
		
		entity.setDataConfirmacaoPrenhez(null);
		entity.setObservacaoConfirmacaoPrenhez(null);
		entity.setSituacaoConfirmacaoPrenhez(null);
		
		configureSituacaoCobertura(entity);
		
		//verifica se existem outras coberturas para o animal com situação PRENHA, ou INDEFINIDA
		CoberturaValidation.validaSituacoesCoberturasDoAnimal(entity, findByAnimal(entity.getFemea()));
		
		save(entity);
		
	}
	
	
	public void registrarReconfirmacaoPrenhez(Cobertura entity){
		
		CoberturaValidation.validateRegistroReconfirmacaoPrenhez(entity);
		configureSituacaoCobertura(entity);
		
		save(entity);
	}
	
	public void removerRegistroReconfirmacaoPrenhez(Cobertura entity) {
		
		entity.setDataReconfirmacaoPrenhez(null);
		entity.setObservacaoReconfirmacaoPrenhez(null);
		entity.setSituacaoReconfirmacaoPrenhez(null);
		
		configureSituacaoCobertura(entity);
		
		//verifica se existem outras coberturas para o animal com situação PRENHA, ou INDEFINIDA
		CoberturaValidation.validaSituacoesCoberturasDoAnimal(entity, findByAnimal(entity.getFemea()));
		
		save(entity);
		
	}

	public void registrarRepeticaoCio(Cobertura entity){
		
		configureSituacaoCobertura(entity);
		CoberturaValidation.validateRegistroRepeticaoCio(entity);
		save(entity);
		
	}
	
	public void removerRegistroRepeticaoCio(Cobertura entity) {
		
		entity.setDataRepeticaoCio(null);
		entity.setObservacaoRepeticaoCio(null);
		
		configureSituacaoCobertura(entity);
		
		//verifica se existem outras coberturas para o animal com situação PRENHA, ou INDEFINIDA
		CoberturaValidation.validaSituacoesCoberturasDoAnimal(entity, findByAnimal(entity.getFemea()));
		
		save(entity);
		
	}
	
	private void configureSituacaoCobertura(Cobertura entity){
		
		if ( entity.getDataRepeticaoCio() != null ){
			entity.setSituacaoCobertura(SituacaoCobertura.REPETIDA);
		}else{
			if ( entity.getDataReconfirmacaoPrenhez() != null ){
				entity.setSituacaoCobertura(entity.getSituacaoReconfirmacaoPrenhez());
			}else{
				if ( entity.getDataConfirmacaoPrenhez() != null ){
					entity.setSituacaoCobertura(entity.getSituacaoConfirmacaoPrenhez());
				}else{
					entity.setSituacaoCobertura(SituacaoCobertura.INDEFINIDA);
				}
			}
		}

		configuraDataPrevisaoPartoEEncerramentoLactacao(entity);
		
	}
	
	private void configuraDataPrevisaoPartoEEncerramentoLactacao(Cobertura entity){
		
		if ( entity.getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) ||
				entity.getSituacaoCobertura().equals(SituacaoCobertura.INDEFINIDA) ){
			entity.setPrevisaoParto(DateUtil.asDate(DateUtil.asLocalDate(entity.getData()).plusMonths(9)));
			entity.setPrevisaoEncerramentoLactacao(DateUtil.asDate(DateUtil.asLocalDate(entity.getData()).plusMonths(7)));
		}else{
			entity.setPrevisaoParto(null);
			entity.setPrevisaoEncerramentoLactacao(null);
		}
		
	}
	
	public List<Cobertura> findByAnimal(Animal animal){
		return dao.findByAnimal(animal);
	}
	
	
	@Override
	public boolean remove(Cobertura entity) {
		return dao.remove(entity);
	}

	@Override
	public Cobertura findById(Integer id) {
		return dao.findById(id);
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
		return FXCollections.observableArrayList(dao.findAllByNumeroNomeAnimal(param));
	}

	public void removeServicoFromCobertura(Cobertura cobertura) {
		dao.removeServicoFromCobertura(cobertura);
	}

	@Override
	public void validate(Cobertura entity) {
		CoberturaValidation.validate(entity);
	}

	public void registrarParto(Cobertura entity) {
		entity.setSituacaoCobertura(SituacaoCobertura.PARIDA);
		configuraDataPrevisaoPartoEEncerramentoLactacao(entity);
		try{
			dao.persist(entity);
		}catch(Exception e){
			configureSituacaoCobertura(entity);
			entity.setParto(null);
			throw new RuntimeException(e);
		}
	}

	public void removerParto(Cobertura entity) {
		
		configureSituacaoCobertura(entity);
		try{
			dao.removerParto(entity);
		}catch(Exception e){
			entity.setSituacaoCobertura(SituacaoCobertura.PARIDA);
			throw new RuntimeException(e);
		}
		
	}
	
	public Cobertura findCoberturaAtivaByAnimal(Animal animal){
		return dao.findCoberturaAtivaByAnimal(animal);
	}
	
}
