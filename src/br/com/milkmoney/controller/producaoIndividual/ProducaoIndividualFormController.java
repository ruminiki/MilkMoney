package br.com.milkmoney.controller.producaoIndividual;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.ProducaoIndividual;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.ProducaoIndividualService;

@Controller
public class ProducaoIndividualFormController extends AbstractFormController<Integer, ProducaoIndividual> {

	@FXML private DatePicker inputData;
	@FXML private UCTextField inputObservacao;
	@FXML private UCTextField inputAnimal;
	@FXML private TextField inputPrimeiraOrdenha;
	@FXML private TextField inputSegundaOrdenha;
	@FXML private TextField inputTerceiraOrdenha;
	
	@FXML private ComboBox<Animal> inputAnimalComboBox;
	
	@Autowired private AnimalService animalService;
	@Autowired private ProducaoIndividualService service;
	
	@FXML
	public void initialize() {
		
		inputData.valueProperty().bindBidirectional(getObject().dataProperty());
		inputAnimal.setText(getObject().getAnimal().getNumeroNome());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		inputPrimeiraOrdenha.textProperty().bindBidirectional(getObject().primeiraOrdenhaProperty());
		inputSegundaOrdenha.textProperty().bindBidirectional(getObject().segundaOrdenhaProperty());
		inputTerceiraOrdenha.textProperty().bindBidirectional(getObject().terceiraOrdenhaProperty());
		
		MaskFieldUtil.numeroInteiro(inputPrimeiraOrdenha);
		MaskFieldUtil.numeroInteiro(inputSegundaOrdenha);
		MaskFieldUtil.numeroInteiro(inputTerceiraOrdenha);
		
	}
	
	@Override
	protected void handleSave() {
		setObject(((ProducaoIndividualService)service).beforeSave(getObject()));
		super.handleSave();
    }
	
	@Override
	protected String getFormName() {
		return "view/producaoIndividual/ProducaoIndividualForm.fxml";
	}
	
	public String getExternalFormName() {
		return "view/producaoIndividual/ProducaoIndividualExternalForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Produção Individual";
	}

	@Override
	@Resource(name = "producaoIndividualService")
	protected void setService(IService<Integer, ProducaoIndividual> service) {
		super.setService(service);
	}

}
