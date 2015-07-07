package br.com.milksys.controller.cria;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.controller.animal.AnimalCriaFormController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.Cria;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.SimNao;
import br.com.milksys.model.SituacaoNascimento;
import br.com.milksys.model.State;
import br.com.milksys.model.TipoCobertura;
import br.com.milksys.validation.CriaValidation;

@Controller
public class CriaFormController extends AbstractFormController<Integer, Cria> {

	@FXML private ComboBox<String> inputSituacaoNascimento;
	@FXML private ComboBox<String> inputSexo;
	@FXML private ComboBox<String> inputIncorporadoAoRebanho;
	@FXML private UCTextField inputPeso;
	
	@Autowired private AnimalCriaFormController animalCriaFormController;

	@FXML
	public void initialize() {
		inputIncorporadoAoRebanho.setItems(SimNao.getItems());
		inputIncorporadoAoRebanho.valueProperty().bindBidirectional(getObject().incorporadoAoRebanhoProperty());
		inputIncorporadoAoRebanho.valueProperty().addListener((observable, oldValue, newValue) -> cadastrarAnimal(newValue));
		
		inputSexo.setItems(Sexo.getItems());
		inputSexo.valueProperty().bindBidirectional(getObject().sexoProperty());
		
		inputSituacaoNascimento.setItems(SituacaoNascimento.getItems());
		inputSituacaoNascimento.valueProperty().bindBidirectional(getObject().situacaoNascimentoProperty());

		inputPeso.textProperty().bindBidirectional(getObject().pesoProperty());
		MaskFieldUtil.numeroInteiro(inputPeso);
	}
	
	private void cadastrarAnimal(String newValue) {
		
		if ( newValue != null && newValue.equals(SimNao.SIM)){
		
			animalCriaFormController.setState(State.CREATE_TO_SELECT);
			Animal animal = new Animal(getObject().getSexo());
			animal.setPeso(getObject().getPeso());
			
			Cobertura cobertura = getObject().getParto().getCobertura();
			
			animal.setMae(cobertura.getFemea());
			
			if ( cobertura.getTipoCobertura().equals(TipoCobertura.MONTA_NATURAL) ){
				animal.setPaiMontaNatural(cobertura.getTouro());
			}else{
				animal.setPaiEnseminacaoArtificial(cobertura.getSemen());
			}
			
			animalCriaFormController.setObject(animal);
			animalCriaFormController.showForm();
			
			if ( animalCriaFormController.getObject() != null ){
				getObject().setAnimal(animalCriaFormController.getObject());
				handleSave();
			}
			
		}else{
			getObject().setAnimal(null);
		}
		
	}
	
	@Override
	protected void handleSave() {
		//apenas valida, pois o objeto é salvo na mesma transação que salva o parto
		CriaValidation.validate(getObject());
		closeForm();
	}

	@Override
	public String getFormName() {
		return "view/cria/CriaForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Cria";
	}
	
}
