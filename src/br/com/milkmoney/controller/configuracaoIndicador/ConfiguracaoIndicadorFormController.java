package br.com.milkmoney.controller.configuracaoIndicador;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.ConfiguracaoIndicador;
import br.com.milkmoney.model.ObjetivoIndicador;
import br.com.milkmoney.service.IService;

@Controller
public class ConfiguracaoIndicadorFormController extends AbstractFormController<Integer, ConfiguracaoIndicador> {

	@FXML private UCTextField       inputAno;
	@FXML private TextField         inputMenorValor, inputMaiorValor;
	@FXML private TextArea          inputDefinicao;
	@FXML private ChoiceBox<String> inputObjetivo, inputFormato;

	@FXML
	public void initialize() {
		
		inputAno.textProperty().bindBidirectional(getObject().anoProperty());
		inputMenorValor.textProperty().bindBidirectional(getObject().menorValorEsperadoProperty());
		inputMaiorValor.textProperty().bindBidirectional(getObject().maiorValorEsperadoProperty());
		inputObjetivo.setItems(ObjetivoIndicador.getItems());
		inputObjetivo.valueProperty().bindBidirectional(getObject().objetivoProperty());
		
	}
	
	@Override
	public String getFormName() {
		return "view/configuracaoIndicador/ConfiguracaoIndicadorForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "ConfiguracaoIndicador";
	}
	
	@Override
	@Resource(name = "configuracaoIndicadorService")
	protected void setService(IService<Integer, ConfiguracaoIndicador> service) {
		super.setService(service);
	}

}
