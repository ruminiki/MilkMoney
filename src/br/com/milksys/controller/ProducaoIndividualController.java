package br.com.milksys.controller;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.PropertyDecimalValueFactory;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Animal;
import br.com.milksys.model.ProducaoIndividual;
import br.com.milksys.model.State;
import br.com.milksys.service.AnimalService;
import br.com.milksys.service.IService;
import br.com.milksys.service.ProducaoIndividualService;
import br.com.milksys.util.DateUtil;
import br.com.milksys.util.NumberFormatUtil;

@Controller
public class ProducaoIndividualController extends AbstractController<Integer, ProducaoIndividual> {

	@FXML private TableView<Animal> tableAnimal; 
	@FXML private TableColumn<Animal, String> animalListColumn;
	
	@FXML private TableColumn<Animal, String> animalColumn;
	@FXML private TableColumn<ProducaoIndividual, LocalDate> dataColumn;
	@FXML private TableColumn<ProducaoIndividual, String> primeiraOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> segundaOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> terceiraOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> observacaoColumn;
	@FXML private TableColumn<ProducaoIndividual, String> valorColumn;
	
	@FXML private DatePicker inputData;
	@FXML private UCTextField inputObservacao;
	@FXML private UCTextField inputAnimal;
	@FXML private TextField inputPrimeiraOrdenha;
	@FXML private TextField inputSegundaOrdenha;
	@FXML private TextField inputTerceiraOrdenha;
	
	@FXML private ComboBox<Animal> inputAnimalComboBox;
	
	@Autowired private AnimalService animalService;
	
	private Animal selectedAnimal;
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			//@TODO filtrar apenas animais femeas 
			tableAnimal.setItems(animalService.findAllAsObservableList());
			tableAnimal.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> findByAnimal(newValue));
			animalListColumn.setCellValueFactory(new PropertyValueFactory<Animal, String>("numeroNome"));
		
			animalColumn.setCellValueFactory(new PropertyValueFactory<Animal, String>("animal"));
			dataColumn.setCellFactory(new TableCellDateFactory<ProducaoIndividual, LocalDate>("data"));
			primeiraOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("primeiraOrdenha"));
			segundaOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("segundaOrdenha"));
			terceiraOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("terceiraOrdenha"));
			valorColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("valor"));
			observacaoColumn.setCellValueFactory(new PropertyValueFactory<ProducaoIndividual, String>("observacao"));
			
			super.initialize();
			
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			
			inputData.valueProperty().bindBidirectional(getObject().dataProperty());
			if ( selectedAnimal != null && inputAnimal != null )
				inputAnimal.setText(selectedAnimal.getNumeroNome());
			getObject().setAnimal(selectedAnimal);
			inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
			inputPrimeiraOrdenha.textProperty().bindBidirectional(getObject().primeiraOrdenhaProperty());
			inputSegundaOrdenha.textProperty().bindBidirectional(getObject().segundaOrdenhaProperty());
			inputTerceiraOrdenha.textProperty().bindBidirectional(getObject().terceiraOrdenhaProperty());
			
			MaskFieldUtil.numeroInteiro(inputPrimeiraOrdenha);
			MaskFieldUtil.numeroInteiro(inputSegundaOrdenha);
			MaskFieldUtil.numeroInteiro(inputTerceiraOrdenha);
			
		}
		
		if ( state.equals(State.INSERT_TO_SELECT) ){
			
			animalColumn.setCellValueFactory(new PropertyValueFactory<Animal, String>("animal"));
			dataColumn.setCellFactory(new TableCellDateFactory<ProducaoIndividual, LocalDate>("data"));
			primeiraOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("primeiraOrdenha"));
			segundaOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("segundaOrdenha"));
			terceiraOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("terceiraOrdenha"));
			valorColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("valor"));

			inputAnimalComboBox.setItems(animalService.findAllAsObservableList());
			
			refreshTableOverview();
			
		}
		
	}
	
	
	@Override
	protected void refreshTableOverview() {
		data.clear();
		
		if ( state.equals(State.LIST) ){
			
			if ( selectedAnimal == null || selectedAnimal.getId() <= 0 ){
				if ( tableAnimal.getItems() != null && tableAnimal.getItems().size() > 0 ){
					table.getSelectionModel().select(0);
					selectedAnimal = tableAnimal.getItems().get(0);
					refreshTableOverview();
				}	
			}else{
				data.addAll(((ProducaoIndividualService)service).findByAnimal(selectedAnimal));
			}
			
			updateLabelNumRegistros();
			
		}
		
		if ( state.equals(State.INSERT_TO_SELECT) ){
			
			data.clear();
			data.addAll(((ProducaoIndividualService)service).findByDate(getObject().getData()));
			table.setItems(data);
			
		}
		
		((ProducaoIndividualService)service).atualizaValorProducao(data);
		
	}
	
	private void findByAnimal(Animal animal) {
		selectedAnimal = animal;
		refreshTableOverview();
	}

	@Override
	protected void handleSave() {
		if ( state.equals(State.INSERT_TO_SELECT) ){
			closePopUpAfterSave = false;
		}
		setObject(((ProducaoIndividualService)service).beforeSave(getObject()));
		super.handleSave();
    }
	
	/**
	 * Chamado pelo form ProducaoIndividualExternalForm.fxml
	 */
	@FXML
	private void handleAdicionar(){
		
		getObject().setAnimal(inputAnimalComboBox.getSelectionModel().getSelectedItem());
		getObject().setPrimeiraOrdenha(NumberFormatUtil.fromString(inputPrimeiraOrdenha.getText()));
		getObject().setSegundaOrdenha(NumberFormatUtil.fromString(inputSegundaOrdenha.getText()));
		getObject().setTerceiraOrdenha(NumberFormatUtil.fromString(inputTerceiraOrdenha.getText()));
		getObject().setObservacao(inputObservacao.getText());
		
		handleSave();
		
		setObject(new ProducaoIndividual(getObject().getData()));

		//limpa a tela
		inputAnimalComboBox.getSelectionModel().select(null);
		inputData.setValue(DateUtil.asLocalDate(getObject().getData()));
		inputObservacao.setText(null);
		inputPrimeiraOrdenha.setText(null);
		inputSegundaOrdenha.setText(null);
		inputTerceiraOrdenha.setText(null);
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
	protected ProducaoIndividual getObject() {
		return (ProducaoIndividual) super.object;
	}
	
	@Override
	@Resource(name = "producaoIndividualService")
	protected void setService(IService<Integer, ProducaoIndividual> service) {
		super.setService(service);
	}

}
