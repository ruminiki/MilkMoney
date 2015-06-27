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

import br.com.milksys.components.CustomAlert;
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
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleIncreaseAnoReferencia() {
		selectedAnoReferencia++;
		configuraTabelaDiasMesSelecionado();
		initializeTableOverview();
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleDecreaseAnoReferencia() {
		selectedAnoReferencia--;
		configuraTabelaDiasMesSelecionado();
		initializeTableOverview();
	}
	
	/**
	 * Ao alterar o mês de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	private void changeMesReferenciaListener(String newValue) {
		selectedMesReferencia = meses.indexOf(newValue) + 1;
		configuraTabelaDiasMesSelecionado();
		initializeTableOverview();
	}    
	
	@Override
	protected void initializeTableOverview() {
		super.data.clear();
		super.data.addAll(service.findAllByPeriodoAsObservableList(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes())));
		recarregaPrecoLeite();
	}
	
	@Override
	protected void handleOk() {
		int vacasOrdenhadas = getObject().getNumeroVacasOrdenhadas();
		BigDecimal volumeProduzido = getObject().getVolumeProduzido();
		
		if ( vacasOrdenhadas > 0 && volumeProduzido.compareTo(BigDecimal.ZERO) > 0 ){
			getObject().setMediaProducao(volumeProduzido.divide(new BigDecimal(vacasOrdenhadas), 2, RoundingMode.HALF_UP));	
		}
		
		PrecoLeite precoLeite = precoLeiteService.findByMesAno(getObject().getMes(), getObject().getAno());
		if ( precoLeite != null ){
			getObject().setValor(precoLeite.getValor().multiply(getObject().getVolumeEntregue()));
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
			ProducaoLeite producaoLeite = new ProducaoLeite(DateUtil.asLocalDate(cDataInicio.getTime()), 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
			service.save(producaoLeite);
			cDataInicio.add(Calendar.DAY_OF_MONTH, 1);
		}
		
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
				if ( totalVacasOrdenhadas > 0 )  
					lblMediaProdutividadeMes.setText(NumberFormatUtil.decimalFormat(totalEntregue.divide(new BigDecimal(totalVacasOrdenhadas), 2, RoundingMode.HALF_UP)));
				else
					lblMediaProdutividadeMes.setText(NumberFormatUtil.decimalFormat(BigDecimal.ZERO));
			}else{
				lblMediaMes.setText(NumberFormatUtil.decimalFormat(BigDecimal.ZERO));
				lblMediaProdutividadeMes.setText(NumberFormatUtil.decimalFormat(BigDecimal.ZERO));
			}
			
			lblValorEstimado.setText(NumberFormatUtil.decimalFormat(valor));
			if ( !precoLeiteService.isPrecoCadastrado(meses.get(selectedMesReferencia-1), selectedAnoReferencia) ){
				lblValorEstimado.setText("Cadastrar Preço");
			}
			lblAno.setText(String.valueOf(selectedAnoReferencia));
		}
	}
	
	/**
	 * Quando não houver preço de leite informado para o mês
	 * habilita o cadastro pela tela de produção.
	 */
	@FXML
	private void handleCadastrarPrecoLeite(){
		
		precoLeiteController.state = State.INSERT_TO_SELECT;
		
		PrecoLeite precoLeite = precoLeiteService.findByMesAno(meses.get(selectedMesReferencia-1), selectedAnoReferencia);
		
		if ( precoLeite == null ){ 
			precoLeite = new PrecoLeite();
			precoLeite.setMesReferencia(meses.get(selectedMesReferencia-1));
			precoLeite.setAnoReferencia(selectedAnoReferencia);
			precoLeite.setCodigoMes(selectedMesReferencia);
		}
		
		precoLeiteController.setObject(precoLeite);
		precoLeiteController.showForm(null);
		if ( precoLeiteController.getObject() != null ){
			
			recarregaPrecoLeite();
			resume();
			
		}
		
	}
	
	/**
	 * Método que percorre lista de objetos atualizando o valor com base no preço do leite do mês
	 */
	private void recarregaPrecoLeite(){
		
		PrecoLeite precoLeite = precoLeiteService.findByMesAno(meses.get(selectedMesReferencia-1), selectedAnoReferencia);
		if ( precoLeite != null ){
			//varre a tabela atualizando os valores diários
			for ( int index = 0; index < data.size(); index++ ){
				ProducaoLeite producaoLeite = data.get(index);
				producaoLeite.setValor(precoLeite.getValor().multiply(producaoLeite.getVolumeEntregue()));
				data.set(index, producaoLeite);
			}
		}
		
	}
	
	/**
	 * Abre formulário para cadastrar a produção individual de um animal
	 */
	protected void handleCadastrarProducaoIndividual() {
		producaoIndividualController.state = State.INSERT_TO_SELECT;
		
		ProducaoIndividual producaoIndividual = new ProducaoIndividual();
		producaoIndividual.setData(getObject().getData());
		
		producaoIndividualController.setObject(producaoIndividual);
		producaoIndividualController.showForm(producaoIndividualController.getExternalFormName());
		
	}

	protected boolean isInputValid() {
		if ( getObject().getVolumeEntregue().compareTo(getObject().getVolumeProduzido()) > 0 ){
			CustomAlert.mensagemAlerta("O volume entregue não pode ser maior que o volume produzido.");
			return false;
		}
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
	
	@Override
	protected ProducaoLeite getObject() {
		return (ProducaoLeite) super.object;
	}
	
}
