package br.com.milkmoney.controller.precoLeite;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.PropertyDecimalValueFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.PrecoLeite;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.PrecoLeiteService;

@Controller
public class PrecoLeiteOverviewController extends AbstractOverviewController<Integer, PrecoLeite> {

	@FXML private TableColumn<PrecoLeite, String> mesReferenciaColumn;
	@FXML private TableColumn<PrecoLeite, String> anoReferenciaColumn;
	@FXML private TableColumn<PrecoLeite, String> valorMaximoPraticadoColumn;
	@FXML private TableColumn<PrecoLeite, String> valorRecebidoColumn;
	
	@FXML private Button btnIncrease;
	@FXML private Button btnDecrease;
	
	@FXML private Label lblAno;
	
	private int selectedAnoReferencia = LocalDate.now().getYear();
	
	@Autowired private PrecoLeiteService service;
	@Autowired private PrecoLeiteFormController formController;
	
	@FXML
	public void initialize() {
		
		mesReferenciaColumn.setCellValueFactory(new PropertyValueFactory<PrecoLeite,String>("mesReferencia"));
		anoReferenciaColumn.setCellValueFactory(new PropertyValueFactory<PrecoLeite,String>("anoReferencia"));
		valorMaximoPraticadoColumn.setCellValueFactory(new PropertyDecimalValueFactory<PrecoLeite, String>("valorMaximoPraticado", 3));
		valorRecebidoColumn.setCellValueFactory(new PropertyDecimalValueFactory<PrecoLeite, String>("valorRecebido", 3));
		
		super.service = this.service;
		((PrecoLeiteService)service).configuraMesesAnoReferencia(selectedAnoReferencia);
		this.refreshTableOverview();
		table.setItems(data);
		lblAno.setText(String.valueOf(selectedAnoReferencia));
		super.initialize(formController);
		
	}
	
	@Override
	protected void refreshTableOverview() {
		super.data.clear();
		super.data.addAll(service.findAllByAnoAsObservableList(selectedAnoReferencia));
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleIncreaseAnoReferencia() {
		selectedAnoReferencia++;
		((PrecoLeiteService)service).configuraMesesAnoReferencia(selectedAnoReferencia);
		this.refreshTableOverview();
		lblAno.setText(String.valueOf(selectedAnoReferencia));
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleDecreaseAnoReferencia() {
		selectedAnoReferencia--;
		((PrecoLeiteService)service).configuraMesesAnoReferencia(selectedAnoReferencia);
		this.refreshTableOverview();
		lblAno.setText(String.valueOf(selectedAnoReferencia));
	}
	
	@Override
	public String getFormTitle() {
		return "Preço Leite";
	}
	
	@Override
	public String getFormName() {
		return "view/precoLeite/PrecoLeiteOverview.fxml";
	}
	
	@Override
	@Resource(name = "precoLeiteService")
	protected void setService(IService<Integer, PrecoLeite> service) {
		super.setService(service);
	}

}
