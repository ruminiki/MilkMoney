package br.com.milksys.controller.producaoIndividual;

import java.time.LocalDate;
import java.util.Date;

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
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.ProducaoIndividual;
import br.com.milksys.service.AnimalService;
import br.com.milksys.service.IService;
import br.com.milksys.service.ProducaoIndividualService;
import br.com.milksys.util.DateUtil;
import br.com.milksys.util.NumberFormatUtil;

@Controller
public class ProducaoIndividualExternalFormController extends AbstractFormController<Integer, ProducaoIndividual> {

	@FXML private TableView<ProducaoIndividual> table;
	@FXML private TableColumn<Animal, String> animalColumn;
	@FXML private TableColumn<ProducaoIndividual, LocalDate> dataColumn;
	@FXML private TableColumn<ProducaoIndividual, String> primeiraOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> segundaOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> terceiraOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> valorColumn;
	
	@FXML private DatePicker inputData;
	@FXML private UCTextField inputObservacao;
	@FXML private ComboBox<Animal> inputAnimal;
	@FXML private TextField inputPrimeiraOrdenha;
	@FXML private TextField inputSegundaOrdenha;
	@FXML private TextField inputTerceiraOrdenha;
	
	@Autowired private AnimalService animalService;
	@Autowired private ProducaoIndividualService service;
	
	private Date selectedDate;
	
	@FXML
	public void initialize() {
		
		inputData.valueProperty().bindBidirectional(getObject().dataProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		inputPrimeiraOrdenha.textProperty().bindBidirectional(getObject().primeiraOrdenhaProperty());
		inputSegundaOrdenha.textProperty().bindBidirectional(getObject().segundaOrdenhaProperty());
		inputTerceiraOrdenha.textProperty().bindBidirectional(getObject().terceiraOrdenhaProperty());
		
		MaskFieldUtil.numeroInteiro(inputPrimeiraOrdenha);
		MaskFieldUtil.numeroInteiro(inputSegundaOrdenha);
		MaskFieldUtil.numeroInteiro(inputTerceiraOrdenha);
		
		animalColumn.setCellValueFactory(new PropertyValueFactory<Animal, String>("animal"));
		dataColumn.setCellFactory(new TableCellDateFactory<ProducaoIndividual, LocalDate>("data"));
		primeiraOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("primeiraOrdenha"));
		segundaOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("segundaOrdenha"));
		terceiraOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("terceiraOrdenha"));
		valorColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("valor"));

		inputAnimal.setItems(animalService.findAllAsObservableList());
		table.setItems(((ProducaoIndividualService)service).findByDate(selectedDate));
		
		refreshTableOverview();
		
	}
	
	protected void refreshTableOverview() {
		table.getItems().clear();
		table.getItems().addAll(((ProducaoIndividualService)service).findByDate(selectedDate));
		((ProducaoIndividualService)service).atualizaValorProducao(table.getItems());
	}
	
	@Override
	protected void handleSave() {
		super.setClosePopUpAfterSave(false);
		super.setObject(((ProducaoIndividualService)service).beforeSave(getObject()));
		super.handleSave();
    }
	
	/**
	 * Chamado pelo form ProducaoIndividualExternalForm.fxml
	 */
	@FXML
	private void handleAdicionar(){
		
		getObject().setAnimal(inputAnimal.getSelectionModel().getSelectedItem());
		getObject().setPrimeiraOrdenha(NumberFormatUtil.fromString(inputPrimeiraOrdenha.getText()));
		getObject().setSegundaOrdenha(NumberFormatUtil.fromString(inputSegundaOrdenha.getText()));
		getObject().setTerceiraOrdenha(NumberFormatUtil.fromString(inputTerceiraOrdenha.getText()));
		getObject().setObservacao(inputObservacao.getText());
		
		handleSave();
		
		setObject(new ProducaoIndividual(getObject().getData()));

		//limpa a tela
		inputAnimal.getSelectionModel().select(null);
		inputData.setValue(DateUtil.asLocalDate(getObject().getData()));
		inputObservacao.setText(null);
		inputPrimeiraOrdenha.setText(null);
		inputSegundaOrdenha.setText(null);
		inputTerceiraOrdenha.setText(null);
	}
	
	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	@Override
	protected String getFormName() {
		return "view/producaoIndividual/ProducaoIndividualExternalForm.fxml";
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
