package br.com.milkmoney.controller.animal;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.animal.renderer.TableCellOpcoesFactory;
import br.com.milkmoney.controller.cobertura.CoberturaFormController;
import br.com.milkmoney.controller.cobertura.CoberturaOverviewController;
import br.com.milkmoney.controller.confirmacaoPrenhes.ConfirmacaoPrenhesFormController;
import br.com.milkmoney.controller.fichaAnimal.FichaAnimalOverviewController;
import br.com.milkmoney.controller.indicador.IndicadorOverviewController;
import br.com.milkmoney.controller.lactacao.LactacaoOverviewController;
import br.com.milkmoney.controller.morteAnimal.MorteAnimalFormController;
import br.com.milkmoney.controller.parto.PartoFormController;
import br.com.milkmoney.controller.producaoIndividual.ProducaoIndividualOverviewController;
import br.com.milkmoney.controller.raca.RacaOverviewController;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.controller.vendaAnimal.VendaAnimalFormController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.model.MorteAnimal;
import br.com.milkmoney.model.OptionChoiceFilter;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.Raca;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.model.SituacaoAnimal;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.model.State;
import br.com.milkmoney.model.VendaAnimal;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.FichaAnimalService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.LactacaoService;
import br.com.milkmoney.service.MorteAnimalService;
import br.com.milkmoney.service.ParametroService;
import br.com.milkmoney.service.PartoService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.service.VendaAnimalService;
import br.com.milkmoney.service.searchers.SearchAnimaisDisponiveisParaCobertura;
import br.com.milkmoney.service.searchers.SearchAnimaisMortos;
import br.com.milkmoney.service.searchers.SearchAnimaisVendidos;
import br.com.milkmoney.service.searchers.SearchFemeasASecar;
import br.com.milkmoney.service.searchers.SearchFemeasAtivas;
import br.com.milkmoney.service.searchers.SearchFemeasCobertas;
import br.com.milkmoney.service.searchers.SearchFemeasCobertasNaoConfirmadas;
import br.com.milkmoney.service.searchers.SearchFemeasEmLactacao;
import br.com.milkmoney.service.searchers.SearchFemeasEmPeriodoVoluntarioEspera;
import br.com.milkmoney.service.searchers.SearchFemeasNaoCobertas;
import br.com.milkmoney.service.searchers.SearchFemeasNaoPrenhasAposXDiasAposParto;
import br.com.milkmoney.service.searchers.SearchFemeasNaoPrenhasXDiasAposParto;
import br.com.milkmoney.service.searchers.SearchFemeasSecas;
import br.com.milkmoney.service.searchers.SearchMachos;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.CoberturaValidation;
import br.com.milkmoney.validation.MorteAnimalValidation;
import br.com.milkmoney.validation.VendaAnimalValidation;

@Controller
public class AnimalOverviewController extends AbstractOverviewController<Integer, Animal> {

	@FXML private TableColumn<Animal, String> nomeColumn;
	@FXML private TableColumn<Animal, String> numeroColumn;
	@FXML private TableColumn<Animal, Date> dataNascimentoColumn;
	@FXML private TableColumn<Raca, String> racaColumn;
	@FXML private TableColumn<String, String> sexoColumn;
	@FXML private TableColumn<Animal, String> situacaoAnimalColumn;
	@FXML private TableColumn<Animal, Long> idadeColumn;
	@FXML private TableColumn<Animal, String> opcoesColumn;
	@FXML private Label lblNumeroServicos, lblDataUltimaCobertura, lblProximoServico, lblNumeroPartos, lblIdadePrimeiroParto, 
						lblIdadePrimeiraCobertura, lblDiasEmLactacao, lblDiasEmAberto, lblIntervaloPrimeiroParto, lblDataSecar,
						lblAnimal, lblDataUltimoParto, lblDataProximoParto, lblSituacaoUltimaCobertura, lblPai, lblMae, 
						lblEmLactacao, lblSecas, lblNaoDefinidas, lblTotalVacas, lblNovilhas, lblNumeroLactacoes, lblMediaProducao, 
						lblUltimoTratamento, lblLote, lblEficienciaReprodutiva;
	@FXML private Hyperlink hlVisualizarUltimoParto, hlSecarAnimal, hlRegistrarParto, hlEditarCobertura, hlRegistrarCobertura, hlConfirmarPrenhes;
	@FXML private VBox sideBar;
	
	@FXML ChoiceBox<OptionChoiceFilter> choiceFilter;
	
