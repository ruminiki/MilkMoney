package br.com.milkmoney.controller.animal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
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
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.animal.renderer.TableCellOpcoesFactory;
import br.com.milkmoney.controller.arvoreGenealogica.ArvoreGenealogicaOverviewController;
import br.com.milkmoney.controller.cobertura.CoberturaFormController;
import br.com.milkmoney.controller.cobertura.CoberturaOverviewController;
import br.com.milkmoney.controller.confirmacaoPrenhes.ConfirmacaoPrenhesFormController;
import br.com.milkmoney.controller.evolucaoRebanho.EvolucaoRebanhoOverviewController;
import br.com.milkmoney.controller.fichaAnimal.FichaAnimalOverviewController;
import br.com.milkmoney.controller.indicador.IndicadorOverviewController;
import br.com.milkmoney.controller.lactacao.LactacaoOverviewController;
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

@Controller
public class AnimalOverviewController extends AbstractOverviewController<Integer, Animal> {

	@FXML private TableColumn<Animal, String> nomeColumn;
	@FXML private TableColumn<Animal, Date> dataNascimentoColumn;
	@FXML private TableColumn<Raca, String> racaColumn;
	@FXML private TableColumn<Animal, String> numeroColumn;
	@FXML private TableColumn<Animal, String> loteColumn;
	@FXML private TableColumn<String, String> sexoColumn;
	@FXML private TableColumn<Animal, String> situacaoAnimalColumn;
	@FXML private TableColumn<Animal, Long> idadeColumn;
	@FXML private TableColumn<Animal, String> opcoesColumn;
	@FXML private Label lblNumeroServicos, lblDataUltimaCobertura, lblProximoServico, lblNumeroPartos, lblIdadePrimeiroParto, 
						lblIdadePrimeiraCobertura, lblDiasEmLactacao, lblDiasEmAberto, lblIntervaloPrimeiroParto, lblDataSecar,
						lblAnimal, lblDataUltimoParto, lblDataProximoParto, lblSituacaoUltimaCobertura, lblPai, lblMae, 
						lblEmLactacao, lblSecas, lblNaoDefinidas, lblTotalVacas, lblBezerras, lblNovilhas, lblNumeroLactacoes, lblMediaProducao, 
						lblUltimoTratamento, lblLote, lblEficienciaReprodutiva;
	@FXML private Hyperlink hlVisualizarUltimoParto, hlSecarAnimal, hlRegistrarParto, hlEditarCobertura, hlRegistrarCobertura, hlConfirmarPrenhes;
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
	@Autowired private ConfirmacaoPrenhesFormController confirmacaoPrenhesFormController;
	@Autowired private CoberturaOverviewController coberturaOverviewController;
	@Autowired private CoberturaFormController coberturaFormController;
	@Autowired private RacaOverviewController racaController;
	@Autowired private AnimalReducedOverviewController animalReducedController;
	@Autowired private MorteAnimalFormController morteAnimalFormController;
	@Autowired private VendaAnimalFormController vendaAnimalFormController;
	@Autowired private LactacaoOverviewController lactacaoOverviewController;
	@Autowired private FichaAnimalOverviewController fichaAnimalOverviewController;
	@Autowired private ProducaoIndividualOverviewController producaoIndividualOverviewController;
	@Autowired private IndicadorOverviewController indicadorOverviewController;
	@Autowired private PartoFormController partoFormController;
	@Autowired private EvolucaoRebanhoOverviewController evolucaoRebanhoOverviewController;
	@Autowired private ProjecaoOverviewController projecaoRebanhoOverviewController;
	@Autowired private ArvoreGenealogicaOverviewController arvoreGenealogicaOverviewController;
	@Autowired private PainelControleAnimalController painelControleAnimalController;
	
	private FichaAnimal fichaAnimal;
	
