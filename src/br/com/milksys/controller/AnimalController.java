package br.com.milksys.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.util.StringConverter;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

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
	public void initialize() {
		nomeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
		numeroColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumero()));
		dataNascimentoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getDataNascimento())));
		racaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRaca().getDescricao()));

		if ( inputNumero != null ){
			inputNumero.textProperty().bindBidirectional(((Animal)object).numeroProperty());
		}
		
		if ( inputNome != null ){
			inputNome.textProperty().bindBidirectional(((Animal)object).nomeProperty());
		}
		
		if ( inputDataNascimento != null ){
			inputDataNascimento.valueProperty().bindBidirectional(((Animal)object).dataNascimentoProperty());
		}
		
		if ( inputRaca != null ){
			inputRaca.setItems(racaService.findAllAsObservableList());
			inputRaca.getSelectionModel().selectFirst();
			
	        // list of values showed in combo box drop down
			inputRaca.setCellFactory(new Callback<ListView<Raca>,ListCell<Raca>>(){
	            @Override
	            public ListCell<Raca> call(ListView<Raca> l){
	                return new ListCell<Raca>(){
	                    @Override
	                    protected void updateItem(Raca item, boolean empty) {
	                        super.updateItem(item, empty);
	                        if (item == null || empty) {
	                            setGraphic(null);
	                        } else {
	                            setText(item.getDescricao());
	                        }
	                    }
	                } ;
	            }
	        });
			//selected value showed in combo box
			inputRaca.setConverter(new StringConverter<Raca>() {
	              @Override
	              public String toString(Raca raca) {
	                if (raca == null){
	                  return null;
	                } else {
	                  return raca.getDescricao();
	                }
	              }

	            @Override
	            public Raca fromString(String raca) {
	                return null;
	            }
	        });
			
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
		racaController.showForm();
		if ( racaController.getObject() != null ){
			inputRaca.getItems().add((Raca)racaController.getObject());
			inputRaca.getSelectionModel().select((Raca)racaController.getObject());
		}
	}

}
