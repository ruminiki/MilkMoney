package br.com.milksys.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Calendar;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.NumberTextField;
import br.com.milksys.model.PrecoLeite;
import br.com.milksys.model.ProducaoIndividual;
import br.com.milksys.model.ProducaoLeite;
import br.com.milksys.model.State;
import br.com.milksys.service.PrecoLeiteService;
import br.com.milksys.service.ProducaoLeiteService;
import br.com.milksys.util.DateUtil;
import br.com.milksys.util.NumberFormatUtil;
import br.com.milksys.util.Util;

@Controller
public class ProducaoLeiteController extends AbstractController<Integer, ProducaoLeite> {

	@FXML private TextField inputData;
	@FXML private NumberTextField inputNumeroVacasOrdenhadas;
	@FXML private NumberTextField inputVolumeProduzido;
	@FXML private NumberTextField inputVolumeEntregue;
	@FXML private TextField inputObservacao;

	@FXML private TableColumn<ProducaoLeite, String> dataColumn;
	@FXML private TableColumn<ProducaoLeite, String> numeroVacasOrdenhadasColumn;
	@FXML private TableColumn<ProducaoLeite, String> volumeProduzidoColumn;
	@FXML private TableColumn<ProducaoLeite, String> volumeEntregueColumn;
	@FXML private TableColumn<ProducaoLeite, String> mediaProducaoColumn;
	@FXML private TableColumn<ProducaoLeite, String> valorEstimadoColumn;
	@FXML private TableColumn<ProducaoLeite, String> observacaoColumn;
	
	@FXML private ComboBox<String> inputMesReferencia;
	@FXML private Button btnIncrease;
	@FXML private Button btnDecrease;
	
	@FXML private Label lblTotalEntregue;
	@FXML private Label lblTotalProduzido;
	@FXML private Label lblMediaMes;
	@FXML private Label lblMediaProdutividadeMes;
	@FXML private Label lblTotalVacasOrdenhadas;
	@FXML private Label lblAno;
	@FXML private Hyperlink lblValorEstimado;
	
	@Autowired
	private ProducaoLeiteService service;
	@Autowired
	private PrecoLeiteService precoLeiteService;
	@Autowired
	private PrecoLeiteController precoLeiteController;
	@Autowired
	private ProducaoIndividualController producaoIndividualController;
	
	private int selectedAnoReferencia = LocalDate.now().getYear();
	private int selectedMesReferencia = LocalDate.now().getMonthValue();
	
	private ObservableList<String> meses = Util.generateListMonths();
	
