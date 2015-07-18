package br.com.milksys.controller.confirmacaoPrenhez;

import java.util.function.Function;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.TableCellHyperlinkRemoverFactory;
import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.ConfirmacaoPrenhez;
import br.com.milksys.model.MetodoConfirmacaoPrenhez;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.service.ConfirmacaoPrenhezService;
import br.com.milksys.service.IService;
import br.com.milksys.util.DateUtil;

@Controller
public class ConfirmacaoPrenhezFormController extends AbstractFormController<Integer, ConfirmacaoPrenhez> {

	@FXML private DatePicker       inputData;
	@FXML private Label            lblCobertura, lblPrevisaoParto, lblSituacaoCobertura;
	@FXML private ComboBox<String> inputSituacaoCobertura, inputMetodoConfirmacao;
	@FXML private UCTextField      inputObservacao;
	
	@FXML private TableView<ConfirmacaoPrenhez> table;
	@FXML private TableColumn<ConfirmacaoPrenhez, String> dataConfirmacaoColumn;
	@FXML private TableColumn<ConfirmacaoPrenhez, String> situacaoCoberturaColumn;
	@FXML private TableColumn<ConfirmacaoPrenhez, String> metodoConfirmacaoColumn;
	@FXML private TableColumn<ConfirmacaoPrenhez, String> removerColumn;
	
	private Cobertura cobertura;
	
	
	@FXML
	public void initialize() {
		
		lblCobertura.setText(cobertura.toString());
		lblPrevisaoParto.setText(DateUtil.format(cobertura.getPrevisaoParto()));
		lblSituacaoCobertura.setText(cobertura.getSituacaoCobertura());
		
		inputData.valueProperty().bindBidirectional(getObject().dataProperty());
		inputSituacaoCobertura.setItems(SituacaoCobertura.getItems());
		inputSituacaoCobertura.valueProperty().bindBidirectional(getObject().situacaoCoberturaProperty());
		inputMetodoConfirmacao.setItems(MetodoConfirmacaoPrenhez.getItems());
		inputMetodoConfirmacao.valueProperty().bindBidirectional(getObject().metodoConfirmacaoProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		
		
		removerColumn.setCellValueFactory(new PropertyValueFactory<ConfirmacaoPrenhez,String>("situacaoCobertura"));
		removerColumn.setCellFactory(new TableCellHyperlinkRemoverFactory<ConfirmacaoPrenhez, String>(removerConfirmacao, cobertura.getParto() != null ));
		
		table.setItems(((ConfirmacaoPrenhezService)service).findByCobertura(cobertura));
		
	}
	
	public Cobertura getCobertura() {
		return cobertura;
	}

	public void setCobertura(Cobertura cobertura) {
		this.cobertura = cobertura;
	}

	Function<Integer, Boolean> removerConfirmacao = index -> {
		
		if ( index <= table.getItems().size() ){

			service.remove(table.getSelectionModel().getSelectedItem());
			table.getItems().remove(index);
			
			return true;
		}
		return false;
	};
	
	
	@Override
	protected String getFormName() {
		return "view/cobertura/ConfirmacaoPrenhezForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Confirmação Prenhez";
	}

	@Override
	@Resource(name = "confirmacaoPrenhezService")
	protected void setService(IService<Integer, ConfirmacaoPrenhez> service) {
		super.setService(service);
	}
	
}
