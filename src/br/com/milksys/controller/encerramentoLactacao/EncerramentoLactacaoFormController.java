package br.com.milksys.controller.encerramentoLactacao;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.controller.animal.AnimalReducedOverviewController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.EncerramentoLactacao;
import br.com.milksys.model.MotivoEncerramentoLactacao;
import br.com.milksys.model.Sexo;
import br.com.milksys.service.IService;

@Controller
public class EncerramentoLactacaoFormController extends AbstractFormController<Integer, EncerramentoLactacao> {

	@FXML private UCTextField inputObservacao, inputAnimal;
	@FXML private DatePicker inputData;
	@FXML private ComboBox<String> inputMotivoEncerramento;
	@FXML private Button btnBuscarAnimal;
	
	@Autowired AnimalReducedOverviewController animalReducedOverviewController;
	
	@FXML
	public void initialize() {
		
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		inputData.valueProperty().bindBidirectional(getObject().dataProperty());
		inputMotivoEncerramento.setItems(MotivoEncerramentoLactacao.getItems());
		inputMotivoEncerramento.valueProperty().bindBidirectional(getObject().motivoEncerramentoLactacaoProperty());
		
		if ( getObject().getAnimal() != null ){
			inputAnimal.setText(getObject().getAnimal().toString());
		}
		
	}
	
	@FXML
	private void handleSelecionarAnimal() {
		
		animalReducedOverviewController.setObject(new Animal(Sexo.FEMEA));
		
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, true);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, true);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.REMOVE_DISABLED, true);

		animalReducedOverviewController.showForm();
		
		if ( animalReducedOverviewController.getObject() != null && animalReducedOverviewController.getObject().getId() > 0 ){
			getObject().setAnimal(animalReducedOverviewController.getObject());
			getObject().setDataPrevisaoParto(animalReducedOverviewController.getObject().getDataPrevisaoProximoParto());
		}
		
		if ( getObject().getAnimal() != null ){
			inputAnimal.textProperty().set(getObject().getAnimal().getNumeroNome());	
		}else{
			inputAnimal.textProperty().set("");
		}
		
	}
	
	@Override
	public String getFormName() {
		return "view/encerramentoLactacao/EncerramentoLactacaoForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Encerramento Lactação";
	}
	
	@Override
	@Resource(name = "encerramentoLactacaoService")
	protected void setService(IService<Integer, EncerramentoLactacao> service) {
		super.setService(service);
	}

	
}
