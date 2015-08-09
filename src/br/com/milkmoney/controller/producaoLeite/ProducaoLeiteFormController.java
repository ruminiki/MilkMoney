package br.com.milkmoney.controller.producaoLeite;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.NumberTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.ProducaoLeite;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.util.DateUtil;

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
	public String getFormName() {
		return "view/producaoLeite/ProducaoLeiteForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Produção Leite";
	}
	
	@Override
	@Resource(name = "producaoLeiteService")
	protected void setService(IService<Integer, ProducaoLeite> service) {
		super.setService(service);
	}
	
}
