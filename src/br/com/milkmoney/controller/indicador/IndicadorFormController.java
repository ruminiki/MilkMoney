package br.com.milkmoney.controller.indicador;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.service.IService;

@Controller
public class IndicadorFormController extends AbstractFormController<Integer, Indicador> {

	@FXML private UCTextField       inputDescricao, inputSigla;
	@FXML private TextArea          inputDefinicao;
	
	@FXML
	public void initialize() {
		
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputSigla.textProperty().bindBidirectional(getObject().siglaProperty());
		inputDefinicao.textProperty().bindBidirectional(getObject().definicaoProperty());
		
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
