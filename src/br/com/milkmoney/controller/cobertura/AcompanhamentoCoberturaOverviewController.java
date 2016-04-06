package br.com.milkmoney.controller.cobertura;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.function.Function;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.components.WaitReport;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.aborto.AbortoFormController;
import br.com.milkmoney.controller.cobertura.renderer.TableCellConfirmarPrenhezHyperlinkFactory;
import br.com.milkmoney.controller.cobertura.renderer.TableCellRegistrarAbortoHyperlinkFactory;
import br.com.milkmoney.controller.cobertura.renderer.TableCellRegistrarPartoHyperlinkFactory;
import br.com.milkmoney.controller.cobertura.renderer.TableCellSituacaoCoberturaFactory;
import br.com.milkmoney.controller.confirmacaoPrenhez.ConfirmacaoPrenhezFormController;
import br.com.milkmoney.controller.lactacao.LactacaoFormController;
import br.com.milkmoney.controller.parto.PartoFormController;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.FichaAnimalService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.LactacaoService;
import br.com.milkmoney.service.ParametroService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.CoberturaValidation;


@Controller
public class AcompanhamentoCoberturaOverviewController extends AbstractOverviewController<Integer, Cobertura> {

	//COBERTURAS
	@FXML private TableColumn<Cobertura, String>        dataColumn;
	@FXML private TableColumn<Animal, String>           animalColumn;
	@FXML private TableColumn<Cobertura, String>        reprodutorColumn;
	@FXML private TableColumn<Cobertura, String>        previsaoPartoColumn;
	@FXML private TableColumn<Cobertura, String>        dataPartoColumn;
	@FXML private TableColumn<Cobertura, String>        dataAbortoColumn;
	@FXML private TableColumn<Cobertura, String>        tipoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String>        situacaoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String>        statusColumn;
	@FXML private TableColumn<Cobertura, String>        dataConfirmacaoColumn;
	@FXML private TableColumn<Cobertura, String>        metodoConfirmacaoColumn;
	@FXML private Label                                 lblNaoConfirmadas, lblVazias, lblPrenhas, lblAbortadas, lblParidas, lblAno;
	@FXML private ToggleButton                          tbJan, tbFev, tbMar, tbAbr, tbMai, tbJun, tbJul, tbAgo, 
	                                                    tbSet, tbOut, tbNov, tbDez;
	
	@Autowired private CoberturaFormController          coberturaFormController;
	@Autowired private ConfirmacaoPrenhezFormController confirmacaoPrenhezFormController;
	@Autowired private AbortoFormController             abortoFormController;
	@Autowired private PartoFormController              partoFormController;
	@Autowired private LactacaoFormController           lactacaoFormController;
	@Autowired private AnimalService                    animalService;
	@Autowired private ParametroService                 parametroService;
	@Autowired private RelatorioService					relatorioService;
	@Autowired private LactacaoService                  lactacaoService;
	@Autowired private FichaAnimalService               fichaAnimalService;
	
	private int       									selectedAnoReferencia = LocalDate.now().getYear();
	private int 										selectedMesReferencia = LocalDate.now().getMonthValue();
	
	private ToggleGroup 								groupMes = new ToggleGroup();
	
