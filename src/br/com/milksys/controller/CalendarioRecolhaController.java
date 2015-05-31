package br.com.milksys.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.model.CalendarioRecolha;
import br.com.milksys.service.IService;

@Controller
public class CalendarioRecolhaController extends AbstractController<Integer, CalendarioRecolha> {

	@FXML private TableColumn<CalendarioRecolha, String> idColumn;
	@FXML private TableColumn<CalendarioRecolha, String> descricaoColumn;
	@FXML private TableColumn<CalendarioRecolha, String> dataInicioColumn;
	@FXML private TableColumn<CalendarioRecolha, String> dataFimColumn;
	@FXML private TableColumn<CalendarioRecolha, String> calendarioAtualColumn;
	@FXML private TableColumn<CalendarioRecolha, String> mesInicioColumn;
	@FXML private TextField inputDiaInicio;
	@FXML private TextField inputDiaFim;
	@FXML private ComboBox<String> inputCalendarioAtual;
	@FXML private ComboBox<String> inputMesInicio;
	@FXML private UCTextField inputDescricao;
	private ObservableList<String> optionsSimNao = FXCollections.observableArrayList("SIM", "NÃO");
	private ObservableList<String> optionsMesInicio = FXCollections.observableArrayList("ANTERIOR", "CORRENTE");
	
	@FXML
	public void initialize() {
		idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
		descricaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescricao()));
		dataInicioColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDiaInicio())));
		dataFimColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDiaFim())));
		calendarioAtualColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCalendarioVigente()));
		mesInicioColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMesInicio()));
		
		if ( inputDiaInicio != null ){
			inputDiaInicio.textProperty().bindBidirectional(((CalendarioRecolha)object).diaInicioProperty());
		}
		
		if ( inputDiaFim != null ){
			inputDiaFim.textProperty().bindBidirectional(((CalendarioRecolha)object).diaFimProperty());
		}
		
		if ( inputCalendarioAtual != null ){
			inputCalendarioAtual.setItems(optionsSimNao);
			inputCalendarioAtual.getSelectionModel().selectFirst();
			inputCalendarioAtual.valueProperty().bindBidirectional(((CalendarioRecolha)object).calendarioVigenteProperty());
		}
		
		if ( inputMesInicio != null ){
			inputMesInicio.setItems(optionsMesInicio);
			inputMesInicio.getSelectionModel().selectFirst();
			inputMesInicio.valueProperty().bindBidirectional(((CalendarioRecolha)object).mesInicioProperty());
		}
		
		if ( inputDescricao != null ){
			inputDescricao.textProperty().bindBidirectional(((CalendarioRecolha)object).descricaoProperty());
		}
		
		super.initialize();

	}

	@Override
	protected boolean isInputValid() {
		return true;
	}

	@Override
	protected String getFormName() {
		return "view/calendarioRecolha/CalendarioRecolhaForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Calendário Recolha";
	}

	@Override
	@Resource(name = "calendarioRecolhaService")
	protected void setService(IService<Integer, CalendarioRecolha> service) {
		super.setService(service);
	}

}