	//services
	@Autowired private MorteAnimalService morteAnimalService;
	@Autowired private VendaAnimalService vendaAnimalService;
	@Autowired private FichaAnimalService fichaAnimalService;
	@Autowired private LactacaoService lactacaoService;
	@Autowired private RelatorioService relatorioService;
	@Autowired private ParametroService parametroService;
	@Autowired private PartoService partoService;
	@Autowired private CoberturaService coberturaService;
	
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
	
	private FichaAnimal fichaAnimal;
	
	@FXML
	public void initialize() {
		
		situacaoAnimalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("situacaoAnimal"));
		nomeColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("nome"));
		numeroColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
		dataNascimentoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataNascimento"));
		idadeColumn.setCellValueFactory(new PropertyValueFactory<Animal,Long>("idade"));
		racaColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("raca"));
		sexoColumn.setCellValueFactory(new PropertyValueFactory<String,String>("sexoFormatado"));
		opcoesColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
		opcoesColumn.setCellFactory(new TableCellOpcoesFactory<Animal,String>(registrarCoberturaAnimal, encerrarLactacaoAnimal, 
																			  registrarDesfazerRegistroVenda, registrarDesfazerRegistroMorte,
																			  registrarProducaoAnimal, exibirFichaAnimal, linhaTempoAnimal));
		
		super.initialize((AnimalFormController)MainApp.getBean(AnimalFormController.class));
		
		if ( table.getItems().size() > 0 )
			table.getSelectionModel().select(0);
		
		if ( choiceFilter.getItems().size() <= 0 ){
			choiceFilter.getItems().addAll(Arrays.asList(new OptionChoiceFilter[] {
					new OptionChoiceFilter("Todos", null),
					new OptionChoiceFilter("Fêmeas", (SearchFemeasAtivas)MainApp.getBean(SearchFemeasAtivas.class)), 
					new OptionChoiceFilter("Machos", (SearchMachos)MainApp.getBean(SearchMachos.class)),
					new OptionChoiceFilter("Em lactação", (SearchFemeasEmLactacao)MainApp.getBean(SearchFemeasEmLactacao.class)),
					new OptionChoiceFilter("Secas", (SearchFemeasSecas)MainApp.getBean(SearchFemeasSecas.class)),
					new OptionChoiceFilter("Vendidas", (SearchAnimaisVendidos)MainApp.getBean(SearchAnimaisVendidos.class)),
					new OptionChoiceFilter("Mortas", (SearchAnimaisMortos)MainApp.getBean(SearchAnimaisMortos.class)),
					new OptionChoiceFilter("Cobertas", (SearchFemeasCobertas)MainApp.getBean(SearchFemeasCobertas.class)),
					new OptionChoiceFilter("Cobertas Não Confirmadas", (SearchFemeasCobertasNaoConfirmadas)MainApp.getBean(SearchFemeasCobertasNaoConfirmadas.class)),
					new OptionChoiceFilter("Não cobertas", (SearchFemeasNaoCobertas)MainApp.getBean(SearchFemeasNaoCobertas.class)),
					new OptionChoiceFilter("A secar", (SearchFemeasASecar)MainApp.getBean(SearchFemeasASecar.class)),
					new OptionChoiceFilter("Disponíveis para cobrir", (SearchAnimaisDisponiveisParaCobertura)MainApp.getBean(SearchAnimaisDisponiveisParaCobertura.class)),
					new OptionChoiceFilter("Em Período Voluntário de Espera (PVE)", (SearchFemeasEmPeriodoVoluntarioEspera)MainApp.getBean(SearchFemeasEmPeriodoVoluntarioEspera.class)),
					new OptionChoiceFilter("Não cobertas até 40 dias após parto", (SearchFemeasNaoPrenhasXDiasAposParto)MainApp.getBean(SearchFemeasNaoPrenhasXDiasAposParto.class), new Object[]{40}),
					new OptionChoiceFilter("Não cobertas até 60 dias após parto", (SearchFemeasNaoPrenhasXDiasAposParto)MainApp.getBean(SearchFemeasNaoPrenhasXDiasAposParto.class), new Object[]{60}),
					new OptionChoiceFilter("Não cobertas até 85 dias após parto", (SearchFemeasNaoPrenhasXDiasAposParto)MainApp.getBean(SearchFemeasNaoPrenhasXDiasAposParto.class), new Object[]{85}),
					new OptionChoiceFilter("Não cobertas + 85 dias após parto", (SearchFemeasNaoPrenhasAposXDiasAposParto)MainApp.getBean(SearchFemeasNaoPrenhasAposXDiasAposParto.class), new Object[]{85})
			}));
		}
		choiceFilter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OptionChoiceFilter>() {
			@Override
			public void changed(ObservableValue<? extends OptionChoiceFilter> observable,	OptionChoiceFilter oldValue, OptionChoiceFilter newValue) {
				doSearch(newValue);
			}
		});
		
	}
	
	@Override
	public void clearFilter() {
		if ( inputPesquisa != null ){
			inputPesquisa.clear();
		}
		setSearch(null);
		if ( choiceFilter.getSelectionModel().getSelectedIndex() == 0 ){
			refreshTableOverview();
		}else{
			choiceFilter.getSelectionModel().clearAndSelect(0);			
		}
	}
	
	@Override
	protected void selectRowTableHandler(Animal animal) {
		super.selectRowTableHandler(animal);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if ( animal != null ){
					
					fichaAnimal = fichaAnimalService.generateFichaAnimal(animal);
					
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
									if ( table.getSelectionModel().getSelectedItem() != null ){
										partoFormController.setObject(partoService.findLastParto(getObject()));
										partoFormController.showForm();
										setObject(service.findById(getObject().getId()));
										table.getSelectionModel().select(getObject());
									}else{
										CustomAlert.nenhumRegistroSelecionado();
									}
								}
							});
						}
						
						hlSecarAnimal.setVisible(fichaAnimal.getDataPrevisaoEncerramentoLactacao() != null);
						if (fichaAnimal.getDataPrevisaoEncerramentoLactacao() != null){
							hlSecarAnimal.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									int index = 0;
									for ( Animal animal : table.getItems() ){
										if ( animal.getId() == getObject().getId() ){
											encerrarLactacaoAnimal.apply(index);
											break;
										}
									}
								}
							});
						}
						
						hlRegistrarParto.setVisible(fichaAnimal.getDataProximoParto() != null);
						if (fichaAnimal.getDataProximoParto() != null){
							hlRegistrarParto.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									Cobertura cobertura = coberturaService.findCoberturaAtivaByAnimal(getObject());
									if ( cobertura != null && cobertura.getParto() == null ){
										
										CoberturaValidation.validaRegistroPartoCobertura(cobertura, lactacaoService.findLastBeforeDate(cobertura.getFemea(), cobertura.getData()));
										
										partoFormController.setState(State.CREATE_TO_SELECT);
										partoFormController.setObject(new Parto(cobertura));
										partoFormController.showForm();
										
										if ( partoFormController.getObject() != null ){
											cobertura.setParto(partoFormController.getObject());
											coberturaService.registrarParto(cobertura);
											setObject(service.findById(getObject().getId()));
											table.getSelectionModel().select(getObject());
										}	
									}
								}
							});
						}
						
						hlEditarCobertura.setVisible(fichaAnimal.getDataUltimaCobertura() != null);
						if ( fichaAnimal.getDataUltimaCobertura() != null ){
							hlEditarCobertura.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									Cobertura cobertura = coberturaService.findLastCoberturaAnimal(getObject());
									if ( cobertura != null ){
										coberturaFormController.setObject(cobertura);
										coberturaFormController.showForm();
										setObject(service.findById(getObject().getId()));
										table.getSelectionModel().select(getObject());
									}
								}
							});
						}
						
						hlRegistrarCobertura.setVisible(fichaAnimal.getProximoServico() != null);
						if ( fichaAnimal.getProximoServico() != null ){
							hlRegistrarCobertura.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									coberturaFormController.setObject(new Cobertura(getObject(), fichaAnimal.getProximoServico()));
									coberturaFormController.showForm();
									setObject(service.findById(getObject().getId()));
									table.getSelectionModel().select(getObject());
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
									Cobertura cobertura = coberturaService.findLastCoberturaAnimal(getObject());
									if ( cobertura != null ){
										confirmacaoPrenhesFormController.setObject(cobertura);
								    	confirmacaoPrenhesFormController.showForm();
							    		setObject(service.findById(getObject().getId()));
							    		table.getSelectionModel().select(getObject());
									}
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
					lblNovilhas.setText( String.valueOf( ((AnimalService)service).countAllNovilhasAtivas()) );
					
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
	
	Function<Integer, Boolean> encerrarLactacaoAnimal = index -> {
		if ( table.getSelectionModel().getSelectedItem() != null ){
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
		table.getSelectionModel().select(index);
		return true;
	};
	
	Function<Integer, Boolean> registrarCoberturaAnimal = index -> {
		if ( table.getSelectionModel().getSelectedItem() != null ){
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
			table.getSelectionModel().select(index);
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Integer, Boolean> registrarProducaoAnimal = index -> {
		if ( table.getSelectionModel().getSelectedItem() != null ){
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
			table.getSelectionModel().select(index);
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Integer, Boolean> exibirFichaAnimal = index -> {
		if ( table.getSelectionModel().getSelectedItem() != null ){
			Object[] params = new Object[]{
					table.getSelectionModel().getSelectedItem().getId()
			};
			relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
					RelatorioService.FICHA_COMPLETA_ANIMAL, params);
			
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		table.getSelectionModel().select(index);
		return true;
	};
	
	Function<Integer, Boolean> linhaTempoAnimal = index -> {
		if ( table.getSelectionModel().getSelectedItem() != null ){
			fichaAnimalOverviewController.setAnimal(getObject());
			fichaAnimalOverviewController.showForm();
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		table.getSelectionModel().select(index);
		return true;
	};
	
	Function<Integer, Boolean> registrarDesfazerRegistroMorte = index -> {
		if ( table.getSelectionModel().getSelectedItem() != null ){
			
			if ( getObject().getSituacaoAnimal().equals(SituacaoAnimal.MORTO) ){
				
				Optional<ButtonType> result = CustomAlert.confirmar("Desfazer Registro Morte", "Tem certeza que deseja desfazer o registro de morte do animal?");
				if (result.get() == ButtonType.OK) {
					morteAnimalService.removeByAnimal(getObject());
					refreshObjectInTableView.apply(service.findById(getObject().getId()));
				}
				
			}else{
				
				MorteAnimalValidation.validaSituacaoAnimal(getObject());
				
				morteAnimalFormController.setObject(new MorteAnimal(getObject()));
				morteAnimalFormController.showForm();
				if ( morteAnimalFormController.getObject() != null && morteAnimalFormController.getObject().getId() > 0 ){
					getObject().setSituacaoAnimal(SituacaoAnimal.MORTO);
					refreshObjectInTableView.apply(service.findById(getObject().getId()));
				}
				
			}
			table.getSelectionModel().select(index);
			
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Integer, Boolean> registrarDesfazerRegistroVenda = index -> {
		if ( table.getSelectionModel().getSelectedItem() != null ){
			
			if ( getObject().getSituacaoAnimal().equals(SituacaoAnimal.VENDIDO) ){
				Optional<ButtonType> result = CustomAlert.confirmar("Desfazer Registro Venda", "Tem certeza que deseja desfazer o registro de venda do animal?");
				if (result.get() == ButtonType.OK) {
					vendaAnimalService.removeByAnimal(getObject());
					refreshObjectInTableView.apply(service.findById(getObject().getId()));
				}
			}else{
				VendaAnimalValidation.validaSituacaoAnimal(getObject());
				vendaAnimalFormController.setAnimalVendido(getObject());
				vendaAnimalFormController.setObject(new VendaAnimal());
				vendaAnimalFormController.showForm();
				if ( vendaAnimalFormController.getObject() != null && vendaAnimalFormController.getObject().getId() > 0 ){
					getObject().setSituacaoAnimal(SituacaoAnimal.VENDIDO);
					refreshObjectInTableView.apply(service.findById(getObject().getId()));
				}
			}
			table.getSelectionModel().select(index);
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	//-------------FILTRO RÁPIDO----------------------------------
	
	@SuppressWarnings("unchecked")
	private void doSearch(OptionChoiceFilter filter){
		setSearch(filter.getSearch(), filter.getParams());
		refreshTableOverview();
	}
	
	@FXML 
	private void handleOpenIndicadores(){
		indicadorOverviewController.showForm();
	}
	
	//------ANÁLISE REPORT----
	@FXML
	private void gerarRelatorioAnaliseReprodutiva(){
		//os ids dos animais selecionados são passados como parâmetro
		StringBuilder sb = new StringBuilder();
		
		for ( Animal animal :table.getItems() ){
			sb.append(animal.getId());
			sb.append(",");
		}
		
		sb.replace(sb.length(), sb.length(), "");
		
		relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.FICHA_ANIMAL, sb.toString());
		
	}
	
}
