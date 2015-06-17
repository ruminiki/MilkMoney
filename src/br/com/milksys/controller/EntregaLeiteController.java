package br.com.milksys.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Calendar;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.CustomCellFactory;
import br.com.milksys.components.CustomStringConverter;
import br.com.milksys.components.NumberTextField;
import br.com.milksys.model.CalendarioRecolha;
import br.com.milksys.model.EntregaLeite;
import br.com.milksys.model.Mes;
import br.com.milksys.model.State;
import br.com.milksys.service.CalendarioRecolhaService;
import br.com.milksys.service.EntregaLeiteService;
import br.com.milksys.util.DateUtil;
import br.com.milksys.util.NumberFormatUtil;

@Controller
public class EntregaLeiteController extends AbstractController<Integer, EntregaLeite> {

	@FXML private TextField inputData;
	@FXML private NumberTextField inputNumeroVacasOrdenhadas;
	@FXML private NumberTextField inputVolume;
	@FXML private TextField inputObservacao;

	@FXML private TableColumn<EntregaLeite, String> dataColumn;
	@FXML private TableColumn<EntregaLeite, String> numeroVacasOrdenhadasColumn;
	@FXML private TableColumn<EntregaLeite, String> volumeColumn;
	@FXML private TableColumn<EntregaLeite, String> observacaoColumn;
	@FXML private TableColumn<EntregaLeite, String> mediaProducaoColumn;
	
	@FXML private ComboBox<Mes> inputMesReferencia;
	@FXML private Button btnIncrease;
	@FXML private Button btnDecrease;
	
	@FXML private Label lblPeriodo;
	@FXML private Label lblTotalEntregas;
	@FXML private Label lblTotalEntregue;
	@FXML private Label lblMediaMes;
	@FXML private Label lblValorEstimado;
	@FXML private Label lblMediaProdutividadeMes;
	@FXML private Label lblTotalVacasOrdenhadas;
	@FXML private Label lblAno;
	
	@Resource(name="calendarioRecolhaService")
	private CalendarioRecolhaService calendarioRecolhaService;
	@Resource(name="entregaLeiteService")
	private EntregaLeiteService service;
	
	private LocalDate dataInicio, dataFim;
	private int selectedAnoReferencia = LocalDate.now().getYear();
	private int selectedMesReferencia = LocalDate.now().getMonthValue();
	
	private ObservableList<Mes> optionsMesReferencia = FXCollections.observableArrayList();
	