	@FXML
	public void initialize() {
		
		dataColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("data"));
		animalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("femea"));
		reprodutorColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reprodutor"));
		previsaoPartoColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("previsaoParto"));
		dataPartoColumn.setCellFactory(new TableCellRegistrarPartoHyperlinkFactory<Cobertura,String>("dataParto", registrarParto));
		dataAbortoColumn.setCellFactory(new TableCellRegistrarAbortoHyperlinkFactory<Cobertura,String>("dataAborto", registrarAborto));
		tipoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("siglaTipoCobertura"));
		situacaoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("situacaoCobertura"));
		statusColumn.setCellFactory(new TableCellSituacaoCoberturaFactory<Cobertura,String>("situacaoCobertura"));
		dataConfirmacaoColumn.setCellFactory(new TableCellConfirmarPrenhezHyperlinkFactory<Cobertura,String>("dataConfirmacaoPrenhez", confirmarPrenhez));
		metodoConfirmacaoColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("metodoConfirmacaoPrenhez"));
		
		groupMes.getToggles().clear();
		groupMes.getToggles().addAll(tbJan, tbFev, tbMar, tbAbr, tbMai, tbJun, tbJul, tbAgo, tbSet, tbOut, tbNov, tbDez);
		groupMes.selectedToggleProperty().addListener((observable, oldValue, newValue) -> changeMesReferenciaListener( newValue ));
		groupMes.getToggles().get(selectedMesReferencia - 1).setSelected(true);
		
		super.initialize(coberturaFormController);
		
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleIncreaseAnoReferencia() {
		selectedAnoReferencia++;
		refreshTableOverview();
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleDecreaseAnoReferencia() {
		selectedAnoReferencia--;
		refreshTableOverview();
	}
	
	/**
	 * Ao alterar o mês de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	private void changeMesReferenciaListener(Toggle newValue) {
		
		int index = 1;
		for ( Toggle t : groupMes.getToggles() ){
			if ( t.isSelected() ){
				selectedMesReferencia = index;
				break;
			}
			index ++;
		}
		
		refreshTableOverview();
		
	}    
	
	@Override
	protected void refreshTableOverview() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				
				data.clear();
				table.getItems().clear();
				
				if ( inputPesquisa != null && inputPesquisa.getText() != null &&
						inputPesquisa.getText().length() > 0){
					data.addAll(((CoberturaService)service).defaultSearch(inputPesquisa.getText(), DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes())));
					setSearch(null);
				}else{
					data.addAll( ((CoberturaService)service).findByPeriodo(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes())));
				}
				
				//atualiza labels totais por situação
				int prenhas, vazias, abortadas, paridas, naoConfirmadas;
				prenhas = vazias = abortadas = paridas = naoConfirmadas = 0;
				
				for ( Cobertura c : data ){
					switch (c.getSituacaoCobertura()) {
					case SituacaoCobertura.NAO_CONFIRMADA:{
						naoConfirmadas++;
						break;
					}case SituacaoCobertura.PRENHA:{
						prenhas++;
						break;
					}case SituacaoCobertura.VAZIA:
						vazias++;
						break;
					case SituacaoCobertura.ABORTADA:
						abortadas++;
						break;
					case SituacaoCobertura.PARIDA:
						paridas++;
						break;
					default:
						break;
					}
				}
				
				if ( data.size() > 0 ){
					lblAbortadas.setText(abortadas + " | " + BigDecimal.valueOf((Double.parseDouble(String.valueOf(abortadas)) / data.size()) * 100).setScale(0, RoundingMode.HALF_EVEN) + "%" );
					lblVazias.setText(vazias + " | " + BigDecimal.valueOf((Double.parseDouble(String.valueOf(vazias)) / data.size()) * 100).setScale(0, RoundingMode.HALF_EVEN) + "%" );
					lblPrenhas.setText(prenhas + " | " + BigDecimal.valueOf((Double.parseDouble(String.valueOf(prenhas)) / data.size()) * 100).setScale(0, RoundingMode.HALF_EVEN) + "%" );
					lblParidas.setText(paridas + " | " + BigDecimal.valueOf((Double.parseDouble(String.valueOf(paridas)) / data.size()) * 100).setScale(0, RoundingMode.HALF_EVEN) + "%" );
					lblNaoConfirmadas.setText(naoConfirmadas + " | " + BigDecimal.valueOf((Double.parseDouble(String.valueOf(naoConfirmadas)) / data.size()) * 100).setScale(0, RoundingMode.HALF_EVEN) + "%" );
				}else{
					lblAbortadas.setText("0");
					lblVazias.setText("0");
					lblPrenhas.setText("0");
					lblParidas.setText("0");
					lblNaoConfirmadas.setText("0");
				}
				
				lblAno.setText(String.valueOf(selectedAnoReferencia));
				
				table.setItems(data);
				table.layout();
				updateLabelNumRegistros();
			}
		});
		
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
	
	@Override
	public String getFormTitle() {
		return "Cobertura";
	}
	
	@Override
	public String getFormName() {
		return "view/cobertura/CoberturaOverview.fxml";
	}

	@Override
	@Resource(name = "coberturaService")
	protected void setService(IService<Integer, Cobertura> service) {
		super.setService(service);
	}
	
	@FXML
	private void imprimir(){
		Object[] params = new Object[]{
				DateUtil.asDate(dataInicioMes()),
				DateUtil.asDate(dataFimMes()),
				"",
				0,
				0,
				0,
				"",
				0,
				0
		};
		WaitReport.wait(relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_COBERTURA, params), MainApp.primaryStage);
		
	}
	
	//====FUNCTIONS
	
	Function<Integer, Boolean> registrarParto = index -> {
		table.getSelectionModel().select(index);
		
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return false;
		}
		if ( getObject().getParto() == null ){
			
			CoberturaValidation.validaRegistroPartoCobertura(getObject(), lactacaoService.findLastBeforeDate(getObject().getFemea(), getObject().getData()));
			
			partoFormController.setState(State.CREATE_TO_SELECT);
			partoFormController.setObject(new Parto(getObject()));
			partoFormController.showForm();
			
			if ( partoFormController.getObject() != null && partoFormController.getObject().getLactacao() != null ){
				getObject().setParto(partoFormController.getObject());
				((CoberturaService)service).registrarParto(getObject());
				refreshObjectInTableView.apply(service.findById(getObject().getId()));
			}	
			
		}else{
			
			partoFormController.setObject(getObject().getParto());
			partoFormController.showForm();
			refreshObjectInTableView.apply(service.findById(getObject().getId()));
			
		}
		
		return true;
	};
	
	Function<Integer, Boolean> registrarAborto = index -> {
		table.getSelectionModel().select(index);
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return false;
		}
		
		abortoFormController.setObject(getObject());
		abortoFormController.showForm();
    	
		refreshObjectInTableView.apply(service.findById(getObject().getId()));
		return true;
	};
	
	Function<Integer, Boolean> confirmarPrenhez = index -> {
		table.getSelectionModel().select(index);
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return false;
		}
		
		confirmacaoPrenhezFormController.setObject(getObject());
    	confirmacaoPrenhezFormController.showForm();
    	
		refreshObjectInTableView.apply(service.findById(getObject().getId()));
		return true;
	};
	
	
	
}
