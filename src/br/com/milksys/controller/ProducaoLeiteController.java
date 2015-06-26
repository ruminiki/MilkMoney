package br.com.milksys.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Calendar;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.NumberTextField;
import br.com.milksys.components.PropertyDecimalValueFactory;
import br.com.milksys.components.TableCellDateFactory;
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

	@FXML private TableColumn<ProducaoLeite, LocalDate> dataColumn;
	@FXML private TableColumn<ProducaoLeite, String> numeroVacasOrdenhadasColumn;
	@FXML private TableColumn<ProducaoLeite, String> volumeProduzidoColumn;
	@FXML private TableColumn<ProducaoLeite, String> volumeEntregueColumn;
	@FXML private TableColumn<ProducaoLeite, String> mediaProducaoColumn;
	@FXML private TableColumn<ProducaoLeite, String> valorColumn;
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
	
	@Autowired private ProducaoLeiteService service;
	@Autowired private PrecoLeiteService precoLeiteService;
	@Autowired private PrecoLeiteController precoLeiteController;
	@Autowired private ProducaoIndividualController producaoIndividualController;
	
	private int selectedAnoReferencia = LocalDate.now().getYear();
	private int selectedMesReferencia = LocalDate.now().getMonthValue();
	
	private ObservableList<String> meses = Util.generateListMonths();
	
	private PrecoLeite precoLeite;
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			dataColumn.setCellFactory(new TableCellDateFactory<ProducaoLeite, LocalDate>("data"));
			volumeProduzidoColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoLeite, String>("volumeProduzido"));
			volumeEntregueColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoLeite, String>("volumeEntregue"));
			mediaProducaoColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoLeite, String>("mediaProducao"));
			valorColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoLeite, String>("valor"));
			observacaoColumn.setCellValueFactory(new PropertyValueFactory<ProducaoLeite, String>("observacao"));
			
			numeroVacasOrdenhadasColumn.setCellValueFactory(new PropertyValueFactory<ProducaoLeite, String>("numeroVacasOrdenhadas"));
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
			configuraTabelaDiasMesSelecionado();
			super.initialize();
			this.resume();

		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) ){
			
			inputNumeroVacasOrdenhadas.textProperty().bindBidirectional(getObject().numeroVacasOrdenhadasProperty());
			inputVolumeProduzido.textProperty().bindBidirectional(getObject().volumeProduzidoProperty());
			inputVolumeEntregue.textProperty().bindBidirectional(getObject().volumeEntregueProperty());
			inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
			inputData.setText(DateUtil.format(getObject().dataProperty().get()));
			
		}
		
	}
	
	/**
	 * Ao alterar o ano de refer�ncia carrega o respectivo calend�rio de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleIncreaseAnoReferencia() {
		selectedAnoReferencia++;
		configuraTabelaDiasMesSelecionado();
	}
	
	/**
	 * Ao alterar o ano de refer�ncia carrega o respectivo calend�rio de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleDecreaseAnoReferencia() {
		selectedAnoReferencia--;
		configuraTabelaDiasMesSelecionado();
	}
	
	/**
	 * Ao alterar o m�s de refer�ncia carrega o respectivo calend�rio de entrega.
	 * @param newValue
	 */
	private void changeMesReferenciaListener(String newValue) {
		selectedMesReferencia = meses.indexOf(newValue) + 1;
		configuraTabelaDiasMesSelecionado();
	}    
	
	@Override
	protected void initializeTableOverview() {
		super.data.clear();
		super.data.addAll(service.findAllByPeriodoAsObservableList(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes())));
	}
	
	@Override
	protected void handleOk() {
		int vacasOrdenhadas = getObject().getNumeroVacasOrdenhadas();
		BigDecimal volumeProduzido = getObject().getVolumeProduzido();
		
		if ( vacasOrdenhadas > 0 && volumeProduzido.compareTo(BigDecimal.ZERO) > 0 ){
			getObject().setMediaProducao(volumeProduzido.divide(new BigDecimal(vacasOrdenhadas), 2, RoundingMode.HALF_UP));	
		}
		
		super.handleOk();
		this.resume();
	}
	
	
	private void configuraTabelaDiasMesSelecionado(){
		
		Calendar cDataInicio = Calendar.getInstance();
		cDataInicio.setTimeInMillis(DateUtil.asDate(dataInicioMes()).getTime());
		
		Calendar cDataFim = Calendar.getInstance();
		cDataFim.setTimeInMillis(DateUtil.asDate(dataFimMes()).getTime());
		
		//localiza o pre�o do leite para o m�s
		registraPrecoProducaoLeite();
		
		while ( cDataInicio.before(cDataFim) || cDataInicio.equals(cDataFim) ){
			ProducaoLeite producaoLeite = new ProducaoLeite(DateUtil.asLocalDate(cDataInicio.getTime()), 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, precoLeite);
			service.save(producaoLeite);
			cDataInicio.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		lblAno.setText(String.valueOf(selectedAnoReferencia));
		resume();
		
	}
	
	/**
	 * Atualiza o pre�o do leite na produ��o de cada dia do m�s
	 */
	private void registraPrecoProducaoLeite(){
		this.precoLeite = precoLeiteService.findByMesAno(meses.get(selectedMesReferencia-1), selectedAnoReferencia);
		service.updatePrecoLeitePeriodo(precoLeite, DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes()));
	}
	
	/**
	 * Retorna a data do primeiro dia do m�s selecionado
	 * @return
	 */
	private LocalDate dataInicioMes(){
		return LocalDate.of(selectedAnoReferencia, selectedMesReferencia, 01);
	}
	
	/**
	 * Retorna a data do �ltimo dia do m�s selecionado
	 * @return
	 */
	private LocalDate dataFimMes(){
		return LocalDate.of(selectedAnoReferencia, selectedMesReferencia, dataInicioMes().lengthOfMonth());
	}
	
	/**
	 * Faz a somat�ria da produ��o
	 * M�dia
	 * Valor Estimado
	 * Total Entregue
	 */
	protected void resume(){
		
		if ( data != null && data.size() > 0 ){
			
			int dias = 0;
			BigDecimal totalEntregue = new BigDecimal(0);
			BigDecimal totalProduzido = new BigDecimal(0);
			BigDecimal valor = new BigDecimal(0);
			int totalVacasOrdenhadas = 0;
			
			for (int i = 0; i < data.size(); i++){
				
				ProducaoLeite e = data.get(i);
				
				if ( e.getVolumeProduzido().compareTo(BigDecimal.ZERO) > 0 ){
					dias++;
					totalProduzido = totalProduzido.add(e.getVolumeProduzido());
					totalVacasOrdenhadas += e.getNumeroVacasOrdenhadas();
				}
				
				totalEntregue = totalEntregue.add(e.getVolumeEntregue());
				valor = valor.add(e.getValor());
				
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
				lblValorEstimado.setText(NumberFormatUtil.decimalFormat(valor));
			}else{
				lblValorEstimado.setText("Cadastrar Pre�o");
			}
			
		}
	}
	
	/**
	 * Quando n�o houver pre�o de leite informado para o m�s
	 * habilita o cadastro pela tela de produ��o.
	 */
	@FXML
	private void handleCadastrarPrecoLeite(){
		
		precoLeiteController.state = State.INSERT_TO_SELECT;
		
		if ( this.precoLeite == null ){ 
			precoLeite = new PrecoLeite();
			precoLeite.setMesReferencia(meses.get(selectedMesReferencia-1));
			precoLeite.setAnoReferencia(selectedAnoReferencia);
			precoLeite.setCodigoMes(selectedMesReferencia);
		}
		
		precoLeiteController.setObject(precoLeite);
		precoLeiteController.showForm(0,0);
		if ( precoLeiteController.getObject() != null ){
			registraPrecoProducaoLeite();
			resume();
		}
		
	}
	
	/**
	 * Abre formul�rio para cadastrar a produ��o individual de um animal
	 */
	protected void handleCadastrarProducaoIndividual() {
		producaoIndividualController.state = State.INSERT_TO_SELECT;
		
		ProducaoIndividual pi = new ProducaoIndividual();
		pi.setData(getObject().getData());
		producaoIndividualController.setPrecoLeite(this.precoLeite);
		producaoIndividualController.setObject(pi);
		
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
		return "Produ��o Leite";
	}
	
	@Override
	protected ProducaoLeite getObject() {
		return (ProducaoLeite) super.object;
	}
	
}
