package br.com.milksys.controller.vendaAnimal;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.controller.animal.AnimalReducedOverviewController;
import br.com.milksys.controller.comprador.CompradorReducedOverviewController;
import br.com.milksys.controller.motivoVendaAnimal.MotivoVendaAnimalFormController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Comprador;
import br.com.milksys.model.DestinacaoAnimal;
import br.com.milksys.model.MotivoVendaAnimal;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.VendaAnimal;
import br.com.milksys.service.IService;
import br.com.milksys.service.MotivoVendaAnimalService;

@Controller
public class VendaAnimalFormController extends AbstractFormController<Integer, VendaAnimal> {

	@FXML private UCTextField inputAnimal, inputComprador, inputObservacao, inputValor;
	@FXML private DatePicker inputDataVenda, inputPrevisaoRecebimento;
	@FXML private ComboBox<String> inputDestinacaoAnimal;
	@FXML private ComboBox<MotivoVendaAnimal> inputMotivoVenda;
	
	@Autowired private CompradorReducedOverviewController compradorReducedOverviewController;
	@Autowired private AnimalReducedOverviewController animalReducedOverviewController;
	@Autowired private MotivoVendaAnimalService motivoVendaAnimalService;
	@Autowired private MotivoVendaAnimalFormController motivoVendaAnimalFormController;

	@FXML
	public void initialize() {
		
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		inputValor.textProperty().bindBidirectional(getObject().valorProperty());
		
		inputDataVenda.valueProperty().bindBidirectional(getObject().dataVendaProperty());
		inputPrevisaoRecebimento.valueProperty().bindBidirectional(getObject().dataRecebimentoProperty());
		
		inputDestinacaoAnimal.setItems(DestinacaoAnimal.getItems());
		inputDestinacaoAnimal.valueProperty().bindBidirectional(getObject().destinacaoAnimalProperty());
		
		inputMotivoVenda.setItems(motivoVendaAnimalService.findAllAsObservableList());
		inputMotivoVenda.valueProperty().bindBidirectional(getObject().motivoVendaAnimalProperty());
		
		if ( getObject().getAnimal() != null ){
			inputAnimal.textProperty().set(getObject().getAnimal().getNumeroNome());
		}
		
		if ( getObject().getComprador() != null ){
			inputComprador.textProperty().set(getObject().getComprador().toString());
		}
		
		MaskFieldUtil.decimal(inputValor);
		
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
	private void handleSelecionarComprador() {
		compradorReducedOverviewController.setObject(new Comprador());
		compradorReducedOverviewController.showForm();
		
		if ( compradorReducedOverviewController.getObject() != null && compradorReducedOverviewController.getObject().getId() > 0 ){
			getObject().setComprador(compradorReducedOverviewController.getObject());
		}
		
		if ( getObject().getComprador() != null ){
			inputComprador.textProperty().set(getObject().getComprador().toString());	
		}else{
			inputComprador.textProperty().set("");
		}
	}
	
	@FXML
	private void handleNovoMotivoVenda(){
		motivoVendaAnimalFormController.setObject(new MotivoVendaAnimal());
		motivoVendaAnimalFormController.showForm();
		if ( motivoVendaAnimalFormController.getObject() != null && motivoVendaAnimalFormController.getObject().getId() > 0 ){
			inputMotivoVenda.getItems().add(motivoVendaAnimalFormController.getObject());
			inputMotivoVenda.getSelectionModel().select(motivoVendaAnimalFormController.getObject());
		}
	}
	
	@Override
	public String getFormName() {
		return "view/vendaAnimal/VendaAnimalForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Venda Animal";
	}
	
	@Override
	@Resource(name = "vendaAnimalService")
	protected void setService(IService<Integer, VendaAnimal> service) {
		super.setService(service);
	}

	
}
