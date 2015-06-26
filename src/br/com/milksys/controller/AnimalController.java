package br.com.milksys.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.CustomComboBoxCellFactory;
import br.com.milksys.components.CustomStringConverter;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Raca;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;
import br.com.milksys.service.RacaService;

@Controller
public class AnimalController extends AbstractController<Integer, Animal> {

	@FXML private TableColumn<Animal, String> nomeColumn;
	@FXML private TableColumn<Animal, String> numeroColumn;
	@FXML private TableColumn<Animal, String> dataNascimentoColumn;
	@FXML private TableColumn<Raca, String> racaColumn;
	
	@FXML private UCTextField inputNumero;
	@FXML private UCTextField inputNome;
	@FXML private DatePicker inputDataNascimento;
	@FXML private ComboBox<Raca> inputRaca;
	
	@Autowired private RacaService racaService;
	@Autowired private RacaController racaController;

	@FXML
	@SuppressWarnings("unchecked")
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			nomeColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("nome"));
			numeroColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
			dataNascimentoColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("dataNascimento"));
			racaColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("raca"));

		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			
			inputNumero.textProperty().bindBidirectional(getObject().numeroProperty());
			inputNome.textProperty().bindBidirectional(getObject().nomeProperty());
			inputDataNascimento.valueProperty().bindBidirectional(getObject().dataNascimentoProperty());
			inputRaca.setItems(racaService.findAllAsObservableList());
			inputRaca.getSelectionModel().selectFirst();
			inputRaca.setCellFactory(new CustomComboBoxCellFactory<>("descricao"));
			inputRaca.setConverter(new CustomStringConverter("descricao"));
			inputRaca.valueProperty().bindBidirectional(getObject().racaProperty());
			
		}
		
		super.initialize();

	}

	@Override
	protected boolean isInputValid() {
		return true;
	}

	@Override
	protected String getFormName() {
		return "view/animal/AnimalForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Animal";
	}
	
	@Override
	public Animal getObject() {
		return ((Animal)super.getObject());
	}

	@Override
	@Resource(name = "animalService")
	protected void setService(IService<Integer, Animal> service) {
		super.setService(service);
	}

	@FXML
	protected void openFormRacaToInsertAndSelect() {
		racaController.state = State.INSERT_TO_SELECT;
		racaController.object = new Raca();
		racaController.showForm(0,0);
		if ( racaController.getObject() != null ){
			inputRaca.getItems().add((Raca)racaController.getObject());
			inputRaca.getSelectionModel().select((Raca)racaController.getObject());
		}
	}

}
