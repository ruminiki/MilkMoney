package br.com.milksys.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.util.Callback;

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
	
	@FXML private TextField inputMesReferencia;
	@FXML private TextField inputAnoReferencia;
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
	@Autowired private PrecoLeiteController precoLeiteController;
	
	private int selectedAnoReferencia = LocalDate.now().getYear();
	private ObservableList<String> meses = Util.generateListMonths();
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			//DateUtil.format(
			mesReferenciaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMesReferencia()));
			dataInicioColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataInicio() != null ? DateUtil.format(cellData.getValue().getDataInicio()) : "--"));
			dataFimColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataFim() != null ? DateUtil.format(cellData.getValue().getDataFim()) : "--"));
			volumeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getVolume())));
			valorRecebidoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getPrecoLeite().getValorRecebido())));
			valorTotalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValorTotal())));
			observacaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObservacao()));
		
			valorMaximoPraticadoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getPrecoLeite().getValorMaximoPraticado())));
			valorMaximoPraticadoColumn.setCellFactory(new Callback<TableColumn<EntregaLeite,String>, TableCell<EntregaLeite,String>>(){
				@Override
				public TableCell<EntregaLeite, String> call(TableColumn<EntregaLeite, String> param) {
					TableCell<EntregaLeite, String> cell = new TableCell<EntregaLeite, String>(){
						@Override
						public void updateItem(String item, boolean empty) {
							if(item!=null){
								Hyperlink link = new Hyperlink();
								link.setText(item);
								link.setFocusTraversable(false);
								link.setOnAction(new EventHandler<ActionEvent>() {
								    @Override
								    public void handle(ActionEvent e) {
								    	object = data.get(getTableRow().getIndex());
								    	handleCadastrarPrecoLeite();
								    }
								});
								setGraphic(link);
							} 
						}
					};                           
					return cell;
				}
			});
			
			super.service = this.service;
			configuraMesesEntregaAnoReferencia();
			super.initialize();
			
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			
			inputMesReferencia.textProperty().bindBidirectional(((EntregaLeite)object).mesReferenciaProperty());
			inputAnoReferencia.textProperty().bindBidirectional(((EntregaLeite)object).anoReferenciaProperty());
			
			inputDataInicio.valueProperty().bindBidirectional(((EntregaLeite)object).dataInicioProperty());
			inputDataFim.valueProperty().bindBidirectional(((EntregaLeite)object).dataFimProperty());
			
			inputVolume.textProperty().bindBidirectional(((EntregaLeite)object).volumeProperty());
			
			inputValorMaximoPraticado.setText(NumberFormatUtil.decimalFormat(((EntregaLeite)object).getPrecoLeite().getValorMaximoPraticado()));
			inputValorRecebido.setText(NumberFormatUtil.decimalFormat(((EntregaLeite)object).getPrecoLeite().getValorRecebido()));
			
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
			
			PrecoLeite precoLeite = precoLeiteService.findByMesAno(meses.get(i), selectedAnoReferencia);
			EntregaLeite entregaLeite = service.findByMesAno(meses.get(i), selectedAnoReferencia);
			
			if ( entregaLeite == null ){
				entregaLeite = new EntregaLeite(meses.get(i), selectedAnoReferencia, BigDecimal.ZERO, precoLeite);
			}else{
				BigDecimal totalEntregue = loadTotalEntreguePeriodo(entregaLeite.getDataInicio(), entregaLeite.getDataFim());
				entregaLeite.setVolume(totalEntregue);
				//verifica se já existe preço associado ao mês de entrega, se não tiver atualiza o registro
				if ( entregaLeite.getPrecoLeite() == null ){
					entregaLeite.setPrecoLeite(precoLeite);
				}
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
	
	/**
	 * Quando não houver preço de leite informado para o mês selecionado, habilita o cadastro
	 */
	@FXML
	private void handleCadastrarPrecoLeite(){
		
		precoLeiteController.state = State.INSERT_TO_SELECT;
		//verifica se existe preço cadastrado
		PrecoLeite precoLeite = precoLeiteService.findByMesAno(((EntregaLeite)object).getMesReferencia(), ((EntregaLeite)object).getAnoReferencia());
		
		if ( precoLeite == null ){
			precoLeite = new PrecoLeite();
			precoLeite.setMesReferencia(((EntregaLeite)object).getMesReferencia());
			precoLeite.setAnoReferencia(((EntregaLeite)object).getAnoReferencia());
			precoLeite.setCodigoMes(meses.indexOf(((EntregaLeite)object).getMesReferencia())+1);
		}
		
		precoLeiteController.setObject(precoLeite);
		precoLeiteController.showForm(0,0);
		
		if ( precoLeiteController.getObject() != null ){
			((EntregaLeite)object).setPrecoLeite((PrecoLeite)precoLeiteController.getObject());
			table.getItems().set(table.getItems().indexOf(((EntregaLeite)object)), ((EntregaLeite)object));
			service.save((EntregaLeite)object);
			resume();
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
