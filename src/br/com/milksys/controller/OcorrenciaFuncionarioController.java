package br.com.milksys.controller;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Funcionario;
import br.com.milksys.model.MotivoOcorrenciaFuncionario;
import br.com.milksys.model.OcorrenciaFuncionario;
import br.com.milksys.model.State;
import br.com.milksys.service.FuncionarioService;
import br.com.milksys.service.MotivoOcorrenciaFuncionarioService;
import br.com.milksys.service.OcorrenciaFuncionarioService;

@Controller
public class OcorrenciaFuncionarioController extends AbstractController<Integer, OcorrenciaFuncionario> {

	@FXML private TableView<Funcionario> tableFuncionario; 
	@FXML private TableColumn<Funcionario, String> funcionarioListColumn;
	
	@FXML private TableColumn<Funcionario, String> funcionarioColumn;
	@FXML private TableColumn<MotivoOcorrenciaFuncionario, String> motivoColumn;
	@FXML private TableColumn<OcorrenciaFuncionario, LocalDate> dataColumn;
	@FXML private TableColumn<OcorrenciaFuncionario, String> descricaoColumn;
	
	@FXML private DatePicker inputData;
	@FXML private UCTextField inputDescricao;
	@FXML private UCTextField inputFuncionario;
	@FXML private ComboBox<MotivoOcorrenciaFuncionario> inputMotivo;
	@FXML private UCTextField inputJustificativa;
	
	@FXML private ComboBox<Funcionario> inputFuncionarioComboBox;
	
	@Autowired private FuncionarioService funcionarioService;
	@Autowired private OcorrenciaFuncionarioService ocorrenciaFuncionarioService;
	@Autowired private MotivoOcorrenciaFuncionarioService motivoOcorrenciaFuncionarioService;
	@Autowired private MotivoOcorrenciaFuncionarioController motivoOcorrenciaFuncionarioController;
	
	private Funcionario selectedFuncionario;
	
	@FXML
	public void initialize() {
		
		super.service = ocorrenciaFuncionarioService;
		
		if ( state.equals(State.LIST) ){
			
			//@TODO filtrar apenas animais femeas 
			tableFuncionario.setItems(funcionarioService.findAllAsObservableList());
			tableFuncionario.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> findByFuncionario(newValue));
			funcionarioListColumn.setCellValueFactory(new PropertyValueFactory<Funcionario, String>("nome"));
		
			funcionarioColumn.setCellValueFactory(new PropertyValueFactory<Funcionario, String>("funcionario"));
			motivoColumn.setCellValueFactory(new PropertyValueFactory<MotivoOcorrenciaFuncionario, String>("motivoOcorrenciaFuncionario"));
			dataColumn.setCellFactory(new TableCellDateFactory<OcorrenciaFuncionario, LocalDate>("data"));
			descricaoColumn.setCellValueFactory(new PropertyValueFactory<OcorrenciaFuncionario, String>("descricao"));
			
			super.initialize();
			
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) ){
			
			inputData.valueProperty().bindBidirectional(getObject().dataProperty());
			inputFuncionario.setText(selectedFuncionario.getNome());
			getObject().setFuncionario(selectedFuncionario);
			
			inputMotivo.valueProperty().bindBidirectional(getObject().motivoOcorrenciaFuncionarioProperty());
			inputMotivo.setItems(motivoOcorrenciaFuncionarioService.findAllAsObservableList());
			
			inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
			inputJustificativa.textProperty().bindBidirectional(getObject().justificativaProperty());
			
		}
		
	}
	
	
	@Override
	protected void initializeTableOverview() {
		data.clear();
		
		if (selectedFuncionario == null || selectedFuncionario.getId() <= 0) {
			if (tableFuncionario.getItems() != null	&& tableFuncionario.getItems().size() > 0) {
				table.getSelectionModel().select(0);
				selectedFuncionario = tableFuncionario.getItems().get(0);
				initializeTableOverview();
			}
		} else {
			data.addAll(ocorrenciaFuncionarioService.findByFuncionario(selectedFuncionario));
		}

		updateLabelNumRegistros();
			
	}

	private void findByFuncionario(Funcionario funcionario) {
		selectedFuncionario = funcionario;
		initializeTableOverview();
	}

	@Override
	protected boolean isInputValid() {
		
		return true;
	}

	@Override
	protected String getFormName() {
		return "view/ocorrenciaFuncionario/ocorrenciaFuncionarioForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Produção Individual";
	}

	@Override
	protected OcorrenciaFuncionario getObject() {
		return (OcorrenciaFuncionario) super.object;
	}
	
	@FXML
	protected void openFormMotivoOcorrenciaFuncionarioToInsertAndSelect() {
		motivoOcorrenciaFuncionarioController.state = State.INSERT_TO_SELECT;
		motivoOcorrenciaFuncionarioController.object = new MotivoOcorrenciaFuncionario();
		motivoOcorrenciaFuncionarioController.showForm(null);
		if ( motivoOcorrenciaFuncionarioController.getObject() != null ){
			inputMotivo.getItems().add(motivoOcorrenciaFuncionarioController.getObject());
			inputMotivo.getSelectionModel().select(motivoOcorrenciaFuncionarioController.getObject());
		}
	}

}
