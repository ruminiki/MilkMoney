package br.com.milksys.controller.producaoIndividual;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.components.CustomAlert;
import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.PropertyDecimalValueFactory;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.controller.animal.AnimalReducedOverviewController;
import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Animal;
import br.com.milksys.model.ProducaoIndividual;
import br.com.milksys.model.Sexo;
import br.com.milksys.service.AnimalService;
import br.com.milksys.service.IService;
import br.com.milksys.service.ProducaoIndividualService;
import br.com.milksys.service.searchers.SearchFemeasEmLactacao;
import br.com.milksys.util.DateUtil;
import br.com.milksys.util.NumberFormatUtil;

@Controller
public class ProducaoIndividualExternalFormController extends AbstractFormController<Integer, ProducaoIndividual> {

	@FXML private TableView<ProducaoIndividual> table;
	@FXML private TableColumn<Animal, String> animalColumn;
	@FXML private TableColumn<ProducaoIndividual, LocalDate> dataColumn;
	@FXML private TableColumn<ProducaoIndividual, BigDecimal> primeiraOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, BigDecimal> segundaOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, BigDecimal> terceiraOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, BigDecimal> valorColumn;
	
	@FXML private DatePicker inputData;
	@FXML private UCTextField inputObservacao, inputAnimal;
	@FXML private TextField inputPrimeiraOrdenha;
	@FXML private TextField inputSegundaOrdenha;
	@FXML private TextField inputTerceiraOrdenha;
	
	@Autowired private AnimalService animalService;
	@Autowired private ProducaoIndividualService service;
	
	@Autowired private AnimalReducedOverviewController animalReducedOverviewController;
	
	private Date selectedDate;
	
	@FXML
	public void initialize() {
		
		inputData.valueProperty().set(DateUtil.asLocalDate(getObject().getData()));
		
		MaskFieldUtil.numeroInteiro(inputPrimeiraOrdenha);
		MaskFieldUtil.numeroInteiro(inputSegundaOrdenha);
		MaskFieldUtil.numeroInteiro(inputTerceiraOrdenha);
		
		animalColumn.setCellValueFactory(new PropertyValueFactory<Animal, String>("animal"));
		dataColumn.setCellFactory(new TableCellDateFactory<ProducaoIndividual, LocalDate>("data"));
		primeiraOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, BigDecimal>("primeiraOrdenha"));
		segundaOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, BigDecimal>("segundaOrdenha"));
		terceiraOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, BigDecimal>("terceiraOrdenha"));
		valorColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, BigDecimal>("valor"));

		if ( getObject().getAnimal() != null ){
			inputAnimal.setText(getObject().getAnimal().toString());
		}
		
		table.setItems(((ProducaoIndividualService)service).findByDate(selectedDate));
		
		refreshTableOverview();
		
	}
	
	protected void refreshTableOverview() {
		table.getItems().clear();
		table.getItems().addAll(((ProducaoIndividualService)service).findByDate(selectedDate));
		((ProducaoIndividualService)service).atualizaValorProducao(table.getItems());
	}
	
	@FXML
	protected void handleSelecionarFemea() {
		
		animalReducedOverviewController.setObject(new Animal(Sexo.FEMEA));
		animalReducedOverviewController.setSearch((SearchFemeasEmLactacao)MainApp.getBean(SearchFemeasEmLactacao.class), getObject().getData());
		
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, true);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, true);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.REMOVE_DISABLED, true);
		
		animalReducedOverviewController.showForm();
		
		if ( animalReducedOverviewController.getObject() != null && animalReducedOverviewController.getObject().getId() > 0 ){
			getObject().setAnimal(animalReducedOverviewController.getObject());
		}
		
		if ( getObject().getAnimal() != null ){
			inputAnimal.setText(getObject().getAnimal().getNumeroNome());
		}else{
			inputAnimal.setText("");
		}
		
	}
	
	@Override
	protected void handleSave() {
		super.setClosePopUpAfterSave(false);
		super.setObject(((ProducaoIndividualService)service).beforeSave(getObject()));
		
		try {
			service.save(getObject());
			refreshTableOverview();
		} catch (ValidationException e) {
			CustomAlert.mensagemAlerta(e.getTipo(), e.getMessage());
		}
		
    }
	
	@FXML
	private void handleDelete(){
		int selectedIndex = table.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Optional<ButtonType> result = CustomAlert.confirmarExclusao();
			if (result.get() == ButtonType.OK) {
				
				try {
					service.remove(table.getSelectionModel().getSelectedItem());
				} catch (Exception e) {
					CustomAlert.mensagemAlerta("", e.getMessage());
					return;
				}
				
				table.getItems().remove(selectedIndex);
			}
		} else {
			CustomAlert.nenhumRegistroSelecionado();		
		}
	}
	
	/**
	 * Chamado pelo form ProducaoIndividualExternalForm.fxml
	 */
	@FXML
	private void handleAdicionar(){
		
		getObject().setPrimeiraOrdenha(NumberFormatUtil.fromString(inputPrimeiraOrdenha.getText()));
		getObject().setSegundaOrdenha(NumberFormatUtil.fromString(inputSegundaOrdenha.getText()));
		getObject().setTerceiraOrdenha(NumberFormatUtil.fromString(inputTerceiraOrdenha.getText()));
		getObject().setObservacao(inputObservacao.getText());
		
		handleSave();
		
		setObject(new ProducaoIndividual(getObject().getData()));

		//limpa a tela
		inputAnimal.clear();
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
