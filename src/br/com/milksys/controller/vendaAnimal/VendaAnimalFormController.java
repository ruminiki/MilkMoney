package br.com.milksys.controller.vendaAnimal;

import java.math.BigDecimal;
import java.util.function.Function;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.TableCellHyperlinkRemoverFactory;
import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.controller.animal.AnimalReducedOverviewController;
import br.com.milksys.controller.comprador.CompradorReducedOverviewController;
import br.com.milksys.controller.motivoVendaAnimal.MotivoVendaAnimalReducedOverviewController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.AnimalVendido;
import br.com.milksys.model.Comprador;
import br.com.milksys.model.DestinacaoAnimal;
import br.com.milksys.model.MotivoVendaAnimal;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.VendaAnimal;
import br.com.milksys.service.IService;
import br.com.milksys.util.NumberFormatUtil;
import br.com.milksys.validation.AnimalVendidoValidation;
import br.com.milksys.validation.VendaAnimalValidation;

@Controller
public class VendaAnimalFormController extends AbstractFormController<Integer, VendaAnimal> {

	//dados da venda
	@FXML private UCTextField inputComprador, inputObservacao;
	@FXML private DatePicker inputDataVenda, inputPrevisaoRecebimento;
	
	//dados do animal
	@FXML private UCTextField inputAnimal, inputValorAnimal; 
	@FXML private ComboBox<String> inputDestinacaoAnimal;
	@FXML private UCTextField inputMotivoVendaAnimal;
	@FXML private Button btnBuscarAnimal;
	
	@FXML private TableView<AnimalVendido> table;
	@FXML private TableColumn<AnimalVendido, String> animalColumn;
	@FXML private TableColumn<MotivoVendaAnimal, String> motivoVendaAnimalColumn;
	@FXML private TableColumn<DestinacaoAnimal, String> destinacaoAnimalColumn;
	@FXML private TableColumn<AnimalVendido, String> valorAnimalColumn;
	@FXML private TableColumn<AnimalVendido, String> removerColumn;
	
	@FXML private Label lblTotalVenda;
	
	@Autowired private CompradorReducedOverviewController compradorReducedOverviewController;
	@Autowired private AnimalReducedOverviewController animalReducedOverviewController;
	@Autowired private MotivoVendaAnimalReducedOverviewController motivoVendaAnimalReducedOverviewController;

	private AnimalVendido animalVendido = new AnimalVendido(getObject());
	
	@FXML
	public void initialize() {
		
		//dados da venda
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		inputDataVenda.valueProperty().bindBidirectional(getObject().dataVendaProperty());
		inputPrevisaoRecebimento.valueProperty().bindBidirectional(getObject().dataRecebimentoProperty());
		if ( getObject().getComprador() != null ){
			inputComprador.textProperty().set(getObject().getComprador().toString());
		}
		
		//dados dos animais vendidos
		inputDestinacaoAnimal.setItems(DestinacaoAnimal.getItems());
		
		MaskFieldUtil.decimal(inputValorAnimal);
		
		//table animais vendidos
		animalColumn.setCellValueFactory(new PropertyValueFactory<AnimalVendido,String>("animal"));
		motivoVendaAnimalColumn.setCellValueFactory(new PropertyValueFactory<MotivoVendaAnimal,String>("motivoVendaAnimal"));
		destinacaoAnimalColumn.setCellValueFactory(new PropertyValueFactory<DestinacaoAnimal,String>("destinacaoAnimal"));
		valorAnimalColumn.setCellValueFactory(new PropertyValueFactory<AnimalVendido,String>("valorAnimal"));
		removerColumn.setCellValueFactory(new PropertyValueFactory<AnimalVendido,String>("motivoVendaAnimal"));
		removerColumn.setCellFactory(new TableCellHyperlinkRemoverFactory<AnimalVendido, String>(removerAnimalDaVenda, false));
		
		table.getItems().clear();
		table.getItems().addAll(getObject().getAnimaisVendidos());
		
		atualizaTotalVenda();
		
		//para o caso de o form ter sido chamado a partir da tela de animais
		//nesse caso ele já terá o animal informado
		if ( animalVendido != null && animalVendido.getAnimal() != null &&
				getObject().getId() <= 0 ){
			inputAnimal.setText(animalVendido.getAnimal().toString());
			inputValorAnimal.setText(NumberFormatUtil.decimalFormat(animalVendido.getAnimal().getValor()));
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
			animalVendido.setAnimal(animalReducedOverviewController.getObject());
		}
		
		if ( animalVendido.getAnimal() != null ){
			inputAnimal.textProperty().set(animalVendido.getAnimal().getNumeroNome());	
		}else{
			inputAnimal.textProperty().set("");
		}
		
	}
	
	@FXML
	private void handleSelecionarMotivoVenda(){
		
		motivoVendaAnimalReducedOverviewController.setObject(new MotivoVendaAnimal());
		motivoVendaAnimalReducedOverviewController.showForm();
		if ( motivoVendaAnimalReducedOverviewController.getObject() != null && motivoVendaAnimalReducedOverviewController.getObject().getId() > 0 ){
			animalVendido.setMotivoVendaAnimal(motivoVendaAnimalReducedOverviewController.getObject());
		}
		
		if ( animalVendido.getMotivoVendaAnimal() != null ){
			inputMotivoVendaAnimal.textProperty().set(animalVendido.getMotivoVendaAnimal().toString());	
		}else{
			inputMotivoVendaAnimal.textProperty().set("");
		}
		
	}
	
	@FXML 
	private void adicionarAnimalVenda(){
		
		animalVendido.setDestinacaoAnimal(inputDestinacaoAnimal.getSelectionModel().getSelectedItem());
		animalVendido.setValorAnimal(NumberFormatUtil.fromString(inputValorAnimal.getText()));
		animalVendido.setVendaAnimal(getObject());
		
		AnimalVendidoValidation.validate(animalVendido);
		VendaAnimalValidation.validateAddAnimal(getObject(), animalVendido);
		
		getObject().getAnimaisVendidos().add(animalVendido);
		table.getItems().add(animalVendido);
		
		atualizaTotalVenda();
		
		animalVendido = new AnimalVendido(getObject());
		
		inputDestinacaoAnimal.getSelectionModel().clearSelection();
		inputMotivoVendaAnimal.clear();
		inputValorAnimal.clear();
		inputAnimal.clear();
		
	}
	
	private void atualizaTotalVenda(){
		BigDecimal totalVenda = BigDecimal.ZERO;
		
		for (AnimalVendido a : getObject().getAnimaisVendidos()){
			totalVenda = totalVenda.add(a.getValorAnimal());
		}
		
		getObject().setValorTotal(totalVenda);
		lblTotalVenda.setText("R$ " + NumberFormatUtil.decimalFormat(getObject().getValorTotal()));
	}
	
	Function<Integer, Boolean> removerAnimalDaVenda = index -> {
		
		if ( index <= table.getItems().size() ){
			
			if ( !getObject().getAnimaisVendidos().remove(index) ){
				getObject().getAnimaisVendidos().remove(table.getItems().get(index));
			}
			table.getItems().clear();
			table.getItems().addAll(getObject().getAnimaisVendidos());
			
			atualizaTotalVenda();
			
			return true;
			
		}
		
		return false;
		
	};
	
	public AnimalVendido getAnimalVendido() {
		return animalVendido;
	}

	public void setAnimalVendido(AnimalVendido animalVendido) {
		this.animalVendido = animalVendido;
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
