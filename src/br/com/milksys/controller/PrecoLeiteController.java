package br.com.milksys.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

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
	@FXML private TableColumn<PrecoLeite, String> valorMaximoPraticadoColumn;
	@FXML private TableColumn<PrecoLeite, String> valorRecebidoColumn;
	
	@FXML private NumberTextField inputValorMaximoPraticado;
	@FXML private NumberTextField inputValorRecebido;
	@FXML private TextField inputMesReferencia;
	@FXML private TextField inputAnoReferencia;
	
	@FXML private Button btnIncrease;
	@FXML private Button btnDecrease;
	
	@FXML private Label lblAno;
	
	private int selectedAnoReferencia = LocalDate.now().getYear();
	private ObservableList<String> meses = Util.generateListMonths();
	
	@Autowired private PrecoLeiteService service;
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			mesReferenciaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMesReferencia()));
			anoReferenciaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAnoReferencia())));
			valorMaximoPraticadoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValorMaximoPraticado())));
			valorRecebidoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValorRecebido())));
			
			super.service = this.service;
			configuraMesesAnoReferencia();
			super.initialize();
			
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			
			inputMesReferencia.textProperty().bindBidirectional(((PrecoLeite)object).mesReferenciaProperty());
			inputAnoReferencia.textProperty().bindBidirectional(((PrecoLeite)object).anoReferenciaProperty());
			inputValorMaximoPraticado.textProperty().bindBidirectional(((PrecoLeite)object).valorMaximoPraticadoProperty());
			inputValorRecebido.textProperty().bindBidirectional(((PrecoLeite)object).valorRecebidoProperty());
			
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
				precoLeite = new PrecoLeite(meses.get(i), i+1, selectedAnoReferencia, BigDecimal.ZERO, BigDecimal.ZERO);
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
