package br.com.milksys.controller;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.NumberTextField;
import br.com.milksys.model.EntregaLeite;
import br.com.milksys.model.PrecoLeite;
import br.com.milksys.model.State;
import br.com.milksys.service.EntregaLeiteService;
import br.com.milksys.util.NumberFormatUtil;
import br.com.milksys.util.Util;

@Controller
public class EntregaLeiteController extends AbstractController<Integer, EntregaLeite> {


	@FXML private TableColumn<PrecoLeite, String> mesReferenciaColumn;
	@FXML private TableColumn<EntregaLeite, String> dataInicioColumn;
	@FXML private TableColumn<EntregaLeite, String> dataFimColumn;
	@FXML private TableColumn<EntregaLeite, String> volumeColumn;
	@FXML private TableColumn<EntregaLeite, String> valorMaximoPraticadoColumn;
	@FXML private TableColumn<EntregaLeite, String> valorRecebidoColumn;
	@FXML private TableColumn<EntregaLeite, String> valorTotalColumn;
	@FXML private TableColumn<EntregaLeite, String> observacaoColumn;
	
	@FXML private ComboBox<String> inputMesReferencia;
	@FXML private ComboBox<Number> inputAnoReferencia;
	@FXML private DatePicker inputDataInicio;
	@FXML private DatePicker inputDataFim;
	@FXML private NumberTextField inputVolume;
	@FXML private NumberTextField inputValorMaximoPraticado;
	@FXML private NumberTextField inputValorRecebido;
	@FXML private TextField inputObservacao;

	@FXML private Button btnIncrease;
	@FXML private Button btnDecrease;
	
	@FXML private Label lblAno;
	
	@Resource(name="entregaLeiteService")
	private EntregaLeiteService service;
	
	private int selectedAnoReferencia = LocalDate.now().getYear();
	private ObservableList<String> meses = Util.generateListMonths();
	private ObservableList<Number> anos = Util.generateListNumbers(1980, LocalDate.now().getYear());
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			//DateUtil.format(
			mesReferenciaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMesReferencia()));
			dataInicioColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDataInicio())));
			dataFimColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDataFim())));
			volumeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getVolume())));
			valorMaximoPraticadoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValorMaximoPraticado())));
			valorRecebidoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValorRecebido())));
			valorTotalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValorRecebido())));
			observacaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObservacao()));
			
			if ( !isInitialized ){
				super.service = this.service;
				configuraMesesEntregaAnoReferencia();
				super.initialize();
			}
			
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			
			if (state.equals(State.INSERT)){
				((EntregaLeite)object).setAnoReferencia(LocalDate.now().getYear());
				((EntregaLeite)object).setMesReferencia(meses.get(LocalDate.now().getMonthValue()-1));	
			}
			
			inputMesReferencia.setItems(meses);
			inputMesReferencia.getSelectionModel().select(LocalDate.now().getMonthValue()-1);
			inputMesReferencia.valueProperty().bindBidirectional(((EntregaLeite)object).mesReferenciaProperty());
			
			inputAnoReferencia.setItems(anos);
			inputAnoReferencia.getSelectionModel().selectLast();
			inputAnoReferencia.valueProperty().bindBidirectional(((EntregaLeite)object).anoReferenciaProperty());
			
			inputVolume.textProperty().bindBidirectional(((EntregaLeite)object).volumeProperty());
			
			inputValorMaximoPraticado.textProperty().bindBidirectional(((EntregaLeite)object).valorMaximoPraticadoProperty());
			inputValorRecebido.textProperty().bindBidirectional(((EntregaLeite)object).valorRecebidoProperty());
			
			inputObservacao.textProperty().bindBidirectional(((EntregaLeite)object).observacaoProperty());
			
		}
		
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleIncreaseAnoReferencia() {
		selectedAnoReferencia++;
		configuraMesesEntregaAnoReferencia();
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleDecreaseAnoReferencia() {
		selectedAnoReferencia--;
		configuraMesesEntregaAnoReferencia();
	}
	
	@Override
	protected void initializeTableOverview() {
		super.data.clear();
		super.data.addAll(service.findAllByAnoAsObservableList(selectedAnoReferencia));
	}
	
	private void configuraMesesEntregaAnoReferencia(){
		
		for (int i = 0; i < meses.size(); i++) {
			EntregaLeite el = new EntregaLeite(meses.get(i), selectedAnoReferencia);
			service.save(el);
		}

		this.initializeTableOverview();
		table.setItems(data);
		lblAno.setText(String.valueOf(selectedAnoReferencia));
		
	}
	
	protected boolean isInputValid() {
		return true;
	}

	@Override
	protected String getFormName() {
		return "view/entregaLeite/EntregaLeiteForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Entrega Leite";
	}
	
}
