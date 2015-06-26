package br.com.milksys.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.NumberTextField;
import br.com.milksys.components.PropertyDecimalValueFactory;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.model.EntregaLeite;
import br.com.milksys.model.PrecoLeite;
import br.com.milksys.model.ProducaoLeite;
import br.com.milksys.model.State;
import br.com.milksys.service.EntregaLeiteService;
import br.com.milksys.service.PrecoLeiteService;
import br.com.milksys.service.ProducaoLeiteService;
import br.com.milksys.util.NumberFormatUtil;
import br.com.milksys.util.Util;

@Controller
public class EntregaLeiteController extends AbstractController<Integer, EntregaLeite> {


	@FXML private TableColumn<EntregaLeite, String> mesReferenciaColumn;
	@FXML private TableColumn<EntregaLeite, LocalDate> dataInicioColumn;
	@FXML private TableColumn<EntregaLeite, LocalDate> dataFimColumn;
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
			mesReferenciaColumn.setCellValueFactory(new PropertyValueFactory<EntregaLeite, String>("mesReferencia"));
			dataInicioColumn.setCellFactory(new TableCellDateFactory<EntregaLeite, LocalDate>("dataInicio"));
			dataFimColumn.setCellFactory(new TableCellDateFactory<EntregaLeite, LocalDate>("dataFim"));
			volumeColumn.setCellValueFactory(new PropertyDecimalValueFactory<EntregaLeite, String>("volume"));
			valorRecebidoColumn.setCellValueFactory(new PropertyDecimalValueFactory<EntregaLeite, String>("valorRecebido"));
			valorTotalColumn.setCellValueFactory(new PropertyDecimalValueFactory<EntregaLeite, String>("valorTotal"));
			observacaoColumn.setCellValueFactory(new PropertyValueFactory<EntregaLeite, String>("observacao"));
		
			valorMaximoPraticadoColumn.setCellValueFactory(new PropertyDecimalValueFactory<EntregaLeite,String>("valorMaximoPraticado"));
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
			
			inputMesReferencia.textProperty().bindBidirectional(getObject().mesReferenciaProperty());
			inputAnoReferencia.textProperty().bindBidirectional(getObject().anoReferenciaProperty());
			inputDataInicio.valueProperty().bindBidirectional(getObject().dataInicioProperty());
			inputDataFim.valueProperty().bindBidirectional(getObject().dataFimProperty());
			inputVolume.textProperty().bindBidirectional(getObject().volumeProperty());
			inputValorMaximoPraticado.setText(NumberFormatUtil.decimalFormat(getObject().getValorMaximoPraticado()));
			inputValorRecebido.setText(NumberFormatUtil.decimalFormat(getObject().getValorRecebido()));
			inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
			
		}
		
	}
	
	/**
	 * Ao alterar o ano de refer�ncia carrega o respectivo calend�rio de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleIncreaseAnoReferencia() {
		selectedAnoReferencia++;
		configuraMesesEntregaAnoReferencia();
	}
	
	/**
	 * Ao alterar o ano de refer�ncia carrega o respectivo calend�rio de entrega.
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
		
		BigDecimal totalEntregue = loadTotalEntreguePeriodo(getObject().getDataInicio(), getObject().getDataFim());
		
		getObject().setVolume(totalEntregue);
		
		super.handleOk();
		this.resume();
		
	}
	
	/**
	 * Carrega o total entregue no per�odo selecionado.
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
	 * Sempre que acessa o caso de uso � necess�rio atualizar o volume para recarregar a produ��o do per�odo
	 * pois podem ter havido atualiza��es na tabela de produ��o.
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
	 * Quando n�o houver pre�o de leite informado para o m�s selecionado, habilita o cadastro
	 */
	@FXML
	private void handleCadastrarPrecoLeite(){
		
		precoLeiteController.state = State.INSERT_TO_SELECT;
		//verifica se existe pre�o cadastrado
		PrecoLeite precoLeite = precoLeiteService.findByMesAno(getObject().getMesReferencia(), getObject().getAnoReferencia());
		
		if ( precoLeite == null ){
			precoLeite = new PrecoLeite();
			precoLeite.setMesReferencia(getObject().getMesReferencia());
			precoLeite.setAnoReferencia(getObject().getAnoReferencia());
			precoLeite.setCodigoMes(meses.indexOf(getObject().getMesReferencia())+1);
		}
		
		precoLeiteController.setObject(precoLeite);
		precoLeiteController.showForm(0,0);
		
		if ( precoLeiteController.getObject() != null ){
			getObject().setPrecoLeite(precoLeiteController.getObject());
			table.getItems().set(table.getItems().indexOf(getObject()), getObject());
			service.save(getObject());
			resume();
		}
		
	}
	
	@Override
	protected EntregaLeite getObject() {
		return (EntregaLeite)super.object;
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