	private PrecoLeite precoLeite;
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			dataColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getData())));
			volumeProduzidoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getVolumeProduzido())));
			volumeEntregueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getVolumeEntregue())));
			mediaProducaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getMediaProducao())));
			valorEstimadoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValorEstimado())));
			observacaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObservacao()));
			
			numeroVacasOrdenhadasColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroVacasOrdenhadas())));
			numeroVacasOrdenhadasColumn.setCellFactory(new Callback<TableColumn<ProducaoLeite,String>, TableCell<ProducaoLeite,String>>(){
				@Override
				public TableCell<ProducaoLeite, String> call(TableColumn<ProducaoLeite, String> param) {
					TableCell<ProducaoLeite, String> cell = new TableCell<ProducaoLeite, String>(){
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
								    	handleCadastrarProducaoIndividual();
								    }
								});
								setGraphic(link);
							} 
						}
					};                           
					return cell;
				}
			});

			inputMesReferencia.setItems(meses);
			inputMesReferencia.getSelectionModel().select(selectedMesReferencia-1);
			inputMesReferencia.valueProperty().addListener((observable, oldValue, newValue) -> changeMesReferenciaListener(newValue));
			
			super.service = this.service;
			this.precoLeite = precoLeiteService.findByMesAno(meses.get(selectedMesReferencia-1), selectedAnoReferencia);
			configuraTabelaDiasMesSelecionado();
			super.initialize();
			
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) ){
			
			inputNumeroVacasOrdenhadas.textProperty().bindBidirectional(((ProducaoLeite)object).numeroVacasOrdenhadasProperty());
			inputVolumeProduzido.textProperty().bindBidirectional(((ProducaoLeite)object).volumeProduzidoProperty());
			inputVolumeEntregue.textProperty().bindBidirectional(((ProducaoLeite)object).volumeEntregueProperty());
			inputObservacao.textProperty().bindBidirectional(((ProducaoLeite)object).observacaoProperty());
			inputData.setText(((ProducaoLeite)object).dataProperty().get());
			
		}
		
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleIncreaseAnoReferencia() {
		selectedAnoReferencia++;
		configuraTabelaDiasMesSelecionado();
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleDecreaseAnoReferencia() {
		selectedAnoReferencia--;
		configuraTabelaDiasMesSelecionado();
	}
	
	/**
	 * Ao alterar o mês de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	private void changeMesReferenciaListener(String newValue) {
		selectedMesReferencia = meses.indexOf(newValue) + 1;
		this.precoLeite = precoLeiteService.findByMesAno(meses.get(selectedMesReferencia-1), selectedAnoReferencia);
		configuraTabelaDiasMesSelecionado();
		atualizaValorEstimado();
	}    
	
	@Override
	protected void initializeTableOverview() {
		super.data.clear();
		super.data.addAll(service.findAllByPeriodoAsObservableList(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes())));
	}
	
	@Override
	protected void handleOk() {
		int vacasOrdenhadas = ((ProducaoLeite)object).getNumeroVacasOrdenhadas();
		BigDecimal volumeProduzido = ((ProducaoLeite)object).getVolumeProduzido();
		
		if ( vacasOrdenhadas > 0 && volumeProduzido.compareTo(BigDecimal.ZERO) > 0 ){
			((ProducaoLeite)object).setMediaProducao(volumeProduzido.divide(new BigDecimal(vacasOrdenhadas), 2, RoundingMode.HALF_UP));	
			if(precoLeite != null){
				((ProducaoLeite)object).setValorEstimado(precoLeite.getValor().multiply(((ProducaoLeite)object).getVolumeEntregue()));
			}
		}
		
		super.handleOk();
		this.resume();
	}
	
	
	private void configuraTabelaDiasMesSelecionado(){
		
		Calendar cDataInicio = Calendar.getInstance();
		cDataInicio.setTimeInMillis(DateUtil.asDate(dataInicioMes()).getTime());
		
		Calendar cDataFim = Calendar.getInstance();
		cDataFim.setTimeInMillis(DateUtil.asDate(dataFimMes()).getTime());
		
		while ( cDataInicio.before(cDataFim) || cDataInicio.equals(cDataFim) ){
			ProducaoLeite producaoLeite = new ProducaoLeite(cDataInicio.getTime(), 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
			service.save(producaoLeite);
			cDataInicio.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		atualizaValorEstimado();
		lblAno.setText(String.valueOf(selectedAnoReferencia));
		resume();
		
	}
	
	/**
	 * Retorna a data do primeiro dia do mês selecionado
	 * @return
	 */
	private LocalDate dataInicioMes(){
		return LocalDate.of(selectedAnoReferencia, selectedMesReferencia, 01);
	}
	
	/**
	 * Retorna a data do último dia do mês selecionado
	 * @return
	 */
	private LocalDate dataFimMes(){
		return LocalDate.of(selectedAnoReferencia, selectedMesReferencia, dataInicioMes().lengthOfMonth());
	}
	
	/**
	 * Faz a somatória da produção
	 * Média
	 * Valor Estimado
	 * Total Entregue
	 */
	protected void resume(){
		
		if ( data != null && data.size() > 0 ){
			
			int dias = 0;
			BigDecimal totalEntregue = new BigDecimal(0);
			BigDecimal totalProduzido = new BigDecimal(0);
			int totalVacasOrdenhadas = 0;
			
			for (int i = 0; i < data.size(); i++){
				
				ProducaoLeite e = data.get(i);
				
				if ( e.getVolumeProduzido().compareTo(BigDecimal.ZERO) > 0 ){
					dias++;
					totalProduzido = totalProduzido.add(e.getVolumeProduzido());
					totalVacasOrdenhadas += e.getNumeroVacasOrdenhadas();
				}
				
				totalEntregue = totalEntregue.add(e.getVolumeEntregue());
				
			}
			
			lblTotalProduzido.setText(NumberFormatUtil.decimalFormat(totalProduzido));
			lblTotalEntregue.setText(NumberFormatUtil.decimalFormat(totalEntregue));
			lblTotalVacasOrdenhadas.setText(String.valueOf(totalVacasOrdenhadas));
			if ( totalEntregue.compareTo(BigDecimal.ZERO) > 0 && dias > 0 ){
				lblMediaMes.setText(NumberFormatUtil.decimalFormat(totalEntregue.divide(new BigDecimal(dias), 2, RoundingMode.HALF_UP)));
				lblMediaProdutividadeMes.setText(NumberFormatUtil.decimalFormat(totalEntregue.divide(new BigDecimal(totalVacasOrdenhadas), 2, RoundingMode.HALF_UP)));
			}else{
				lblMediaMes.setText(NumberFormatUtil.decimalFormat(BigDecimal.ZERO));
				lblMediaProdutividadeMes.setText(NumberFormatUtil.decimalFormat(BigDecimal.ZERO));
			}
			
			if ( precoLeite != null ){
				lblValorEstimado.setText(NumberFormatUtil.decimalFormat(totalEntregue.multiply(precoLeite.getValor())));	
			}else{
				lblValorEstimado.setText("Cadastrar Preço");
			}
			
		}
	}
	
	/**
	 * Quando o preço do leite é cadastrado ou o mês é alterado
	 * atualiza o valor estimado
	 */
	private void atualizaValorEstimado(){
		this.initializeTableOverview();
		
		if ( precoLeite != null ){
			for (int i = 0; i < data.size(); i++) {
				ProducaoLeite producaoLeite = data.get(i);
				producaoLeite.setValorEstimado(precoLeite.getValor().multiply(producaoLeite.getVolumeEntregue()));
			}
		}
		
		table.setItems(data);

	}
	
	/**
	 * Quando não houver preço de leite informado para o mês
	 * habilita o cadastro pela tela de produção.
	 */
	@FXML
	private void handleCadastrarPrecoLeite(){
		
		if ( this.precoLeite == null ){ 
			precoLeiteController.state = State.INSERT_TO_SELECT;
			
			PrecoLeite p = new PrecoLeite();
			p.setMesReferencia(meses.get(selectedMesReferencia-1));
			p.setAnoReferencia(selectedAnoReferencia);
			precoLeiteController.object = p;
			precoLeiteController.showForm(0,0);
			if ( precoLeiteController.getObject() != null ){
				this.precoLeite = (PrecoLeite) precoLeiteController.getObject();
				atualizaValorEstimado();
				resume();
			}
		}else{
			precoLeiteController.state = State.INSERT_TO_SELECT;
			precoLeiteController.object = precoLeite;
			precoLeiteController.showForm(0,0);
			if ( precoLeiteController.getObject() != null ){
				this.precoLeite = (PrecoLeite) precoLeiteController.getObject();
				atualizaValorEstimado();
				resume();
			}
		}
		
	}
	
	/**
	 * Abre formulário para cadastrar a produção individual de um animal
	 */
	protected void handleCadastrarProducaoIndividual() {
		producaoIndividualController.state = State.INSERT_TO_SELECT;
		
		ProducaoIndividual pi = new ProducaoIndividual();
		pi.setData(((ProducaoLeite)object).getData());
		producaoIndividualController.object = pi;
		
		producaoIndividualController.showForm(0,0);
		
	}

	protected boolean isInputValid() {
		return true;
	}

	@Override
	protected String getFormName() {
		return "view/producaoLeite/ProducaoLeiteForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Produção Leite";
	}
	
}
