package br.com.milkmoney.controller.confirmacaoPrenhes;

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
import br.com.milkmoney.model.ConfirmacaoPrenhes;
import br.com.milkmoney.model.MetodoConfirmacaoPrenhes;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.service.ConfirmacaoPrenhesService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class ConfirmacaoPrenhesFormController extends AbstractFormController<Integer, ConfirmacaoPrenhes> {

	@FXML private DatePicker       inputData;
	@FXML private Label            lblCobertura;
	@FXML private ComboBox<String> inputSituacaoCobertura, inputMetodoConfirmacao;
	@FXML private UCTextField      inputObservacao;
	@FXML private Button           btnSalvar;
	
	@FXML private TableView<ConfirmacaoPrenhes> table;
	@FXML private TableColumn<ConfirmacaoPrenhes, String> dataConfirmacaoColumn;
	@FXML private TableColumn<ConfirmacaoPrenhes, String> situacaoCoberturaColumn;
	@FXML private TableColumn<ConfirmacaoPrenhes, String> metodoConfirmacaoColumn;
	@FXML private TableColumn<ConfirmacaoPrenhes, String> removerColumn;
	
	@Autowired ConfirmacaoPrenhesService service;
	
	@FXML
	public void initialize() {
		
		lblCobertura.setText(getObject().getCobertura().toString());
		
		inputData.valueProperty().bindBidirectional(getObject().dataProperty());
		inputData.setValue(DateUtil.asLocalDate(getObject().getCobertura().getData()).plusDays(60));
		inputSituacaoCobertura.setItems(SituacaoCobertura.getItems());
		inputSituacaoCobertura.valueProperty().bindBidirectional(getObject().situacaoCoberturaProperty());
		inputMetodoConfirmacao.setItems(MetodoConfirmacaoPrenhes.getItems());
		inputMetodoConfirmacao.valueProperty().bindBidirectional(getObject().metodoConfirmacaoProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		
		dataConfirmacaoColumn.setCellFactory(new TableCellDateFactory<ConfirmacaoPrenhes, String>("data"));
		situacaoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<ConfirmacaoPrenhes, String>("situacaoCobertura"));
		metodoConfirmacaoColumn.setCellValueFactory(new PropertyValueFactory<ConfirmacaoPrenhes, String>("metodoConfirmacao"));
		removerColumn.setCellValueFactory(new PropertyValueFactory<ConfirmacaoPrenhes,String>("situacaoCobertura"));
		removerColumn.setCellFactory(new TableCellHyperlinkRemoverFactory<ConfirmacaoPrenhes, String>(removerDiagnosticoPrenhes, getObject().getCobertura().getParto() != null));
		
		table.getItems().addAll(getObject().getCobertura().getConfirmacoesPrenhes());
		btnSalvar.setDisable(getObject().getCobertura().getParto() != null);
		super.service = service;
		closePopUpAfterSave(false);
	}
	
	@Override
	protected void afterSave() {
		getObject().getCobertura().getConfirmacoesPrenhes().add(getObject());
		table.getItems().add(getObject());
	};
	
	Function<Integer, Boolean> removerDiagnosticoPrenhes = index -> {
		
		if ( index <= table.getItems().size() ){
			
			if ( !getObject().getCobertura().getConfirmacoesPrenhes().remove(index) ){
				getObject().getCobertura().getConfirmacoesPrenhes().remove(table.getItems().get(index));
			}
			table.getItems().clear();
			table.getItems().addAll(getObject().getCobertura().getConfirmacoesPrenhes());
			return true;
			
		}
		return false;
	};
	
	@Override
	protected String getFormName() {
		return "view/confirmacaoPrenhes/ConfirmacaoPrenhesForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Confirma��o Prenhes";
	}

}