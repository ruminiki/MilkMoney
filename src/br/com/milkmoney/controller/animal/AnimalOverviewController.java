package br.com.milkmoney.controller.animal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.components.WaitReport;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.animal.renderer.AnimalTableUtils;
import br.com.milkmoney.controller.animal.renderer.PopUpMenu;
import br.com.milkmoney.controller.animal.renderer.TableCellOpcoesFactory;
import br.com.milkmoney.controller.arvoreGenealogica.ArvoreGenealogicaOverviewController;
import br.com.milkmoney.controller.cobertura.CoberturaFormController;
import br.com.milkmoney.controller.cobertura.CoberturaOverviewController;
import br.com.milkmoney.controller.confirmacaoPrenhez.ConfirmacaoPrenhezEmLoteFormController;
import br.com.milkmoney.controller.confirmacaoPrenhez.ConfirmacaoPrenhezFormController;
import br.com.milkmoney.controller.evolucaoRebanho.EvolucaoRebanhoOverviewController;
import br.com.milkmoney.controller.fichaAnimal.FichaAnimalOverviewController;
import br.com.milkmoney.controller.indicador.IndicadorOverviewController;
import br.com.milkmoney.controller.lactacao.LactacaoOverviewController;
import br.com.milkmoney.controller.lote.MovimentacaoAnimalLoteFormController;
import br.com.milkmoney.controller.morteAnimal.MorteAnimalFormController;
import br.com.milkmoney.controller.parto.PartoFormController;
import br.com.milkmoney.controller.producaoIndividual.ProducaoIndividualOverviewController;
import br.com.milkmoney.controller.projecao.ProjecaoOverviewController;
import br.com.milkmoney.controller.raca.RacaOverviewController;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.controller.vendaAnimal.VendaAnimalFormController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.model.FinalidadeAnimal;
import br.com.milkmoney.model.Lote;
import br.com.milkmoney.model.MorteAnimal;
import br.com.milkmoney.model.OptionChoiceFilter;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.Raca;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.model.SimNao;
import br.com.milkmoney.model.SituacaoAnimal;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.model.State;
import br.com.milkmoney.model.VendaAnimal;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.FichaAnimalService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.LactacaoService;
import br.com.milkmoney.service.LoteService;
import br.com.milkmoney.service.MorteAnimalService;
import br.com.milkmoney.service.ParametroService;
import br.com.milkmoney.service.PartoService;
import br.com.milkmoney.service.RacaService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.service.VendaAnimalService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.CoberturaValidation;

import com.sun.javafx.scene.control.skin.TableViewSkin;

@Controller
public class AnimalOverviewController extends AbstractOverviewController<Integer, Animal> {

	@FXML private TableColumn<Animal, String> nomeColumn;
	@FXML private TableColumn<Animal, String> situacaoUltimaCobertura;
	@FXML private TableColumn<Animal, Date> dataNascimentoColumn;
	@FXML private TableColumn<Raca, String> racaColumn;
	@FXML private TableColumn<Animal, String> numeroColumn;
	@FXML private TableColumn<Lote, String> loteColumn;
	@FXML private TableColumn<String, String> sexoColumn;
	@FXML private TableColumn<Animal, String> situacaoAnimalColumn;
	@FXML private TableColumn<Animal, Long> idadeColumn;
	@FXML private TableColumn<Animal, String> opcoesColumn;
	@FXML private Label lblNumeroServicos, lblDataUltimaCobertura, lblProximoServico, lblNumeroPartos, lblIdadePrimeiroParto, 
						lblIdadePrimeiraCobertura, lblDiasEmLactacao, lblDiasEmAberto, lblIntervaloPrimeiroParto, lblDataSecar,
						lblAnimal, lblDataUltimoParto, lblDataProximoParto, lblSituacaoUltimaCobertura, lblPai, lblMae, 
						lblEmLactacao, lblSecas, lblNaoDefinidas, lblTotalVacas, lblBezerras, lblNovilhas, lblNumeroLactacoes, lblMediaProducao, 
						lblUltimoTratamento, lblLote, lblEficienciaReprodutiva;
	@FXML private Hyperlink hlVisualizarUltimoParto, hlSecarAnimal, hlRegistrarParto, hlEditarCobertura, hlRegistrarCobertura, hlConfirmarPrenhez;
	@FXML private VBox sideBar;
	
