package br.com.milkmoney.controller.lactacao;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.animal.AnimalReducedOverviewController;
import br.com.milkmoney.controller.motivoEncerramentoLactacao.MotivoEncerramentoLactacaoReducedOverviewController;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class LactacaoFormController extends AbstractFormController<Integer, Lactacao> {

	@FXML private UCTextField inputObservacao, inputAnimal, inputMotivoEncerramentoLactacao;
	@FXML private DatePicker inputDataInicio, inputDataFim;
	@FXML private ComboBox<String> inputMotivoEncerramento;
	@FXML private Button btnBuscarAnimal;
	
	@Autowired AnimalReducedOverviewController animalReducedOverviewController;
	@Autowired MotivoEncerramentoLactacaoReducedOverviewController motivoEncerramentoLactacaoReducedOverviewController;
	
	@FXML
	public void initialize() {
		
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		inputDataInicio.valueProperty().bindBidirectional(getObject().dataInicioProperty());
		inputDataFim.valueProperty().bindBidirectional(getObject().dataFimProperty());
		
		if ( getObject().getAnimal() != null ){
			inputAnimal.setText(getObject().getAnimal().toString());
		}
		
		if ( getObject().getDataFim() == null ){
			inputDataFim.setValue(DateUtil.asLocalDate(getObject().getDataInicio()).plusMonths(10));
		}
		
		if ( getObject().getMotivoEncerramentoLactacao() != null ){
			inputMotivoEncerramentoLactacao.setText(getObject().getMotivoEncerramentoLactacao().toString());
		}
		
	}
	
	@FXML
	private void handleSelecionarMotivoEncerramento() {
		
		motivoEncerramentoLactacaoReducedOverviewController.showForm();
		
		if ( motivoEncerramentoLactacaoReducedOverviewController.getObject() != null && motivoEncerramentoLactacaoReducedOverviewController.getObject().getId() > 0 ){
			getObject().setMotivoEncerramentoLactacao(motivoEncerramentoLactacaoReducedOverviewController.getObject());
		}
		
		if ( getObject().getMotivoEncerramentoLactacao() != null ){
			inputMotivoEncerramentoLactacao.textProperty().set(getObject().getMotivoEncerramentoLactacao().toString());	
		}else{
			inputMotivoEncerramentoLactacao.textProperty().set("");
		}
		
	}
	
	@Override
	public String getFormName() {
		return "view/lactacao/LactacaoForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Lactação";
	}
	
	@Override
	@Resource(name = "lactacaoService")
	protected void setService(IService<Integer, Lactacao> service) {
		super.setService(service);
	}

	
}
