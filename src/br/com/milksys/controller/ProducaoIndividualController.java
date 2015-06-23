package br.com.milksys.controller;

import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.CustomComboBoxCellFactory;
import br.com.milksys.components.CustomStringConverter;
import br.com.milksys.components.NumberTextField;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Animal;
import br.com.milksys.model.ProducaoIndividual;
import br.com.milksys.model.State;
import br.com.milksys.service.AnimalService;
import br.com.milksys.service.ProducaoIndividualService;
import br.com.milksys.util.DateUtil;
import br.com.milksys.util.NumberFormatUtil;

@Controller
public class ProducaoIndividualController extends AbstractController<Integer, ProducaoIndividual> {

	@FXML private TableColumn<ProducaoIndividual, String> dataColumn;
	@FXML private TableColumn<ProducaoIndividual, String> animalColumn;
	@FXML private TableColumn<ProducaoIndividual, String> volumeColumn;
	@FXML private TableColumn<ProducaoIndividual, String> observacaoColumn;
	
	@FXML private DatePicker inputData;
	@FXML private UCTextField inputObservacao;
	@FXML private NumberTextField inputVolume;
	
	@FXML private ComboBox<Animal> inputAnimal;
	
	@FXML private Button btnOk;
	@FXML private Button btnAdicionar;
	@FXML private Button btnRemover;
	
	@FXML private ListView<ProducaoIndividual> inputList;
	
	@Autowired private AnimalService animalService;
	@Autowired private ProducaoIndividualService producaoIndividualService;

	@FXML@SuppressWarnings("unchecked")
	public void initialize() {
		
		super.service = producaoIndividualService;
		
		if ( state.equals(State.LIST) ){
			
			dataColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getData())));
			animalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAnimal().getNumeroNome())));
			volumeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getVolume())));
			observacaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObservacao()));
			
			super.initialize();
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			
			//@TODO filtrar apenas animais femeas 
			inputAnimal.setItems(animalService.findAllAsObservableList());
			inputAnimal.getSelectionModel().selectFirst();
			inputAnimal.setCellFactory(new CustomComboBoxCellFactory<>("numeroNome"));
			inputAnimal.setConverter(new CustomStringConverter("numeroNome"));
			
			inputAnimal.valueProperty().bindBidirectional(((ProducaoIndividual)object).animalProperty());
			inputData.valueProperty().bindBidirectional(((ProducaoIndividual)object).dataProperty());
			inputObservacao.textProperty().bindBidirectional(((ProducaoIndividual)object).observacaoProperty());
			inputVolume.textProperty().bindBidirectional(((ProducaoIndividual)object).volumeProperty());
			
		}
		
		if ( state.equals(State.INSERT_TO_SELECT) ){
			
			inputList.setCellFactory(new Callback<ListView<ProducaoIndividual>, ListCell<ProducaoIndividual>>(){
	            @Override
	            public ListCell<ProducaoIndividual> call(ListView<ProducaoIndividual> p) {
	                ListCell<ProducaoIndividual> cell = new ListCell<ProducaoIndividual>(){
	                    @Override
	                    protected void updateItem(ProducaoIndividual p, boolean bln) {
	                        super.updateItem(p, bln);
	                        if (p != null) {
	                            setText(p.getAnimal().getNumeroNome()
	                            		+ " - " + NumberFormatUtil.decimalFormat(p.getVolume()) + " (litros)");
	                        }
	                    }
	                };
	                return cell;
	            }
	        });
			
			inputList.setItems(producaoIndividualService.findByDate(((ProducaoIndividual)object).getData()));
			
			inputData.setDisable(true);
			btnOk.setText("Adicionar");
			btnOk.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			    	ProducaoIndividual pi = (ProducaoIndividual)object;
			    	pi.setAnimal(inputAnimal.getSelectionModel().getSelectedItem());
			    	pi.setData(DateUtil.asDate(inputData.getValue()));
			    	pi.setVolume(inputVolume.getNumber());
			    	pi.setObservacao(inputObservacao.getText());
			    	service.save((ProducaoIndividual)object);
			    	inputList.setItems(producaoIndividualService.findByDate(((ProducaoIndividual)object).getData()));
			    	object = new ProducaoIndividual();
			    }
			});			
			
			btnRemover.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			    	int index = inputList.getSelectionModel().getSelectedIndex();
			        object = inputList.getSelectionModel().getSelectedItem();
			        if ( object != null ){
		    			Alert alert = new Alert(AlertType.CONFIRMATION);
		    			alert.setTitle("Confirmação");
		    			alert.setHeaderText("Confirme a exclusão do registro");
		    			alert.setContentText("Tem certeza que deseja remover o registro selecionado?");
		    			Optional<ButtonType> result = alert.showAndWait();
		    			if (result.get() == ButtonType.OK) {
		    				service.remove((ProducaoIndividual)object);
		    				inputList.getItems().remove(index);
		    				object = new ProducaoIndividual();
		    			}
			        }
			    }
			});		
			
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) ){
			inputList.setVisible(false);
			btnRemover.setVisible(false);
			inputList.setMaxWidth(0);
			inputList.setMaxHeight(0);
			((HBox)btnRemover.getParent()).setMaxHeight(0);
			((HBox)btnRemover.getParent()).setMaxWidth(0);
		}
		
	}
	
	@Override
	protected void showForm(int width, int height) {
		if ( state.equals(State.INSERT_TO_SELECT) ){
			super.showForm(447, 300);
		}else{
			super.showForm(447, 180);	
		}
		
	}

	@Override
	protected boolean isInputValid() {
		return true;
	}

	@Override
	protected String getFormName() {
		return "view/producaoIndividual/ProducaoIndividualForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Produção Individual";
	}

}