	//filters
	@FXML private ChoiceBox<OptionChoiceFilter> choiceFilter;
	@FXML private ChoiceBox<String> inputSituacaoAnimal, inputFinalidadeAnimal, inputSexo, inputCobertaInseminada;
	@FXML private ChoiceBox<String> inputSituacaoCobertura;
	@FXML private ChoiceBox<Lote> inputLote;
	@FXML private ChoiceBox<Raca> inputRaca;
	@FXML private TextField inputIdadeDe, inputIdadeAte, inputDiasPosParto, inputDiasPosCobertura, inputNumeroPartos, inputSecarEmXDias;
	
	//services
	@Autowired private MorteAnimalService morteAnimalService;
	@Autowired private VendaAnimalService vendaAnimalService;
	@Autowired private FichaAnimalService fichaAnimalService;
	@Autowired private LactacaoService lactacaoService;
	@Autowired private RelatorioService relatorioService;
	@Autowired private ParametroService parametroService;
	@Autowired private PartoService partoService;
	@Autowired private CoberturaService coberturaService;
	@Autowired private LoteService loteService;
	@Autowired private RacaService racaService;
	
	//controllers
	@Autowired private ConfirmacaoPrenhezFormController confirmacaoPrenhezFormController;
	@Autowired private CoberturaOverviewController coberturaOverviewController;
	@Autowired private CoberturaFormController coberturaFormController;
	@Autowired private RacaOverviewController racaController;
	@Autowired private AnimalReducedOverviewController animalReducedController;
	@Autowired private MorteAnimalFormController morteAnimalFormController;
	@Autowired private VendaAnimalFormController vendaAnimalFormController;
	@Autowired private LactacaoOverviewController lactacaoOverviewController;
	@Autowired private FichaAnimalOverviewController fichaAnimalOverviewController;
	@Autowired private ProducaoIndividualOverviewController producaoIndividualOverviewController;
	//@Autowired private IndicadorOverviewController indicadorOverviewController;
	@Autowired private IndicadorOverviewController acompanhamentoIndicadoresOverviewController;
	@Autowired private PartoFormController partoFormController;
	@Autowired private EvolucaoRebanhoOverviewController evolucaoRebanhoOverviewController;
	@Autowired private ProjecaoOverviewController projecaoRebanhoOverviewController;
	@Autowired private ArvoreGenealogicaOverviewController arvoreGenealogicaOverviewController;
	@Autowired private MovimentacaoAnimalLoteFormController movimentacaoAnimalLoteFormController;
	@Autowired private ConfirmacaoPrenhezEmLoteFormController confirmacaoPrenhezEmLoteFormController;
	
	private FichaAnimal fichaAnimal;
	private PopUpMenu popUpMenu = null;
	private MenuItem selecionarTodos = new MenuItem("Selecionar Todos");
	
