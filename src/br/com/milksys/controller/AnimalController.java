package br.com.milksys.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.CustomComboBoxCellFactory;
import br.com.milksys.components.CustomStringConverter;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Raca;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;
import br.com.milksys.service.RacaService;
import br.com.milksys.util.DateUtil;

@Controller
public class AnimalController extends AbstractController<Integer, Animal> {

	@FXML private TableColumn<Animal, String> nomeColumn;
	@FXML private TableColumn<Animal, String> numeroColumn;
	@FXML private TableColumn<Animal, String> dataNascimentoColumn;
	@FXML private TableColumn<Animal, String> racaColumn;
	@FXML private UCTextField inputNumero;
	@FXML private UCTextField inputNome;
	@FXML private DatePicker inputDataNascimento;
	@FXML private ComboBox<Raca> inputRaca;
	@Resource(name="racaService")
	private RacaService racaService;
	@Resource(name="racaController")
	private RacaController racaController;

	@FXML
	@SuppressWarnings("unchecked")
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			nomeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
			numeroColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumero()));
			dataNascimentoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getDataNascimento())));
			racaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRaca().getDescricao()));

		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			inputNumero.textProperty().bindBidirectional(((Animal)object).numeroProperty());
			inputNome.textProperty().bindBidirectional(((Animal)object).nomeProperty());
			inputDataNascimento.valueProperty().bindBidirectional(((Animal)object).dataNascimentoProperty());
			inputRaca.setItems(racaService.findAllAsObservableList());
			inputRaca.getSelectionModel().selectFirst();
			inputRaca.setCellFactory(new CustomComboBoxCellFactory<>("descricao"));
			inputRaca.setConverter(new CustomStringConverter("descricao"));
			inputRaca.valueProperty().bindBidirectional(((Animal)object).racaProperty());
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
