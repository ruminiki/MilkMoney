package br.com.milkmoney.controller.entregaLeite;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.NumberTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.EntregaLeite;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.util.NumberFormatUtil;

@Controller
public class EntregaLeiteFormController extends AbstractFormController<Integer, EntregaLeite> {

	@FXML private TextField inputMesReferencia;
	@FXML private TextField inputAnoReferencia;
	@FXML private DatePicker inputDataInicio;
	@FXML private DatePicker inputDataFim;
	@FXML private NumberTextField inputVolume;
	@FXML private NumberTextField inputValorMaximoPraticado;
	@FXML private NumberTextField inputValorRecebido;
	@FXML private TextField inputObservacao;
	
	@Autowired private EntregaLeiteOverviewController entregaLeiteOverviewController;

	@FXML
	public void initialize() {
		
		inputMesReferencia.textProperty().bindBidirectional(getObject().mesReferenciaProperty());
		inputAnoReferencia.textProperty().bindBidirectional(getObject().anoReferenciaProperty());
		inputDataInicio.valueProperty().bindBidirectional(getObject().dataInicioProperty());
		inputDataFim.valueProperty().bindBidirectional(getObject().dataFimProperty());
		inputVolume.textProperty().bindBidirectional(getObject().volumeProperty());
		inputValorMaximoPraticado.setText(NumberFormatUtil.decimalFormat(getObject().getValorMaximoPraticado()));
		inputValorRecebido.setText(NumberFormatUtil.decimalFormat(getObject().getValorRecebido()));
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
	
	}

	@Override
	protected void afterSave() {
		entregaLeiteOverviewController.resume();
	}
	
	@Override
	protected String getFormName() {
		return "view/entregaLeite/EntregaLeiteForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Entrega Leite";
	}
	
	@Override
	@Resource(name = "entregaLeiteService")
	protected void setService(IService<Integer, EntregaLeite> service) {
		super.setService(service);
	}

	
}
