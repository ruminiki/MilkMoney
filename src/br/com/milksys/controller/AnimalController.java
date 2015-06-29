package br.com.milksys.controller;

import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import br.com.milksys.components.TableCellDateFactory;
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
	@FXML private TableColumn<Animal, Date> dataNascimentoColumn;
	@FXML private TableColumn<Raca, String> racaColumn;
	@FXML private TableColumn<String, String> sexoColumn;
	
	@FXML private UCTextField inputNumero;
	@FXML private UCTextField inputNome;
	@FXML private DatePicker inputDataNascimento;
	@FXML private ComboBox<Raca> inputRaca;
	@FXML private ComboBox<String> inputSexo;
	
	@Autowired private RacaService racaService;
	@Autowired private RacaController racaController;
	
	private ObservableList<String> sexos = FXCollections.observableArrayList("FÊMEA", "MACHO");

	@FXML
	@SuppressWarnings("unchecked")
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			nomeColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("nome"));
			numeroColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
			dataNascimentoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataNascimento"));
			racaColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("raca"));
			sexoColumn.setCellValueFactory(new PropertyValueFactory<String,String>("sexo"));
			
			super.initialize();

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
			
			inputSexo.setItems(sexos);
			inputSexo.getSelectionModel().selectFirst();
			inputSexo.valueProperty().bindBidirectional(getObject().sexoProperty());
			
		}
		
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
	protected Animal getObject() {
		return (Animal) super.object;
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
		racaController.showForm(null);
		if ( racaController.getObject() != null ){
			inputRaca.getItems().add(racaController.getObject());
			inputRaca.getSelectionModel().select(racaController.getObject());
		}
	}

}