	{
		optionsMesReferencia.add(new Mes(1,  "JANEIRO"));
		optionsMesReferencia.add(new Mes(2,  "FEVEREIRO"));
		optionsMesReferencia.add(new Mes(3,  "MARÇO"));
		optionsMesReferencia.add(new Mes(4,  "ABRIL"));
		optionsMesReferencia.add(new Mes(5,  "MAIO"));
		optionsMesReferencia.add(new Mes(6,  "JUNHO"));
		optionsMesReferencia.add(new Mes(7,  "JULHO"));
		optionsMesReferencia.add(new Mes(8,  "AGOSTO"));
		optionsMesReferencia.add(new Mes(9,  "SETEMBRO"));
		optionsMesReferencia.add(new Mes(10, "OUTUBRO"));
		optionsMesReferencia.add(new Mes(11, "NOVEMBRO"));
		optionsMesReferencia.add(new Mes(12, "DEZEMBRO"));
	}

	
	@FXML
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			dataColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getData())));
			numeroVacasOrdenhadasColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroVacasOrdenhadas())));
			volumeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getVolume())));
			mediaProducaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getMediaProducao())));
			observacaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObservacao()));
			
			inputMesReferencia.setItems(optionsMesReferencia);
			inputMesReferencia.getSelectionModel().select(selectedMesReferencia-1);
			inputMesReferencia.valueProperty().addListener((observable, oldValue, newValue) -> changeMesReferenciaListener(newValue));
			inputMesReferencia.setCellFactory(new CustomCellFactory("getNome"));
			inputMesReferencia.setConverter(new CustomStringConverter("getNome"));
			
			if ( !isInitialized ){
				super.service = this.service;
				configureDataEntregaMesAnoReferencia(selectedMesReferencia, selectedAnoReferencia);
				super.initialize();
			}
			
		}
		
		if ( state.equals(State.INSERT_OR_UPDATE) ){
			
			inputNumeroVacasOrdenhadas.textProperty().bindBidirectional(((EntregaLeite)object).numeroVacasOrdenhadasProperty());
			inputVolume.textProperty().bindBidirectional(((EntregaLeite)object).volumeProperty());
			inputObservacao.textProperty().bindBidirectional(((EntregaLeite)object).observacaoProperty());
			inputData.setText(((EntregaLeite)object).dataProperty().get());
			
		}
		
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleIncreaseAnoReferencia() {
		selectedAnoReferencia++;
		configureDataEntregaMesAnoReferencia(selectedMesReferencia, selectedAnoReferencia);
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleDecreaseAnoReferencia() {
		selectedAnoReferencia--;
		configureDataEntregaMesAnoReferencia(selectedMesReferencia, selectedAnoReferencia);
	}
	
	/**
	 * Ao alterar o mês de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	private void changeMesReferenciaListener(Mes newValue) {
		selectedMesReferencia = newValue.getMesDoAno();
		configureDataEntregaMesAnoReferencia(selectedMesReferencia, selectedAnoReferencia);
	}    

	@Override
	protected void initializeTableOverview() {
		super.data.clear();
		super.data.addAll(service.findAllByPeriodoAsObservableList(DateUtil.asDate(dataInicio), DateUtil.asDate(dataFim)));
	}
	
	@Override
	protected void handleOk() {
		int vacasOrdenhadas = ((EntregaLeite)object).getNumeroVacasOrdenhadas();
		BigDecimal volume = ((EntregaLeite)object).getVolume();
		
		if ( vacasOrdenhadas > 0 && volume.compareTo(BigDecimal.ZERO) > 0 ){
			((EntregaLeite)object).setMediaProducao(volume.divide(new BigDecimal(vacasOrdenhadas), 2, RoundingMode.HALF_UP));	
		}
		
		super.handleOk();
		
		this.resume();
	}
	
	
	private void configureDataEntregaMesAnoReferencia(int mes, int ano){
		
		int mesAtual = mes;
		int anoAtual = ano;
		int mesInicio, anoInicio, mesFim, anoFim;
		
		CalendarioRecolha calendarioVigente = calendarioRecolhaService.getCalendarioVigente();
		
		if ( calendarioVigente == null ){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Nenhum Calendário Vigente");
			alert.setHeaderText("Nenhum Calendário de Recolha Vigente");
			alert.setContentText("Por favor, cadastre o calendário de recolha de leite!");
			alert.showAndWait();
		}
		
		//verifica se o calendario de recolha considera dias do mes anterior
		if ( calendarioVigente.getMesInicio().equals(CalendarioRecolha.MES_ANTERIOR) ){
			
			if ( mesAtual == 1 ){
				mesInicio = 12;
				anoInicio = anoAtual - 1;
			}else{
				mesInicio = mesAtual - 1;
				anoInicio = anoAtual;
			}
		}else{
			//mes corrente
			mesInicio = mesAtual;
			anoInicio = anoAtual;
		}
		
		dataInicio = LocalDate.of(anoInicio, mesInicio, calendarioVigente.getDiaInicio());
				
		//verifica se o calendario de recolha considera dias do mes anterior
		if ( calendarioVigente.getMesInicio().equals(CalendarioRecolha.MES_ANTERIOR) ){
			//mes corrente
			anoFim = anoAtual;
			mesFim = mesAtual;
		}else{
			if ( mesAtual == 12 ){
				mesFim = 1;
				anoFim = anoAtual + 1;
			}else{
				mesFim = mesAtual + 1;
				anoFim = anoAtual;
			}
		}
		
		dataFim = LocalDate.of(anoFim, mesFim, calendarioVigente.getDiaFim());
		
		Calendar cDataInicio = Calendar.getInstance();
		cDataInicio.setTimeInMillis(DateUtil.asDate(dataInicio).getTime());
		
		Calendar cDataFim = Calendar.getInstance();
		cDataFim.setTimeInMillis(DateUtil.asDate(dataFim).getTime());
		
		while ( cDataInicio.before(cDataFim) || cDataInicio.equals(cDataFim) ){
			EntregaLeite el = new EntregaLeite(LocalDate.of(cDataInicio.get(Calendar.YEAR), cDataInicio.get(Calendar.MONTH) + 1, cDataInicio.get(Calendar.DAY_OF_MONTH)), 0, 0, 0, calendarioVigente);
			service.save(el);
			cDataInicio.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		this.initializeTableOverview();
		table.setItems(data);
		lblPeriodo.setText(DateUtil.format(dataInicio) + " à " + DateUtil.format(dataFim));
		lblAno.setText(String.valueOf(selectedAnoReferencia));
		resume();
		
	}
	
	/**
	 * Faz a somatória das entregas e gera
	 * Média
	 * Total Entregas
	 * Valor Estimado
	 * Total Entregue
	 */
	protected void resume(){
		
		if ( data != null && data.size() > 0 ){
			
			int totalEntregas = 0;
			BigDecimal totalEntregue = new BigDecimal(0);
			int totalVacasOrdenhadas = 0;
			
			for (int i = 0; i < data.size(); i++){
				EntregaLeite e = data.get(i);
				if ( e.getVolume().compareTo(BigDecimal.ZERO) > 0 ){
					totalEntregas++;
					totalEntregue = totalEntregue.add(e.getVolume());
					totalVacasOrdenhadas += e.getNumeroVacasOrdenhadas();
				}
			}
			
			lblTotalEntregas.setText(String.valueOf(totalEntregas));
			lblTotalEntregue.setText(NumberFormatUtil.decimalFormat(totalEntregue));
			lblTotalVacasOrdenhadas.setText(String.valueOf(totalVacasOrdenhadas));
			if ( totalEntregue.compareTo(BigDecimal.ZERO) > 0 && totalEntregas > 0 ){
				lblMediaMes.setText(NumberFormatUtil.decimalFormat(totalEntregue.divide(new BigDecimal(totalEntregas), 2, RoundingMode.HALF_UP)));
				lblMediaProdutividadeMes.setText(NumberFormatUtil.decimalFormat(totalEntregue.divide(new BigDecimal(totalVacasOrdenhadas), 2, RoundingMode.HALF_UP)));
			}else{
				lblMediaMes.setText(NumberFormatUtil.decimalFormat(BigDecimal.ZERO));
				lblMediaProdutividadeMes.setText(NumberFormatUtil.decimalFormat(BigDecimal.ZERO));
			}
			
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
