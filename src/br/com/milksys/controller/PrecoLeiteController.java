package br.com.milksys.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.NumberTextField;
import br.com.milksys.model.PrecoLeite;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;
import br.com.milksys.service.PrecoLeiteService;
import br.com.milksys.util.NumberFormatUtil;
import br.com.milksys.util.Util;

@Controller
public class PrecoLeiteController extends AbstractController<Integer, PrecoLeite> {

	@FXML private TableColumn<PrecoLeite, String> mesReferenciaColumn;
	@FXML private TableColumn<PrecoLeite, String> anoReferenciaColumn;
	@FXML private TableColumn<PrecoLeite, String> valorColumn;
	
	@FXML private NumberTextField inputValor;
	@FXML private ComboBox<String> inputMesReferencia;
	@FXML private ComboBox<Number> inputAnoReferencia;
	
	@FXML private Button btnIncrease;
	@FXML private Button btnDecrease;
	
	@FXML private Label lblAno;
	
	private int selectedAnoReferencia = LocalDate.now().getYear();
	
	private ObservableList<String> meses = Util.generateListMonths();
	private ObservableList<Number> anos = Util.generateListNumbers(1980, LocalDate.now().getYear());

	@Autowired private PrecoLeiteService service;
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			mesReferenciaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMesReferencia()));
			anoReferenciaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAnoReferencia())));
			valorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValor())));
			super.service = this.service;
			configuraMesesAnoReferencia();
			super.initialize();
			
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			
			if (state.equals(State.INSERT)){
				((PrecoLeite)object).setAnoReferencia(LocalDate.now().getYear());
				((PrecoLeite)object).setMesReferencia(meses.get(LocalDate.now().getMonthValue()-1));	
			}
			
			inputMesReferencia.setItems(meses);
			inputMesReferencia.getSelectionModel().select(LocalDate.now().getMonthValue()-1);
			inputMesReferencia.valueProperty().bindBidirectional(((PrecoLeite)object).mesReferenciaProperty());
			
			inputAnoReferencia.setItems(anos);
			inputAnoReferencia.getSelectionModel().selectLast();
			inputAnoReferencia.valueProperty().bindBidirectional(((PrecoLeite)object).anoReferenciaProperty());
			
			inputValor.textProperty().bindBidirectional(((PrecoLeite)object).valorProperty());
			
		}
		
		//permite cadastrar apenas para o mês especificado na tela que invoca o cadastro
		if ( state.equals(State.INSERT_TO_SELECT) ){
			inputMesReferencia.setDisable(true);
			inputAnoReferencia.setDisable(true);
		}
		
	}
	
	@Override
	protected void initializeTableOverview() {
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
		configuraMesesAnoReferencia();
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleDecreaseAnoReferencia() {
		selectedAnoReferencia--;
		configuraMesesAnoReferencia();
	}
	
	/**
	 * Configura os meses para registro dos preços.
	 */
	private void configuraMesesAnoReferencia(){
		
		for (int i = 0; i < meses.size(); i++) {
			
			PrecoLeite precoLeite = service.findByMesAno(meses.get(i), selectedAnoReferencia);
			if ( precoLeite == null ){
				precoLeite = new PrecoLeite(meses.get(i), i+1, selectedAnoReferencia, BigDecimal.ZERO);
				service.save(precoLeite);
			}
			
		}

		this.initializeTableOverview();
		table.setItems(data);
		lblAno.setText(String.valueOf(selectedAnoReferencia));
		
	}
	
	
	@Override
	protected boolean isInputValid() {
		return true;
	}

	@Override
	protected String getFormName() {
		return "view/precoLeite/PrecoLeiteForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Preço Leite";
	}

	@Override
	@Resource(name = "precoLeiteService")
	protected void setService(IService<Integer, PrecoLeite> service) {
		super.setService(service);
	}

}
