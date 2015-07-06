package br.com.milksys.controller.producaoLeite;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.NumberTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.model.ProducaoLeite;
import br.com.milksys.util.DateUtil;

@Controller
public class ProducaoLeiteFormController extends AbstractFormController<Integer, ProducaoLeite> {

	@FXML private TextField inputData;
	@FXML private NumberTextField inputNumeroVacasOrdenhadas;
	@FXML private NumberTextField inputVolumeProduzido;
	@FXML private NumberTextField inputVolumeEntregue;
	@FXML private TextField inputObservacao;

	@Autowired private ProducaoLeiteOverviewController producaoLeiteOverviewController;
	
	@FXML
	public void initialize() {
		
		inputNumeroVacasOrdenhadas.textProperty().bindBidirectional(getObject().numeroVacasOrdenhadasProperty());
		inputVolumeProduzido.textProperty().bindBidirectional(getObject().volumeProduzidoProperty());
		inputVolumeEntregue.textProperty().bindBidirectional(getObject().volumeEntregueProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		inputData.setText(DateUtil.format(getObject().dataProperty().get()));
		
		MaskFieldUtil.numeroInteiro(inputNumeroVacasOrdenhadas);
		MaskFieldUtil.numeroInteiro(inputVolumeProduzido);
		MaskFieldUtil.numeroInteiro(inputVolumeEntregue);
		
	}
	
	@Override
	protected void afterSave() {
		producaoLeiteOverviewController.resume();
	}
	
	@Override
	protected String getFormName() {
		return "view/producaoLeite/ProducaoLeiteForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Produção Leite";
	}
	
}
