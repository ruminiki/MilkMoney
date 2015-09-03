package br.com.milkmoney.controller.vendaAnimal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.animal.AnimalReducedOverviewController;
import br.com.milkmoney.controller.comprador.CompradorReducedOverviewController;
import br.com.milkmoney.controller.motivoVendaAnimal.MotivoVendaAnimalReducedOverviewController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Comprador;
import br.com.milkmoney.model.DestinacaoAnimal;
import br.com.milkmoney.model.MotivoVendaAnimal;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.model.VendaAnimal;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.util.NumberFormatUtil;

@Controller
public class VendaAnimalFormController extends AbstractFormController<Integer, VendaAnimal> {

	//dados da venda
	@FXML private UCTextField inputComprador, inputObservacao;
	@FXML private DatePicker inputDataVenda;
	//dados do animal
	@FXML private UCTextField inputAnimal, inputValorAnimal; 
	@FXML private ComboBox<String> inputDestinacaoAnimal;
	@FXML private UCTextField inputMotivoVendaAnimal;
	@FXML private Button btnBuscarAnimal;
	
	@Autowired private CompradorReducedOverviewController compradorReducedOverviewController;
	@Autowired private AnimalReducedOverviewController animalReducedOverviewController;
	@Autowired private MotivoVendaAnimalReducedOverviewController motivoVendaAnimalReducedOverviewController;

	private Animal animalVendido;
	
	@FXML
	public void initialize() {
		
		//dados da venda
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		inputDataVenda.valueProperty().bindBidirectional(getObject().dataVendaProperty());
		inputValorAnimal.textProperty().bindBidirectional(getObject().valorAnimalProperty());
		inputDestinacaoAnimal.setItems(DestinacaoAnimal.getItems());
		inputDestinacaoAnimal.valueProperty().bindBidirectional(getObject().destinacaoAnimalProperty());
		
		if ( getObject().getMotivoVendaAnimal() != null ){
			inputMotivoVendaAnimal.textProperty().set(getObject().getMotivoVendaAnimal().toString());	
		}
		
		if ( getObject().getComprador() != null ){
			inputComprador.textProperty().set(getObject().getComprador().toString());
		}
		
		if ( getObject().getAnimal() != null ){
			inputAnimal.setText(getObject().getAnimal().toString());
		}
		
		MaskFieldUtil.decimal(inputValorAnimal);
		
		//para o caso de o form ter sido chamado a partir da tela de animais
		//nesse caso ele já terá o animal informado
		if ( animalVendido != null && getObject().getId() <= 0 ){
			getObject().setAnimal(animalVendido);
			inputAnimal.setText(animalVendido.toString());
			inputValorAnimal.setText(NumberFormatUtil.decimalFormat(animalVendido.getValor()));
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
	private void handleSelecionarMotivoVenda(){
		
		motivoVendaAnimalReducedOverviewController.setObject(new MotivoVendaAnimal());
		motivoVendaAnimalReducedOverviewController.showForm();
		if ( motivoVendaAnimalReducedOverviewController.getObject() != null && motivoVendaAnimalReducedOverviewController.getObject().getId() > 0 ){
			getObject().setMotivoVendaAnimal(motivoVendaAnimalReducedOverviewController.getObject());
		}
		
		if ( getObject().getMotivoVendaAnimal() != null ){
			inputMotivoVendaAnimal.textProperty().set(getObject().getMotivoVendaAnimal().toString());	
		}else{
			inputMotivoVendaAnimal.textProperty().set("");
		}
		
	}
	
	public void setAnimalVendido(Animal animalVendido) {
		this.animalVendido = animalVendido;
	}

	@Override
	public String getFormName() {
		return "view/vendaAnimal/VendaAnimalForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Venda Animal";
	}
	
	@Override
	@Resource(name = "vendaAnimalService")
	protected void setService(IService<Integer, VendaAnimal> service) {
		super.setService(service);
	}

	
}
