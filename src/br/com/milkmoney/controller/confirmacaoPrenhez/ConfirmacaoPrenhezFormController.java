package br.com.milkmoney.controller.confirmacaoPrenhez;

import java.util.function.Function;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.components.TableCellHyperlinkRemoverFactory;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.ConfirmacaoPrenhez;
import br.com.milkmoney.model.MetodoConfirmacaoPrenhez;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.service.ConfirmacaoPrenhezService;

@Controller
public class ConfirmacaoPrenhezFormController extends AbstractFormController<Integer, ConfirmacaoPrenhez> {

	@FXML private DatePicker       inputData;
	@FXML private Label            lblCobertura;
	@FXML private ComboBox<String> inputSituacaoCobertura, inputMetodoConfirmacao;
	@FXML private UCTextField      inputObservacao;
	@FXML private Button           btnSalvar;
	
	@FXML private TableView<ConfirmacaoPrenhez> table;
	@FXML private TableColumn<ConfirmacaoPrenhez, String> dataConfirmacaoColumn;
	@FXML private TableColumn<ConfirmacaoPrenhez, String> situacaoCoberturaColumn;
	@FXML private TableColumn<ConfirmacaoPrenhez, String> metodoConfirmacaoColumn;
	@FXML private TableColumn<ConfirmacaoPrenhez, String> removerColumn;
	
	@Autowired ConfirmacaoPrenhezService service;
	
	@FXML
	public void initialize() {
		
		lblCobertura.setText(getObject().getCobertura().toString());
		
		inputData.valueProperty().bindBidirectional(getObject().dataProperty());
		inputSituacaoCobertura.setItems(SituacaoCobertura.getItems());
		inputSituacaoCobertura.valueProperty().bindBidirectional(getObject().situacaoCoberturaProperty());
		inputMetodoConfirmacao.setItems(MetodoConfirmacaoPrenhez.getItems());
		inputMetodoConfirmacao.valueProperty().bindBidirectional(getObject().metodoConfirmacaoProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		
		dataConfirmacaoColumn.setCellFactory(new TableCellDateFactory<ConfirmacaoPrenhez, String>("data"));
		situacaoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<ConfirmacaoPrenhez, String>("situacaoCobertura"));
		metodoConfirmacaoColumn.setCellValueFactory(new PropertyValueFactory<ConfirmacaoPrenhez, String>("metodoConfirmacao"));
		removerColumn.setCellValueFactory(new PropertyValueFactory<ConfirmacaoPrenhez,String>("situacaoCobertura"));
		removerColumn.setCellFactory(new TableCellHyperlinkRemoverFactory<ConfirmacaoPrenhez, String>(removerDiagnosticoPrenhez, getObject().getCobertura().getParto() != null));
		
		table.getItems().addAll(getObject().getCobertura().getConfirmacoesPrenhez());
		btnSalvar.setDisable(getObject().getCobertura().getParto() != null);
		super.service = service;
		closePopUpAfterSave(false);
	}
	
	@Override
	protected void afterSave() {
		getObject().getCobertura().getConfirmacoesPrenhez().add(getObject());
		table.getItems().add(getObject());
	};
	
	Function<Integer, Boolean> removerDiagnosticoPrenhez = index -> {
		
		if ( index <= table.getItems().size() ){
			
			if ( !getObject().getCobertura().getConfirmacoesPrenhez().remove(index) ){
				getObject().getCobertura().getConfirmacoesPrenhez().remove(table.getItems().get(index));
			}
			table.getItems().clear();
			table.getItems().addAll(getObject().getCobertura().getConfirmacoesPrenhez());
			return true;
			
		}
		return false;
	};
	
	@Override
	protected String getFormName() {
		return "view/confirmacaoPrenhez/ConfirmacaoPrenhezForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Confirmação Prenhez";
	}

}
