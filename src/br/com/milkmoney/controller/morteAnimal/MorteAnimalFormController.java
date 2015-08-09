package br.com.milkmoney.controller.morteAnimal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.animal.AnimalReducedOverviewController;
import br.com.milkmoney.controller.causaMorteAnimal.CausaMorteAnimalReducedOverviewController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.CausaMorteAnimal;
import br.com.milkmoney.model.MorteAnimal;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.service.IService;

@Controller
public class MorteAnimalFormController extends AbstractFormController<Integer, MorteAnimal> {

	@FXML private UCTextField inputAnimal, inputObservacao, inputValorAnimal, inputCausaMorteAnimal;
	@FXML private DatePicker inputDataMorte;
	@FXML private Button btnBuscarAnimal;
	
	@Autowired private AnimalReducedOverviewController animalReducedOverviewController;
	@Autowired private CausaMorteAnimalReducedOverviewController causaMorteReducedOverviewController;

	@FXML
	public void initialize() {
		
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		inputValorAnimal.textProperty().bindBidirectional(getObject().valorAnimalProperty());
		
		inputDataMorte.valueProperty().bindBidirectional(getObject().dataMorteProperty());
		
		if ( getObject().getAnimal() != null ){
			inputAnimal.textProperty().set(getObject().getAnimal().getNumeroNome());
		}
		
		if ( getObject().getCausaMorteAnimal() != null ){
			inputAnimal.textProperty().set(getObject().getCausaMorteAnimal().toString());
		}
		
		MaskFieldUtil.decimal(inputValorAnimal);
		
	}

	@FXML
	private void handleSelecionarAnimal() {
		
		animalReducedOverviewController.setObject(new Animal(Sexo.FEMEA));
		
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, false);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, false);
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
	
	@FXML
	private void handleSelecionarCausaMorteAnimal() {
		
		causaMorteReducedOverviewController.setObject(new CausaMorteAnimal());
		
		causaMorteReducedOverviewController.showForm();
		
		if ( causaMorteReducedOverviewController.getObject() != null && causaMorteReducedOverviewController.getObject().getId() > 0 ){
			getObject().setCausaMorteAnimal(causaMorteReducedOverviewController.getObject());
		}
		
		if ( getObject().getCausaMorteAnimal() != null ){
			inputCausaMorteAnimal.textProperty().set(getObject().getCausaMorteAnimal().toString());	
		}else{
			inputCausaMorteAnimal.textProperty().set("");
		}
		
	}
	
	@Override
	public String getFormName() {
		return "view/morteAnimal/MorteAnimalForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Morte Animal";
	}
	
	@Override
	@Resource(name = "morteAnimalService")
	protected void setService(IService<Integer, MorteAnimal> service) {
		super.setService(service);
	}

	
}
