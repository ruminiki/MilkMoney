package br.com.milksys.controller.servico;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.controller.prestadorServico.PrestadorServicoFormController;
import br.com.milksys.model.PrestadorServico;
import br.com.milksys.model.Servico;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;
import br.com.milksys.service.PrestadorServicoService;

@Controller
public class ServicoFormController extends AbstractFormController<Integer, Servico> {

	@FXML private UCTextField inputDescricao;
	@FXML private DatePicker inputData;
	@FXML private UCTextField inputValor;
	@FXML private ComboBox<PrestadorServico> inputPrestadorServico;

	@Autowired private PrestadorServicoService prestadorServicoService;
	@Autowired private PrestadorServicoFormController prestadorServicoFormController;
	
	@FXML
	public void initialize() {
		
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputData.valueProperty().bindBidirectional(getObject().dataProperty());
		
		inputPrestadorServico.setItems(prestadorServicoService.findAllAsObservableList());
		inputPrestadorServico.valueProperty().bindBidirectional(getObject().prestadorServicoProperty());
		
		inputValor.textProperty().bindBidirectional(getObject().valorProperty());
		MaskFieldUtil.decimal(inputValor);
		
	}
	
	@Override
	protected String getFormName() {
		return "view/servico/ServicoForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Serviço";
	}
	
	@FXML
	protected void openFormPrestadorServicoToInsertAndSelect() {
		prestadorServicoFormController.setState(State.INSERT_TO_SELECT);
		prestadorServicoFormController.setObject(new PrestadorServico());
		prestadorServicoFormController.showForm();
		if ( prestadorServicoFormController.getObject() != null && prestadorServicoFormController.getObject().getId() > 0 ){
			inputPrestadorServico.getItems().add(prestadorServicoFormController.getObject());
			inputPrestadorServico.getSelectionModel().select(prestadorServicoFormController.getObject());
		}
	}
	
	@Override
	@Resource(name = "servicoService")
	protected void setService(IService<Integer, Servico> service) {
		super.setService(service);
	}
	
}
