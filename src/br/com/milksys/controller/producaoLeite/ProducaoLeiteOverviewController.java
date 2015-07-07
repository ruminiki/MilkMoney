package br.com.milksys.controller.producaoLeite;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.components.PropertyDecimalValueFactory;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.controller.precoLeite.PrecoLeiteFormController;
import br.com.milksys.controller.producaoIndividual.ProducaoIndividualExternalFormController;
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
public class ProducaoLeiteOverviewController extends AbstractOverviewController<Integer, ProducaoLeite> {

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
	@Autowired private PrecoLeiteFormController precoLeiteFormController;
	@Autowired private ProducaoIndividualExternalFormController producaoIndividualExternalFormController;
	
	private int selectedAnoReferencia = LocalDate.now().getYear();
	private int selectedMesReferencia = LocalDate.now().getMonthValue();
	
	private ObservableList<String> meses = Util.generateListMonths();
	
	@FXML
	public void initialize() {
		
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
		service.configuraTabelaDiasMesSelecionado(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes()));
		super.initialize((ProducaoLeiteFormController)MainApp.getBean(ProducaoLeiteFormController.class));
		this.resume();
		
	}
	
	/**
	 * Ao alterar o ano de refer�ncia carrega o respectivo calend�rio de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleIncreaseAnoReferencia() {
		selectedAnoReferencia++;
		service.configuraTabelaDiasMesSelecionado(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes()));
		refreshTableOverview();
		this.resume();
	}
	
	/**
	 * Ao alterar o ano de refer�ncia carrega o respectivo calend�rio de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleDecreaseAnoReferencia() {
		selectedAnoReferencia--;
		service.configuraTabelaDiasMesSelecionado(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes()));
		refreshTableOverview();
		this.resume();
	}
	
	/**
	 * Ao alterar o m�s de refer�ncia carrega o respectivo calend�rio de entrega.
	 * @param newValue
	 */
	private void changeMesReferenciaListener(String newValue) {
		selectedMesReferencia = meses.indexOf(newValue) + 1;
		service.configuraTabelaDiasMesSelecionado(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes()));
		refreshTableOverview();
		this.resume();
	}    
	
	@Override
	protected void refreshTableOverview() {
		super.data.clear();
		super.data.addAll(service.findAllByPeriodoAsObservableList(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes())));
		service.recarregaPrecoLeite(data, meses.get(selectedMesReferencia-1), selectedAnoReferencia);
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
			if ( totalProduzido.compareTo(BigDecimal.ZERO) > 0 && dias > 0 ){
				lblMediaMes.setText(NumberFormatUtil.decimalFormat(totalProduzido.divide(new BigDecimal(dias), 2, RoundingMode.HALF_UP)));
				if ( totalVacasOrdenhadas > 0 )  
					lblMediaProdutividadeMes.setText(NumberFormatUtil.decimalFormat(totalProduzido.divide(new BigDecimal(totalVacasOrdenhadas), 2, RoundingMode.HALF_UP)));
				else
					lblMediaProdutividadeMes.setText(NumberFormatUtil.decimalFormat(BigDecimal.ZERO));
			}else{
				lblMediaMes.setText(NumberFormatUtil.decimalFormat(BigDecimal.ZERO));
				lblMediaProdutividadeMes.setText(NumberFormatUtil.decimalFormat(BigDecimal.ZERO));
			}
			
			lblValorEstimado.setText(NumberFormatUtil.decimalFormat(valor));
			if ( !precoLeiteService.isPrecoCadastrado(meses.get(selectedMesReferencia-1), selectedAnoReferencia) ){
				lblValorEstimado.setText("Cadastrar Pre�o");
			}
			lblAno.setText(String.valueOf(selectedAnoReferencia));
		}
	}
	
	/**
	 * Quando n�o houver pre�o de leite informado para o m�s
	 * habilita o cadastro pela tela de produ��o.
	 */
	@FXML
	private void handleCadastrarPrecoLeite(){
		
		precoLeiteFormController.setState(State.INSERT_TO_SELECT);
		
		PrecoLeite precoLeite = precoLeiteService.findByMesAno(meses.get(selectedMesReferencia-1), selectedAnoReferencia);
		
		if ( precoLeite == null ){ 
			precoLeite = new PrecoLeite();
			precoLeite.setMesReferencia(meses.get(selectedMesReferencia-1));
			precoLeite.setAnoReferencia(selectedAnoReferencia);
			precoLeite.setCodigoMes(selectedMesReferencia);
		}
		
		precoLeiteFormController.setObject(precoLeite);
		precoLeiteFormController.showForm();
		if ( precoLeiteFormController.getObject() != null && precoLeiteFormController.getObject().getId() > 0 ){
			service.recarregaPrecoLeite(data, meses.get(selectedMesReferencia-1), selectedAnoReferencia);
			this.resume();
		}
		
	}
	
	/**
	 * Abre formul�rio para cadastrar a produ��o individual de um animal
	 */
	protected void handleCadastrarProducaoIndividual() {
		producaoIndividualExternalFormController.setObject(new ProducaoIndividual(getObject().getData()));
		producaoIndividualExternalFormController.setSelectedDate(getObject().getData());
		producaoIndividualExternalFormController.showForm();
		
	}

	@Override
	protected String getFormName() {
		return "view/producaoLeite/ProducaoLeiteForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Produ��o Leite";
	}
	
}