	@FXML
	public void initialize() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				situacaoAnimalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("situacaoAnimal"));
				numeroColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
				nomeColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("nome"));
				loteColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("lote"));
				dataNascimentoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataNascimento"));
				idadeColumn.setCellValueFactory(new PropertyValueFactory<Animal,Long>("idade"));
				racaColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("raca"));
				sexoColumn.setCellValueFactory(new PropertyValueFactory<String,String>("sexoFormatado"));
				opcoesColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
				opcoesColumn.setCellFactory(new TableCellOpcoesFactory<Animal,String>(painelControleAnimal, 
																					  novaCobertura, 
																					  confirmarPrenhes,
																					  novoParto,
																					  ultimaCobertura,
																					  ultimoParto,
																					  lactacoes,
																					  controleLeiteiro,
																					  vendaAnimal,
																					  morteAnimal,
																					  linhaTempoAnimal,
																					  exibirFichaAnimal));

				table.widthProperty().addListener((observable, oldValue, newValue) -> resizeColunaTabela(newValue));
				
				//filters
				if ( inputSituacaoAnimal.getItems().size() <= 0 ){
					inputSituacaoAnimal.getItems().setAll(SituacaoAnimal.getItems());
				}
				
				if ( inputSituacaoCobertura.getItems().size() <= 0 ){
					inputSituacaoCobertura.getItems().setAll(SituacaoCobertura.getItems());
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
			}
		});
		
	}
	
	private void resizeColunaTabela(Number newWidth){
		
		double width = newWidth.doubleValue();
		
		if ( width >= 655 ){
			nomeColumn.minWidthProperty().set((width - 420) * 0.50);
			loteColumn.minWidthProperty().set((width - 420) * 0.25);
			racaColumn.minWidthProperty().set((width - 420) * 0.25);
		}
		
	}
	
	@Override
	protected void handleDoubleClick() {
		painelControleAnimalController.setAnimal(getObject());
		painelControleAnimalController.showForm();
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
						lblNumeroServicos.setText(""+fichaAnimal.getNumeroServicosAtePrenhes());
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
						
						hlConfirmarPrenhes.setVisible(fichaAnimal.getDataUltimaCobertura() != null);
						if ( fichaAnimal.getDataUltimaCobertura() != null ){
							if ( fichaAnimal.getSituacaoUltimaCobertura().equals(SituacaoCobertura.NAO_CONFIRMADA) ){
								hlConfirmarPrenhes.setText("confirmar");
							}else{
								if ( fichaAnimal.getSituacaoUltimaCobertura().matches(SituacaoCobertura.PRENHA + "|" + SituacaoCobertura.VAZIA) ){
									hlConfirmarPrenhes.setText("visualizar");
								}else{
									hlConfirmarPrenhes.setVisible(false);
								}
							}
							hlConfirmarPrenhes.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									confirmarPrenhes.apply(getObject());
								}
							});
						}
					}
					lblPai.setText(animal.getPaiEnseminacaoArtificial() != null ? animal.getPaiEnseminacaoArtificial().toString() : ( animal.getPaiMontaNatural() != null ? animal.getPaiMontaNatural().toString() : "--" ));
					lblMae.setText(animal.getMae() != null ? animal.getMae().toString() : "--");
					lblAnimal.setText(animal.toString());
					
					lblEmLactacao.setText( String.valueOf( ((AnimalService)service).countAllFemeasEmLactacao()) );
					lblTotalVacas.setText( String.valueOf( ((AnimalService)service).countAllVacasAtivas()) );
					lblSecas.setText( String.valueOf( ((AnimalService)service).countAllFemeasSecas()) );
					lblBezerras.setText( String.valueOf( ((AnimalService)service).countAllBezerras()) );
					lblNovilhas.setText( String.valueOf( ((AnimalService)service).countAllNovilhas()) );
					
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
				refreshObjectInTableView.apply(service.findById(getObject().getId()));
			}else{
				CustomAlert.mensagemInfo("Somente animais fêmeas podem ter a lactação encerrada.");
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		table.getSelectionModel().select(animal);
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
			}else{
				CustomAlert.mensagemInfo("Somente podem ter registro de produção, animais fêmeas. "
						+ "Por favor, selecione outro animal e tente novamente.");
			}
			table.getSelectionModel().select(animal);
			
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
			relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
					RelatorioService.FICHA_COMPLETA_ANIMAL, params);
			
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		table.getSelectionModel().select(animal);
		return true;
	};
	
	Function<Animal, Boolean> linhaTempoAnimal = animal -> {
		if ( table.getSelectionModel().getSelectedItem() != null ){
			fichaAnimalOverviewController.setAnimal(getObject());
			fichaAnimalOverviewController.showForm();
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		table.getSelectionModel().select(animal);
		return true;
	};
	
	Function<Animal, Boolean> novaCobertura = animal -> {
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
				refreshObjectInTableView.apply(service.findById(getObject().getId()));
			}else{
				CustomAlert.mensagemInfo("Por favor, selecione um animal fêmea, para ter acesso as coberturas. "
						+ "Selecione outro animal e tente novamente.");
			}
			table.getSelectionModel().select(getObject());
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
				setObject(service.findById(getObject().getId()));
				table.getSelectionModel().select(getObject());
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Animal, Boolean> ultimoParto = animal -> {
		if ( animal != null ){
			partoFormController.setObject(partoService.findLastParto(getObject()));
			partoFormController.showForm();
			setObject(service.findById(getObject().getId()));
			table.getSelectionModel().select(getObject());
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Animal, Boolean> confirmarPrenhes = animal -> {
		if ( animal != null ){
			Cobertura cobertura = coberturaService.findLastCoberturaAnimal(getObject());
			if ( cobertura != null ){
				confirmacaoPrenhesFormController.setObject(cobertura);
		    	confirmacaoPrenhesFormController.showForm();
				setObject(service.findById(getObject().getId()));
				table.getSelectionModel().select(getObject());
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Animal, Boolean> novoParto = animal -> {
		if ( animal != null ){
			Cobertura cobertura = coberturaService.findCoberturaAtivaByAnimal(getObject());
			if ( cobertura != null && cobertura.getParto() == null ){
				
				CoberturaValidation.validaRegistroPartoCobertura(cobertura, lactacaoService.findLastBeforeDate(cobertura.getFemea(), cobertura.getData()));
				
				partoFormController.setState(State.CREATE_TO_SELECT);
				partoFormController.setObject(new Parto(cobertura));
				partoFormController.showForm();
				
				if ( partoFormController.getObject() != null && partoFormController.getObject().getLactacao() != null ){
					cobertura.setParto(partoFormController.getObject());
					coberturaService.registrarParto(cobertura);
					setObject(service.findById(getObject().getId()));
					table.getSelectionModel().select(getObject());
				}	
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Animal, Boolean> painelControleAnimal = animal -> {
		if ( animal != null ){
			painelControleAnimalController.setAnimal(getObject());
			painelControleAnimalController.showForm();
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		refreshObjectInTableView.apply(service.findById(getObject().getId()));
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
				refreshObjectInTableView.apply(service.findById(getObject().getId()));
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
				refreshObjectInTableView.apply(service.findById(getObject().getId()));
			}
		}
		return true;
	};
	
	//-------------FILTRO RÁPIDO----------------------------------
	
	@FXML 
	private void handleOpenIndicadores(){
		indicadorOverviewController.showForm();
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
		
		relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.FICHA_ANIMAL, params);
		
	}
	
	@FXML
	private void handleArvoreGenealogica(){
		arvoreGenealogicaOverviewController.showForm(getObject());
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
