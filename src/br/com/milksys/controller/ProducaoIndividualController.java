package br.com.milksys.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.CustomCellFactory;
import br.com.milksys.components.CustomStringConverter;
import br.com.milksys.components.NumberTextField;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Animal;
import br.com.milksys.model.ProducaoIndividual;
import br.com.milksys.model.State;
import br.com.milksys.service.AnimalService;
import br.com.milksys.service.IService;
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
	
	@Autowired private AnimalService animalService;

	@FXML@SuppressWarnings("unchecked")
	public void initialize() {
		
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
			inputAnimal.setCellFactory(new CustomCellFactory<>("numeroNome"));
			inputAnimal.setConverter(new CustomStringConverter("numeroNome"));
			
			inputAnimal.valueProperty().bindBidirectional(((ProducaoIndividual)object).animalProperty());
			inputData.valueProperty().bindBidirectional(((ProducaoIndividual)object).dataProperty());
			inputObservacao.textProperty().bindBidirectional(((ProducaoIndividual)object).observacaoProperty());
			inputVolume.textProperty().bindBidirectional(((ProducaoIndividual)object).volumeProperty());
			
		}
		
		if ( state.equals(State.INSERT_TO_SELECT) ){
			inputData.setDisable(true);
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

	@Override
	@Resource(name = "producaoIndividualService")
	protected void setService(IService<Integer, ProducaoIndividual> service) {
		super.setService(service);
	}

}
