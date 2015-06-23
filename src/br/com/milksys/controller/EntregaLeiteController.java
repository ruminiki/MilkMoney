package br.com.milksys.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.NumberTextField;
import br.com.milksys.model.EntregaLeite;
import br.com.milksys.model.PrecoLeite;
import br.com.milksys.model.ProducaoLeite;
import br.com.milksys.model.State;
import br.com.milksys.service.EntregaLeiteService;
import br.com.milksys.service.PrecoLeiteService;
import br.com.milksys.service.ProducaoLeiteService;
import br.com.milksys.util.DateUtil;
import br.com.milksys.util.NumberFormatUtil;
import br.com.milksys.util.Util;

@Controller
public class EntregaLeiteController extends AbstractController<Integer, EntregaLeite> {


	@FXML private TableColumn<EntregaLeite, String> mesReferenciaColumn;
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
	@FXML private Label lblTotalEntregue;
	@FXML private Label lblTotalRecebido;
	
	@Autowired private EntregaLeiteService service;
	@Autowired private ProducaoLeiteService producaoLeiteService;
	@Autowired private PrecoLeiteService precoLeiteService;
	
	private int selectedAnoReferencia = LocalDate.now().getYear();
	private ObservableList<String> meses = Util.generateListMonths();
	private ObservableList<Number> anos = Util.generateListNumbers(1980, LocalDate.now().getYear());
	
	private PrecoLeite precoLeite;
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			//DateUtil.format(
			mesReferenciaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMesReferencia()));
			dataInicioColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataInicio() != null ? DateUtil.format(cellData.getValue().getDataInicio()) : "--"));
			dataFimColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataFim() != null ? DateUtil.format(cellData.getValue().getDataFim()) : "--"));
			volumeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getVolume())));
			valorMaximoPraticadoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValorMaximoPraticado())));
			valorRecebidoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValorRecebido())));
			valorTotalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValorTotal())));
			observacaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObservacao()));
			
			super.service = this.service;
			configuraMesesEntregaAnoReferencia();
			super.initialize();
			
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
			
			inputDataInicio.valueProperty().bindBidirectional(((EntregaLeite)object).dataInicioProperty());
			inputDataFim.valueProperty().bindBidirectional(((EntregaLeite)object).dataFimProperty());
			
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
	
	@Override
	protected void handleOk() {
		
		BigDecimal totalEntregue = loadTotalEntreguePeriodo(((EntregaLeite)object).getDataInicio(), ((EntregaLeite)object).getDataFim());
		
		((EntregaLeite)object).setVolume(totalEntregue);
		((EntregaLeite)object).setValorTotal(totalEntregue.multiply(((EntregaLeite)object).getValorRecebido()));
		
		this.precoLeite = precoLeiteService.findByMesAno(((EntregaLeite)object).getMesReferencia(), ((EntregaLeite)object).getAnoReferencia());
	
		if ( precoLeite != null )
			((EntregaLeite)object).setValorMaximoPraticado(precoLeite.getValor());
		
		super.handleOk();
		this.resume();
		
	}
	
	/**
	 * Carrega o total entregue no período selecionado.
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	private BigDecimal loadTotalEntreguePeriodo(Date dataInicio, Date dataFim){
		BigDecimal totalEntregue = BigDecimal.ZERO;
		List<ProducaoLeite> producaoLeite = producaoLeiteService.findAllByPeriodoAsObservableList(dataInicio, dataFim);
		
		for( ProducaoLeite p : producaoLeite ){
			totalEntregue = totalEntregue.add(p.getVolumeEntregue());
		}
		return totalEntregue;
	}
	
	/**
	 * Configura os meses para registro das entregas efetuadas.
	 * Sempre que acessa o caso de uso é necessário atualizar o volume para recarregar a produção do período
	 * pois podem ter havido atualizações na tabela de produção.
	 * 
	 */
	private void configuraMesesEntregaAnoReferencia(){
		
		for (int i = 0; i < meses.size(); i++) {
			
			EntregaLeite entregaLeite = service.findByMesAno(meses.get(i), selectedAnoReferencia);
			if ( entregaLeite == null ){
				entregaLeite = new EntregaLeite(meses.get(i), selectedAnoReferencia, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
			}else{
				BigDecimal totalEntregue = loadTotalEntreguePeriodo(entregaLeite.getDataInicio(), entregaLeite.getDataFim());
				
				entregaLeite.setVolume(totalEntregue);
				entregaLeite.setValorTotal(totalEntregue.multiply(entregaLeite.getValorRecebido()));
				
				this.precoLeite = precoLeiteService.findByMesAno(entregaLeite.getMesReferencia(), entregaLeite.getAnoReferencia());
			
				if ( precoLeite != null )
					entregaLeite.setValorMaximoPraticado(precoLeite.getValor());
			}
			service.save(entregaLeite);
		}

		this.initializeTableOverview();
		table.setItems(data);
		lblAno.setText(String.valueOf(selectedAnoReferencia));
		this.resume();
		
	}
	
	/**
	 * Atualiza os totais entregues e valor recebido
	 */
	private void resume(){
		if ( data != null && data.size() > 0 ){
			BigDecimal totalEntregue = new BigDecimal(0);
			BigDecimal valorRecebido = new BigDecimal(0);
			
			for (int i = 0; i < data.size(); i++){
				
				EntregaLeite e = data.get(i);
				
				totalEntregue = totalEntregue.add(e.getVolume());
				valorRecebido = valorRecebido.add(e.getValorTotal());
				
			}
			
			lblTotalEntregue.setText(NumberFormatUtil.decimalFormat(totalEntregue));
			lblTotalRecebido.setText(NumberFormatUtil.decimalFormat(valorRecebido));
		}
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