	@FXML
	public void initialize() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if ( popUpMenu == null ){
					popUpMenu = new PopUpMenu(coberturas,
							  novaCobertura, 
							  confirmarPrenhez,
							  novoParto,
							  ultimaCobertura,
							  ultimoParto,
							  lactacoes,
							  controleLeiteiro,
							  vendaAnimal,
							  morteAnimal,
							  linhaTempoAnimal,
							  exibirFichaAnimal);
				}
			
				
				situacaoAnimalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("situacaoAnimal"));
				situacaoUltimaCobertura.setCellValueFactory(new PropertyValueFactory<Animal,String>("situacaoUltimaCobertura"));
				numeroColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
				nomeColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("nome"));
				loteColumn.setCellValueFactory(new PropertyValueFactory<Lote,String>("loteFormatado"));
				dataNascimentoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataNascimento"));
				idadeColumn.setCellValueFactory(new PropertyValueFactory<Animal,Long>("idade"));
				racaColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("raca"));
				sexoColumn.setCellValueFactory(new PropertyValueFactory<String,String>("sexoFormatado"));
				opcoesColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
				opcoesColumn.setCellFactory(new TableCellOpcoesFactory<Animal,String>(popUpMenu));

				table.widthProperty().addListener((observable, oldValue, newValue) -> resizeColunaTabela(newValue));
				
				//filters
				if ( inputSituacaoAnimal.getItems().size() <= 0 ){
					inputSituacaoAnimal.getItems().setAll(SituacaoAnimal.getItems());
				}
				
				if ( inputSituacaoCobertura.getItems().size() <= 0 ){
					inputSituacaoCobertura.getItems().setAll(SituacaoCobertura.getAllItems());
				}
				
				if ( inputLote.getItems().size() <= 0 ){
					inputLote.getItems().setAll(loteService.findAll());
				}
				
				if ( inputRaca.getItems().size() <= 0 ){
					inputRaca.getItems().setAll(racaService.findAll());
				}
				
				if ( inputSexo.getItems().size() <= 0 ){
					inputSexo.getItems().setAll(Sexo.getItems());
				}
				
				if ( inputFinalidadeAnimal.getItems().size() <= 0 ){
					inputFinalidadeAnimal.getItems().setAll(FinalidadeAnimal.getItems());
				}
				
				if ( inputCobertaInseminada.getItems().size() <= 0 ){
					inputCobertaInseminada.getItems().setAll(SimNao.getItems());
				}
				
				initialize((AnimalFormController)MainApp.getBean(AnimalFormController.class));
				
				if ( table.getItems().size() > 0 ){
					table.getSelectionModel().select(0);
				}
				resizeColunaTabela(table.getWidth());
				table.getContextMenu().getItems().add(selecionarTodos);
			}
		});
		
		table.setSkin(new TableViewSkin<>(table));
		AnimalTableUtils.addCustomTableMenu(table, movimentarAnimaisLote, confirmarPrenhezEmLote);
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		selecionarTodos.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	table.getSelectionModel().selectAll();
		    }
		});
		
	}
	
	private void resizeColunaTabela(Number newWidth){
		
		double width = newWidth.doubleValue();
		
		if ( width >= 655 ){
			nomeColumn.minWidthProperty().set((width - 535) * 0.50);
			loteColumn.minWidthProperty().set((width - 535) * 0.25);
			racaColumn.minWidthProperty().set((width - 535) * 0.25);
		}
		
	}
	
	@FXML
	private void handleEnterFilter(KeyEvent event){
		
		if ( event.getCode().equals(KeyCode.ENTER) ) {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(AnimalService.FILTER_SITUACAO_ANIMAL, (inputSituacaoAnimal.getValue() != null ? inputSituacaoAnimal.getValue() : null));
			params.put(AnimalService.FILTER_SITUACAO_COBERTURA, inputSituacaoCobertura.getValue() != null ? inputSituacaoCobertura.getValue() : null);
			params.put(AnimalService.FILTER_IDADE_DE, !inputIdadeDe.getText().isEmpty() ? inputIdadeDe.getText() : null);
			params.put(AnimalService.FILTER_IDADE_ATE, !inputIdadeAte.getText().isEmpty() ? inputIdadeAte.getText() : null);
			params.put(AnimalService.FILTER_LOTE, inputLote.getValue() != null ? String.valueOf(inputLote.getValue().getId()) : null);
			params.put(AnimalService.FILTER_RACA, inputRaca.getValue() != null ? String.valueOf(inputRaca.getValue().getId()) : null);
			params.put(AnimalService.FILTER_SEXO, inputSexo.getValue() != null ? inputSexo.getValue() : null);
			params.put(AnimalService.FILTER_DIAS_POS_PARTO, !inputDiasPosParto.getText().isEmpty() ? inputDiasPosParto.getText() : null);
			params.put(AnimalService.FILTER_DIAS_POS_COBERTURA, !inputDiasPosCobertura.getText().isEmpty() ? inputDiasPosCobertura.getText() : null);
			params.put(AnimalService.FILTER_NUMERO_PARTOS, !inputNumeroPartos.getText().isEmpty() ? inputNumeroPartos.getText() : null);
			params.put(AnimalService.FILTER_COBERTAS, inputCobertaInseminada.getValue() != null ? inputCobertaInseminada.getValue() : null);
			params.put(AnimalService.FILTER_SECAR_EM_X_DIAS, !inputSecarEmXDias.getText().isEmpty() ? inputSecarEmXDias.getText() : null);
			params.put(AnimalService.FILTER_FINALIDADE_ANIMAL, (inputFinalidadeAnimal.getValue() != null ? inputFinalidadeAnimal.getValue() : null));
			table.getItems().setAll( ((AnimalService)service).fill(params) );
			updateLabelNumRegistros();	
		}
		
		if (event.getCode().equals(KeyCode.ESCAPE)) {
			clearFilter();
		}
		
	}
	
	@Override
	public void clearFilter() {
		
		inputSituacaoAnimal.getSelectionModel().clearSelection();
		inputLote.getSelectionModel().clearSelection();
		inputSexo.getSelectionModel().clearSelection();
		inputSituacaoCobertura.getSelectionModel().clearSelection();
		inputRaca.getSelectionModel().clearSelection();
		inputFinalidadeAnimal.getSelectionModel().clearSelection();
		inputCobertaInseminada.getSelectionModel().clearSelection();
		inputIdadeDe.clear();
		inputIdadeAte.clear();
		inputDiasPosParto.clear();
		inputDiasPosCobertura.clear();
		inputNumeroPartos.clear(); 
		inputSecarEmXDias.clear();

		if ( inputPesquisa != null ){
			inputPesquisa.clear();
		}
		
		super.refreshTableOverview();

	}
	
	@Override
	protected void selectRowTableHandler(Animal animal) {
		super.selectRowTableHandler(animal);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if ( animal != null ){
					
					fichaAnimal = fichaAnimalService.generateFichaAnimal(animal, fichaAnimalService.getAllFields());
					
					if ( fichaAnimal != null ){
						lblNumeroServicos.setText(""+fichaAnimal.getNumeroServicosAtePrenhez());
						lblDataUltimaCobertura.setText(fichaAnimal.getDataUltimaCobertura() != null ? DateUtil.format(fichaAnimal.getDataUltimaCobertura()) : "--"); 
						lblProximoServico.setText(fichaAnimal.getProximoServico() != null ? DateUtil.format(fichaAnimal.getProximoServico()) : "--"); 
						lblNumeroPartos.setText(""+fichaAnimal.getNumeroPartos() + " - "  + fichaAnimal.getNumeroCriasFemea() + "F" + " - " + fichaAnimal.getNumeroCriasMacho() + "M"); 
						lblIdadePrimeiroParto.setText(""+fichaAnimal.getIdadePrimeiroParto());
						lblIdadePrimeiraCobertura.setText(fichaAnimal.getIdadePrimeiraCobertura()); 
						lblDiasEmLactacao.setText(fichaAnimal.getDiasEmLactacao()); 
						lblDiasEmAberto.setText(fichaAnimal.getDiasEmAberto()); 
						lblIntervaloPrimeiroParto.setText(fichaAnimal.getIntervaloEntrePartos());
						lblDataSecar.setText(fichaAnimal.getDataPrevisaoEncerramentoLactacao() != null ? DateUtil.format(fichaAnimal.getDataPrevisaoEncerramentoLactacao()) : "--");
						lblDataProximoParto.setText(fichaAnimal.getDataProximoParto() != null ? DateUtil.format(fichaAnimal.getDataProximoParto()) : "--");	
						lblDataUltimoParto.setText(fichaAnimal.getDataUltimoParto() != null ? DateUtil.format(fichaAnimal.getDataUltimoParto()) : "--");
						lblSituacaoUltimaCobertura.setText(fichaAnimal.getSituacaoUltimaCobertura());
						lblLote.setText(fichaAnimal.getLote());
						lblMediaProducao.setText(fichaAnimal.getMediaProducao() != null && fichaAnimal.getMediaProducao().compareTo(BigDecimal.ZERO) > 0 ? String.valueOf(fichaAnimal.getMediaProducao()) : "--");
						lblUltimoTratamento.setText(fichaAnimal.getUltimoTratamento());
						lblNumeroLactacoes.setText(fichaAnimal.getNumeroLactacoes() > 0 ? String.valueOf(fichaAnimal.getNumeroLactacoes()) : "--");
						lblEficienciaReprodutiva.setText(fichaAnimal.getEficienciaReprodutiva() + "%");
						
						//links
						hlVisualizarUltimoParto.setVisible(fichaAnimal.getDataUltimoParto() != null);
						if (fichaAnimal.getDataUltimoParto() != null){
							hlVisualizarUltimoParto.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									ultimoParto.apply(getObject());
								}
							});
						}
						
						hlSecarAnimal.setVisible(fichaAnimal.getDataPrevisaoEncerramentoLactacao() != null);
						if (fichaAnimal.getDataPrevisaoEncerramentoLactacao() != null){
							hlSecarAnimal.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									lactacoes.apply(getObject());
								}
							});
						}
						
						hlRegistrarParto.setVisible(fichaAnimal.getDataProximoParto() != null);
						if (fichaAnimal.getDataProximoParto() != null){
							hlRegistrarParto.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									novoParto.apply(getObject());
								}
							});
						}
						
						hlEditarCobertura.setVisible(fichaAnimal.getDataUltimaCobertura() != null);
						if ( fichaAnimal.getDataUltimaCobertura() != null ){
							hlEditarCobertura.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									ultimaCobertura.apply(getObject());
								}
							});
						}
						
						hlRegistrarCobertura.setVisible(fichaAnimal.getProximoServico() != null);
						if ( fichaAnimal.getProximoServico() != null ){
							hlRegistrarCobertura.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									novaCobertura.apply(getObject());
								}
							});
						}
						
						hlConfirmarPrenhez.setVisible(fichaAnimal.getDataUltimaCobertura() != null);
						if ( fichaAnimal.getDataUltimaCobertura() != null ){
							if ( fichaAnimal.getSituacaoUltimaCobertura().equals(SituacaoCobertura.NAO_CONFIRMADA) ){
								hlConfirmarPrenhez.setText("confirmar");
							}else{
								if ( fichaAnimal.getSituacaoUltimaCobertura().matches(SituacaoCobertura.PRENHA + "|" + SituacaoCobertura.VAZIA) ){
									hlConfirmarPrenhez.setText("visualizar");
								}else{
									hlConfirmarPrenhez.setVisible(false);
								}
							}
							hlConfirmarPrenhez.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									confirmarPrenhez.apply(getObject());
								}
							});
						}
					}
					lblPai.setText(animal.getPaiEnseminacaoArtificial() != null ? animal.getPaiEnseminacaoArtificial().toString() : ( animal.getPaiMontaNatural() != null ? animal.getPaiMontaNatural().toString() : "--" ));
					lblMae.setText(animal.getMae() != null ? animal.getMae().toString() : "--");
					lblAnimal.setText(animal.toString());
					
					lblEmLactacao.setText( String.valueOf( ((AnimalService)service).countAllFemeasEmLactacao(DateUtil.today)) );
					lblTotalVacas.setText( String.valueOf( ((AnimalService)service).countAllVacasAtivas(DateUtil.today)) );
					lblSecas.setText( String.valueOf( ((AnimalService)service).countAllFemeasSecas(DateUtil.today)) );
					lblBezerras.setText( String.valueOf( ((AnimalService)service).countAllBezerras(DateUtil.today)) );
					lblNovilhas.setText( String.valueOf( ((AnimalService)service).countAllNovilhas(DateUtil.today)) );
					
				}
			}
		});
	}
	
	@Override
	public String getFormTitle() {
		return "Animal";
	}
	
	@Override
	public String getFormName() {
		return "view/animal/AnimalOverview.fxml";
	}
	
	@Override
	@Resource(name = "animalService")
	protected void setService(IService<Integer, Animal> service) {
		super.setService(service);
	}
	
	//------FUNCTIONS---
	
	Function<Animal, Boolean> lactacoes = animal -> {
		if ( animal != null ){
			if ( getObject().getSexo().equals(Sexo.FEMEA) ){
				lactacaoOverviewController.setAnimal(getObject());
				lactacaoOverviewController.showForm();
				selecionaAnimal(animal);
			}else{
				CustomAlert.mensagemInfo("Somente animais fêmeas podem ter a lactação encerrada.");
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Animal, Boolean> controleLeiteiro = animal -> {
		if ( animal != null ){
			if ( getObject().getSexo().equals(Sexo.FEMEA) ){
				
				//se o animal tiver morto ou vendido habilita apenas consulta
				boolean disabled = getObject().getSituacaoAnimal().matches(SituacaoAnimal.MORTO + "|" + SituacaoAnimal.VENDIDO);
				producaoIndividualOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, disabled);
				producaoIndividualOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, disabled);
				producaoIndividualOverviewController.getFormConfig().put(AbstractOverviewController.REMOVE_DISABLED, disabled);
				
				producaoIndividualOverviewController.setAnimal(getObject());
				producaoIndividualOverviewController.showForm();
				selecionaAnimal(animal);
			}else{
				CustomAlert.mensagemInfo("Somente podem ter registro de produção, animais fêmeas. "
						+ "Por favor, selecione outro animal e tente novamente.");
			}
			
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Animal, Boolean> exibirFichaAnimal = animal -> {
		if ( animal != null ){
			Object[] params = new Object[]{
					table.getSelectionModel().getSelectedItem().getId()
			};
			WaitReport.wait(relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
					RelatorioService.FICHA_COMPLETA_ANIMAL, params), table.getScene().getWindow());
			
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Animal, Boolean> linhaTempoAnimal = animal -> {
		if ( animal != null ){
			fichaAnimalOverviewController.setAnimal(getObject());
			fichaAnimalOverviewController.showForm();
			selecionaAnimal(animal);
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		table.getSelectionModel().select(animal);
		return true;
	};
	
	Function<Animal, Boolean> coberturas = animal -> {
		if ( animal != null ){
			if ( getObject().getSexo().equals(Sexo.FEMEA) ){
				coberturaOverviewController.setObject(new Cobertura(getObject()));
				coberturaOverviewController.setFemea(getObject());
				//se o animal tiver morto ou vendido apenas consulta as coberturas
				boolean disabled = getObject().getSituacaoAnimal().matches(SituacaoAnimal.MORTO + "|" + SituacaoAnimal.VENDIDO);
				coberturaOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, disabled);
				coberturaOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, disabled);
				coberturaOverviewController.getFormConfig().put(AbstractOverviewController.REMOVE_DISABLED, disabled);
				coberturaOverviewController.showForm();
				selecionaAnimal(animal);
			}else{
				CustomAlert.mensagemInfo("Por favor, selecione um animal fêmea, para ter acesso as coberturas. "
						+ "Selecione outro animal e tente novamente.");
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Animal, Boolean> novaCobertura = animal -> {
		if ( animal != null ){
			//Cobertura cobertura = coberturaService.findLastCoberturaAnimal(getObject());
			
			coberturaFormController.setObject(new Cobertura(getObject(), 
					fichaAnimal.getProximoServico() == null ? new Date() : fichaAnimal.getProximoServico()));
			coberturaFormController.showForm();
			selecionaAnimal(animal);
			
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Animal, Boolean> ultimaCobertura = animal -> {
		if ( animal != null ){
			Cobertura cobertura = coberturaService.findLastCoberturaAnimal(getObject());
			if ( cobertura != null ){
				coberturaFormController.setObject(cobertura);
				coberturaFormController.showForm();
				selecionaAnimal(animal);
			}else{
				CustomAlert.mensagemInfo("O animal selecionado ainda não tem cobertura registrada.");
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Animal, Boolean> ultimoParto = animal -> {
		if ( animal != null ){
			Parto parto = partoService.findLastParto(getObject(), DateUtil.today);
			if ( parto != null ){
				partoFormController.setObject(parto);
				partoFormController.showForm();
				selecionaAnimal(animal);
			}else{
				CustomAlert.mensagemInfo("O animal selecionado ainda não teve nenhum parto registrado.");
			}
		
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Animal, Boolean> confirmarPrenhez = animal -> {
		if ( animal != null ){
			Cobertura cobertura = coberturaService.findLastCoberturaAnimal(getObject());
			if ( cobertura != null ){
				confirmacaoPrenhezFormController.setObject(cobertura);
		    	confirmacaoPrenhezFormController.showForm();
		    	selecionaAnimal(animal);
			}else{
				CustomAlert.mensagemInfo("O animal selecionado ainda não tem cobertura registrada. Primeiro registre a cobertura para então registrar a confirmação de prenhez.");
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Animal, Boolean> novoParto = animal -> {
		if ( animal != null ){
			
			Cobertura cobertura = coberturaService.findCoberturaAtivaByAnimal(getObject());
			if ( cobertura != null ){
				if ( cobertura.getParto() == null ){
					CoberturaValidation.validaRegistroPartoCobertura(cobertura, 
							lactacaoService.findLastBeforeDate(cobertura.getFemea(), cobertura.getData()));
					
					partoFormController.setState(State.CREATE_TO_SELECT);
					partoFormController.setObject(new Parto(cobertura));
					partoFormController.showForm();
					
					if ( partoFormController.getObject() != null && partoFormController.getObject().getLactacao() != null ){
						cobertura.setParto(partoFormController.getObject());
						coberturaService.registrarParto(cobertura);
						selecionaAnimal(animal);
					}	
				}else{
					CustomAlert.mensagemInfo("A última cobertura do animal já teve o parto registrado no dia " + cobertura.getParto().getData() + ".");
				}
			}else{
				CustomAlert.mensagemInfo("O animal selecionado ainda não possui cobertura/inseminação registrada. "
						+ "Cadastre a cobertura/inseminação e então registre o parto.");
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Animal, Boolean> morteAnimal = animal -> {
		if ( animal != null ){
			if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.VENDIDO) ){
				CustomAlert.mensagemInfo("O animal já foi vendido, não é possível registrar a morte.");
			}else{
				MorteAnimal morteAnimal = morteAnimalService.findByAnimal(animal);
				if ( morteAnimal == null ){
					morteAnimal = new MorteAnimal(animal);
				}
				morteAnimalFormController.setObject(morteAnimal);
				morteAnimalFormController.showForm();
				selecionaAnimal(animal);
			}
		}
		return true;
	};
	
	Function<Animal, Boolean> vendaAnimal = animal -> {
		if ( animal != null ){
			if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.MORTO) ){
				CustomAlert.mensagemInfo("O animal está morto, não é possível registrar a venda.");
			}else{
				VendaAnimal vendaAnimal = vendaAnimalService.findByAnimal(animal);
				if ( vendaAnimal == null ){
					vendaAnimal = new VendaAnimal(animal);
				}
				vendaAnimalFormController.setObject(vendaAnimal);
				vendaAnimalFormController.showForm();
				selecionaAnimal(animal);
			}
		}
		return true;
	};
	
	Function<List<Animal>, Boolean> movimentarAnimaisLote = animais -> {
		if ( animais != null && animais.size() > 0 ){
			movimentacaoAnimalLoteFormController.setAnimaisSelecionados(table.getSelectionModel().getSelectedItems());
			movimentacaoAnimalLoteFormController.showForm();
			for ( Animal a : animais ){
				a.setLote(movimentacaoAnimalLoteFormController.getLoteSelecionado());
				refreshObjectInTableView.apply(a);
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<List<Animal>, Boolean> confirmarPrenhezEmLote = animais -> {
		if ( animais != null && animais.size() > 0 ){
			confirmacaoPrenhezEmLoteFormController.setAnimaisSelecionados(table.getSelectionModel().getSelectedItems());
			confirmacaoPrenhezEmLoteFormController.showForm();
			for ( Animal a : animais ){
				a.setSituacaoUltimaCobertura(confirmacaoPrenhezEmLoteFormController.getNovaSituacaoCobertura());
				refreshObjectInTableView.apply(a);
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	private void selecionaAnimal(Animal animal){
		animal = service.findById(animal.getId());
		refreshObjectInTableView(animal);
		table.getSelectionModel().select(animal);
		setObject(animal);
	}
	
	//-------------FILTRO RÁPIDO----------------------------------
	
	@FXML 
	private void handleOpenIndicadores(){
		acompanhamentoIndicadoresOverviewController.showForm();
	}
	
	//------ANÁLISE REPORT----
	@FXML
	private void gerarRelatorioAnaliseReprodutiva(){
		//os ids dos animais selecionados são passados como parâmetro
		StringBuilder sb = new StringBuilder();
		
		for ( Animal animal : table.getItems() ){
			sb.append(animal.getId());
			sb.append(",");
		}
		
		sb.setLength(sb.length() - 1);
		
		Object[] params = new Object[]{
			table.getItems(),
			sb.toString()
		};
		
		WaitReport.wait(relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.FICHA_ANIMAL, params), MainApp.primaryStage);
	
	}
	
	@FXML
	private void handleArvoreGenealogica(){
		Animal animal = getObject();
		arvoreGenealogicaOverviewController.showForm(getObject());
		selecionaAnimal(animal);
	}
	
	@FXML
	private void handleExibirEvolucaoRebanho(){
		evolucaoRebanhoOverviewController.showForm();
	}
	
	@FXML
	private void handleExibirProjecaoRebanho(){
		projecaoRebanhoOverviewController.showForm();
	}
	
}
