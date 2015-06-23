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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
	@FXML private TableColumn<ProducaoIndividual, String> dataColumnTableForm;
	@FXML private TableColumn<ProducaoIndividual, String> animalColumnTableForm;
	@FXML private TableColumn<ProducaoIndividual, String> volumeColumnTableForm;
	
	@FXML private DatePicker inputData;
	@FXML private UCTextField inputObservacao;
	@FXML private NumberTextField inputVolume;
	
	@FXML private ComboBox<Animal> inputAnimal;
	
	@FXML private Button btnOk;
	@FXML private Button btnAdicionar;
	@FXML private Button btnRemover;
	
	@FXML protected TableView<ProducaoIndividual> tableForm;
	
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
			
			dataColumnTableForm.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getData())));
			animalColumnTableForm.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAnimal().getNumeroNome())));
			volumeColumnTableForm.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getVolume())));
			
			tableForm.setItems(producaoIndividualService.findByDate(((ProducaoIndividual)object).getData()));
			
			inputData.setDisable(true);
			btnOk.setText("Adicionar");
			btnOk.setOnAction(new EventHandler<ActionEvent>() {
				
			    @Override public void handle(ActionEvent e) {
			    	
			    	//atualiza o objeto com os dados da tela
			    	ProducaoIndividual pi = (ProducaoIndividual)object;
			    	pi.setAnimal(inputAnimal.getSelectionModel().getSelectedItem());
			    	pi.setData(DateUtil.asDate(inputData.getValue()));
			    	pi.setVolume(inputVolume.getNumber());
			    	pi.setObservacao(inputObservacao.getText());
			    	
			    	//caso já exista registro para o mesmo animal no mesmo dia remove o anterior
			    	for ( int index = 0; index < tableForm.getItems().size(); index++ ){
			    		ProducaoIndividual p = tableForm.getItems().get(index);
			    		
						if ( p.getData().equals(((ProducaoIndividual)object).getData()) && 
								p.getAnimal().getId() == pi.getAnimal().getId() ){
							
							//atualiza o volume para atualizar a table view
							p.setVolume(((ProducaoIndividual)object).getVolume());
							//seta o id para fazer update e não insert
							object = p;
							tableForm.getItems().set(index, p);
							break;
						}
						
					}
			    	
			    	//adiciona na tabela somente novos registros
			    	if ( ((ProducaoIndividual)object).getId() <= 0 )
			    		tableForm.getItems().add((ProducaoIndividual)object);
			    	
			    	//salva o objeto
			    	service.save((ProducaoIndividual)object);
			    	
			    	object = new ProducaoIndividual();
			    	
			    }
			    
			});			
			
			btnRemover.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			    	
			        object = tableForm.getSelectionModel().getSelectedItem();
			        
			        if ( object != null ){
			        	
		    			Alert alert = new Alert(AlertType.CONFIRMATION);
		    			alert.setTitle("Confirmação");
		    			alert.setHeaderText("Confirme a exclusão do registro");
		    			alert.setContentText("Tem certeza que deseja remover o registro selecionado?");
		    			Optional<ButtonType> result = alert.showAndWait();
		    			
		    			if (result.get() == ButtonType.OK) {
		    				service.remove((ProducaoIndividual)object);
		    				tableForm.getItems().remove(tableForm.getSelectionModel().getSelectedIndex());
		    				object = new ProducaoIndividual();
		    			}
		    			
			        }
			        
			    }
			});		
			
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) ){
			
			tableForm.setVisible(false);
			btnRemover.setVisible(false);
			tableForm.setMaxWidth(0);
			tableForm.setMaxHeight(0);
			btnRemover.setMaxHeight(0);
			btnRemover.setMaxWidth(0);
			
		}
		
	}
	
	@Override
	protected void showForm(int width, int height) {
		if ( state.equals(State.INSERT_TO_SELECT) ){
			super.showForm(500, 330);
		}else{
			super.showForm(500, 180);	
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
