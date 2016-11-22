package br.com.milkmoney.controller.indicador;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.ConfiguracaoIndicador;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.model.ObjetivoIndicador;
import br.com.milkmoney.service.ConfiguracaoIndicadorService;
import br.com.milkmoney.service.IService;

@Controller
public class IndicadorFormController extends AbstractFormController<Integer, Indicador> {

	@FXML private UCTextField       inputDescricao, inputSigla, inputAno;
	@FXML private TextArea          inputDefinicao;
	@FXML private TextField         inputMenorValor, inputMaiorValor;
	@FXML private ChoiceBox<String> inputObjetivo, inputFormato;
	
	private int ano;
	private ConfiguracaoIndicador configuracaoIndicador;
	
	@Autowired private ConfiguracaoIndicadorService configuracaoIndicadorService;
	
	@FXML
	public void initialize() {
		
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputSigla.textProperty().bindBidirectional(getObject().siglaProperty());
		inputDefinicao.textProperty().bindBidirectional(getObject().definicaoProperty());
		
		configuracaoIndicador = getObject().getConfiguracaoIndicador(ano);
		/*if ( configuracaoIndicador == null ){
			configuracaoIndicador = new ConfiguracaoIndicador(getObject(), ano);
		}*/
		
		//configuracao meta
		inputAno.textProperty().bindBidirectional(configuracaoIndicador.anoProperty());
		inputMenorValor.textProperty().bindBidirectional(configuracaoIndicador.menorValorEsperadoProperty());
		inputMaiorValor.textProperty().bindBidirectional(configuracaoIndicador.maiorValorEsperadoProperty());
		inputObjetivo.setItems(ObjetivoIndicador.getItems());
		inputObjetivo.valueProperty().bindBidirectional(configuracaoIndicador.objetivoProperty());
		
	}
	
	@Override
	protected void beforeSave() {
		super.beforeSave();
		int index = 0;
		for ( ConfiguracaoIndicador config : getObject().getConfiguracoesIndicador() ){
			if ( config.getId() == configuracaoIndicador.getId() ){
				getObject().getConfiguracoesIndicador().set(index, configuracaoIndicador);
			}
			index++;
		}
	}
	
	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	@Override
	public String getFormName() {
		return "view/indicador/IndicadorForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Indicador";
	}
	
	@Override
	@Resource(name = "indicadorService")
	protected void setService(IService<Integer, Indicador> service) {
		super.setService(service);
	}

}
