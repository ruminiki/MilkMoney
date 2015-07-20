package br.com.milksys.controller.lactacao;

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
import br.com.milksys.model.Lactacao;
import br.com.milksys.model.MotivoEncerramentoLactacao;
import br.com.milksys.model.Sexo;
import br.com.milksys.service.IService;

@Controller
public class LactacaoFormController extends AbstractFormController<Integer, Lactacao> {

	@FXML private UCTextField inputObservacao, inputAnimal;
	@FXML private DatePicker inputDataInicio, inputDataFim;
	@FXML private ComboBox<String> inputMotivoEncerramento;
	@FXML private Button btnBuscarAnimal;
	
	@Autowired AnimalReducedOverviewController animalReducedOverviewController;
	
	@FXML
	public void initialize() {
		
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		inputDataInicio.valueProperty().bindBidirectional(getObject().dataInicioProperty());
		inputDataFim.valueProperty().bindBidirectional(getObject().dataFimProperty());
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
		}
		
		if ( getObject().getAnimal() != null ){
			inputAnimal.textProperty().set(getObject().getAnimal().getNumeroNome());	
		}else{
			inputAnimal.textProperty().set("");
		}
		
	}
	
	@Override
	public String getFormName() {
		return "view/lactacao/LactacaoForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Lactação";
	}
	
	@Override
	@Resource(name = "lactacaoService")
	protected void setService(IService<Integer, Lactacao> service) {
		super.setService(service);
	}

	
}
