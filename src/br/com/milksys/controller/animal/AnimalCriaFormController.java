package br.com.milksys.controller.animal;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.FinalidadeAnimal;
import br.com.milksys.model.Raca;
import br.com.milksys.model.Sexo;
import br.com.milksys.service.IService;
import br.com.milksys.service.RacaService;
import br.com.milksys.validation.AnimalValidation;

@Controller
public class AnimalCriaFormController extends AbstractFormController<Integer, Animal> {

	@FXML private UCTextField inputNumero, inputNome, inputMae, inputPai;
	@FXML private DatePicker inputDataNascimento;
	@FXML private ComboBox<Raca> inputRaca;
	@FXML private ComboBox<String> inputFinalidadeAnimal, inputSexo;
	
	//services
	@Autowired private RacaService racaService;
	
	@FXML
	public void initialize() {
		
		inputNumero.textProperty().bindBidirectional(getObject().numeroProperty());
		inputNome.textProperty().bindBidirectional(getObject().nomeProperty());
		inputDataNascimento.valueProperty().bindBidirectional(getObject().dataNascimentoProperty());
		
		inputRaca.setItems(racaService.findAllAsObservableList());
		inputRaca.getSelectionModel().selectFirst();
		inputRaca.valueProperty().bindBidirectional(getObject().racaProperty());
		
		inputSexo.setItems(Sexo.getItems());
		inputSexo.valueProperty().bindBidirectional(getObject().sexoProperty());
		
		inputFinalidadeAnimal.setItems(FinalidadeAnimal.getItems());
		inputFinalidadeAnimal.valueProperty().bindBidirectional(getObject().finalidadeAnimalProperty());
		
		if ( getObject().getMae() != null ){
			inputMae.textProperty().set(getObject().getMae().getNumeroNome());
		}
		
		if ( getObject().getPaiMontaNatural() != null ){
			inputPai.textProperty().set(getObject().getPaiMontaNatural().getNumeroNome());
		}
		
		if ( getObject().getPaiEnseminacaoArtificial() != null ){
			inputPai.textProperty().set(getObject().getPaiEnseminacaoArtificial().getTouro());
		}
		
	}
	
	@Override
	protected void handleSave() {
		//apenas valida, pois o objeto � salvo na mesma transa��o que salva o parto
		AnimalValidation.validate(getObject());
		closeForm();
	}

	@Override
	public String getFormName() {
		return "view/animal/AnimalCriaForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Animal - Cria";
	}
	
	@Override
	@Resource(name = "animalService")
	protected void setService(IService<Integer, Animal> service) {
		super.setService(service);
	}
	
}
