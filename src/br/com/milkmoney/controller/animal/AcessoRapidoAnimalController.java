package br.com.milkmoney.controller.animal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.aborto.AbortoFormController;
import br.com.milkmoney.controller.cobertura.CoberturaFormController;
import br.com.milkmoney.controller.cobertura.CoberturaOverviewController;
import br.com.milkmoney.controller.cobertura.renderer.TableCellConfirmarPrenhesHyperlinkFactory;
import br.com.milkmoney.controller.cobertura.renderer.TableCellRegistrarAbortoHyperlinkFactory;
import br.com.milkmoney.controller.cobertura.renderer.TableCellRegistrarPartoHyperlinkFactory;
import br.com.milkmoney.controller.cobertura.renderer.TableCellSituacaoCoberturaFactory;
import br.com.milkmoney.controller.confirmacaoPrenhes.ConfirmacaoPrenhesFormController;
import br.com.milkmoney.controller.fichaAnimal.FichaAnimalOverviewController;
import br.com.milkmoney.controller.indicador.IndicadorOverviewController;
import br.com.milkmoney.controller.lactacao.LactacaoOverviewController;
import br.com.milkmoney.controller.morteAnimal.MorteAnimalFormController;
import br.com.milkmoney.controller.parto.PartoFormController;
import br.com.milkmoney.controller.producaoIndividual.ProducaoIndividualOverviewController;
import br.com.milkmoney.controller.raca.RacaOverviewController;
import br.com.milkmoney.controller.raca.RacaReducedOverviewController;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.controller.touro.TouroReducedOverviewController;
import br.com.milkmoney.controller.vendaAnimal.VendaAnimalFormController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.model.MorteAnimal;
import br.com.milkmoney.model.Parto;
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
import br.com.milkmoney.service.ProcedimentoService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.service.VendaAnimalService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.CoberturaValidation;
import br.com.milkmoney.validation.MorteAnimalValidation;
import br.com.milkmoney.validation.VendaAnimalValidation;

@Controller
public class AcessoRapidoAnimalController extends AbstractOverviewController<Integer, Animal> {

	@FXML private TableView<Cobertura> tableCoberturas;
	@FXML private TableColumn<Cobertura, String> dataCoberturaColumn;
	@FXML private TableColumn<Cobertura, String> situacaoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String> reprodutorColumn;
	@FXML private TableColumn<Cobertura, String> dataPartoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String> dataAbortoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String> dataConfirmacaoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String> previsaoPartoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String> statusColumn;
	
	@FXML private TableView<Parto> tablePartos;
	@FXML private TableColumn<Parto, String> dataPartoColumn;
	@FXML private TableColumn<Parto, String> tipoPartoColumn;
	@FXML private TableColumn<Parto, String> complicacaoPartoColumn;
	
	@FXML private Label lblNumeroServicos, lblDataUltimaCobertura, lblProximoServico, lblNumeroPartos, lblIdadePrimeiroParto, 
	lblIdadePrimeiraCobertura, lblDiasEmLactacao, lblDiasEmAberto, lblIntervaloPrimeiroParto, lblDataSecar,
	lblAnimal, lblDataUltimoParto, lblDataProximoParto, lblSituacaoUltimaCobertura, lblPai, lblMae, 
	lblEmLactacao, lblSecas, lblNaoDefinidas, lblTotalVacas, lblNovilhas, lblNumeroLactacoes, lblMediaProducao, 
	lblUltimoTratamento, lblLote, lblEficienciaReprodutiva;
	
	@FXML private Hyperlink hlVisualizarUltimoParto, hlSecarAnimal, hlRegistrarParto, hlEditarCobertura, hlRegistrarCobertura, hlConfirmarPrenhes;
	
	//services
	@Autowired private MorteAnimalService morteAnimalService;
	@Autowired private VendaAnimalService vendaAnimalService;
	@Autowired private FichaAnimalService fichaAnimalService;
	@Autowired private LactacaoService lactacaoService;
	@Autowired private RelatorioService relatorioService;
	@Autowired private ParametroService parametroService;
	@Autowired private PartoService partoService;
	@Autowired private CoberturaService coberturaService;
	@Autowired private ProcedimentoService procedimentoService;
	@Autowired private AnimalService animalService;
	
	//controllers
	@Autowired private AnimalReducedOverviewController animalReducedOverviewController;
	@Autowired private AnimalFormController animalFormController;
	@Autowired private TouroReducedOverviewController touroReducedOverviewController;
	@Autowired private RacaReducedOverviewController racaReducedOverviewController;
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
	@Autowired private AbortoFormController abortoFormController;
	
	private FichaAnimal fichaAnimal;
	
	private ContextMenu coberturaContextMenu = new ContextMenu();
	MenuItem atualizarCobertura = new MenuItem("Atualizar");
	MenuItem editarCobertura = new MenuItem("Editar");
	MenuItem removerCobertura = new MenuItem("Remover");
	
	private ContextMenu partoContextMenu = new ContextMenu();
	MenuItem atualizarParto = new MenuItem("Atualizar");
	MenuItem visualizarParto = new MenuItem("Visualizar");
	
	@FXML
	public void initialize() {
		
		statusColumn.setCellFactory(new TableCellSituacaoCoberturaFactory<Cobertura,String>("situacaoCobertura"));
		dataCoberturaColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("data"));
		situacaoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("situacaoCobertura"));
		reprodutorColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reprodutor"));
		dataPartoCoberturaColumn.setCellFactory(new TableCellRegistrarPartoHyperlinkFactory<Cobertura,String>("dataParto", registrarParto));
		dataAbortoCoberturaColumn.setCellFactory(new TableCellRegistrarAbortoHyperlinkFactory<Cobertura,String>("dataAborto", registrarAborto));
		dataConfirmacaoCoberturaColumn.setCellFactory(new TableCellConfirmarPrenhesHyperlinkFactory<Cobertura,String>("dataConfirmacaoPrenhes", confirmarPrenhes));
		previsaoPartoCoberturaColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("previsaoParto"));
		
		tableCoberturas.setFixedCellSize(25);
		tableCoberturas.getItems().clear();
		tableCoberturas.getItems().addAll(coberturaService.findByAnimal(getObject()));
		
		tableCoberturas.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					coberturaFormController.setObject(tableCoberturas.getSelectionModel().getSelectedItem());
					coberturaFormController.showForm();
					refreshTableCoberturas();
					handleFichaAnimal();
				}
			}

		});
		
		atualizarCobertura.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	refreshTableCoberturas();
		    }
		});
		
		editarCobertura.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	coberturaFormController.setObject(tableCoberturas.getSelectionModel().getSelectedItem());
				coberturaFormController.showForm();
				refreshTableCoberturas();
				handleFichaAnimal();
		    }
		});
		
		removerCobertura.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	Optional<ButtonType> result = CustomAlert.confirmarExclusao();
				if (result.get() == ButtonType.OK) {
					coberturaService.remove(tableCoberturas.getSelectionModel().getSelectedItem());
					if ( tableCoberturas.getSelectionModel().getSelectedItem().getParto() != null ){
						tablePartos.getItems().clear();
						tablePartos.getItems().addAll(partoService.findByAnimal(getObject()));
					}
					refreshTableCoberturas();
					handleFichaAnimal();
				}
		    }
		});
		
		coberturaContextMenu.getItems().clear();
		coberturaContextMenu.getItems().addAll(atualizarCobertura, editarCobertura, removerCobertura);
		coberturaContextMenu.setPrefWidth(120);
		tableCoberturas.setContextMenu(coberturaContextMenu);
		
		dataPartoColumn.setCellFactory(new TableCellDateFactory<Parto,String>("data"));
		tipoPartoColumn.setCellValueFactory(new PropertyValueFactory<Parto,String>("tipoParto"));
		complicacaoPartoColumn.setCellValueFactory(new PropertyValueFactory<Parto,String>("complicacaoParto"));
		
		tablePartos.setFixedCellSize(25);
		tablePartos.getItems().clear();
		tablePartos.getItems().addAll(partoService.findByAnimal(getObject()));
		
		tablePartos.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					partoFormController.setObject(tablePartos.getSelectionModel().getSelectedItem());
					partoFormController.showForm();
				}
			}

		});
		
		atualizarParto.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
				tablePartos.getItems().clear();
				tablePartos.getItems().addAll(partoService.findByAnimal(getObject()));
		    }
		});
		
		visualizarParto.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	partoFormController.setObject(tablePartos.getSelectionModel().getSelectedItem());
				partoFormController.showForm();
		    }
		});
		
		partoContextMenu.getItems().clear();
		partoContextMenu.getItems().addAll(atualizarParto, visualizarParto);
		partoContextMenu.setPrefWidth(120);
		tablePartos.setContextMenu(partoContextMenu);
		
		handleFichaAnimal();
	}
	
	private void refreshTableCoberturas(){
		tableCoberturas.getItems().clear();
    	tableCoberturas.getItems().addAll(coberturaService.findByAnimal(getObject()));
	}
	
	private void handleFichaAnimal(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if ( getObject() != null ){
					
					fichaAnimal = fichaAnimalService.generateFichaAnimal(getObject(), fichaAnimalService.getAllFields());
					
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
									partoFormController.setObject(partoService.findLastParto(getObject()));
									partoFormController.showForm();
								}
							});
						}
						
						hlSecarAnimal.setVisible(fichaAnimal.getDataPrevisaoEncerramentoLactacao() != null);
						if (fichaAnimal.getDataPrevisaoEncerramentoLactacao() != null){
							hlSecarAnimal.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									handleEncerrarLactacao();
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
										
										if ( partoFormController.getObject() != null && partoFormController.getObject().getLactacao() != null ){
											cobertura.setParto(partoFormController.getObject());
											coberturaService.registrarParto(cobertura);
											tablePartos.getItems().add(partoFormController.getObject());
											refreshTableCoberturas();
											handleFichaAnimal();
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
										refreshTableCoberturas();
										handleFichaAnimal();
									}
								}
							});
						}
						
						hlRegistrarCobertura.setVisible(fichaAnimal.getProximoServico() != null);
						if ( fichaAnimal.getProximoServico() != null ){
							hlRegistrarCobertura.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									registrarCoberturaAnimal();
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
								    	refreshTableCoberturas();
								    	handleFichaAnimal();
									}
								}
							});
						}
					}
					lblPai.setText(getObject().getPaiEnseminacaoArtificial() != null ? getObject().getPaiEnseminacaoArtificial().toString() : ( getObject().getPaiMontaNatural() != null ? getObject().getPaiMontaNatural().toString() : "--" ));
					lblMae.setText(getObject().getMae() != null ? getObject().getMae().toString() : "--");
					lblAnimal.setText(getObject().toString());
				}
			}
		});
	}
	
	Function<Integer, Boolean> registrarParto = index -> {
		tableCoberturas.getSelectionModel().select(index);
		
		if ( tableCoberturas.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return false;
		}
		
		Cobertura cobertura = tableCoberturas.getSelectionModel().getSelectedItem();
		
		if ( cobertura.getParto() == null ){
			
			CoberturaValidation.validaRegistroPartoCobertura(cobertura, lactacaoService.findLastBeforeDate(cobertura.getFemea(), cobertura.getData()));
			
			partoFormController.setState(State.CREATE_TO_SELECT);
			partoFormController.setObject(new Parto(cobertura));
			partoFormController.showForm();
			
			if ( partoFormController.getObject() != null && partoFormController.getObject().getLactacao() != null ){
				cobertura.setParto(partoFormController.getObject());
				coberturaService.registrarParto(cobertura);
				tablePartos.getItems().add(cobertura.getParto());
			}	
			
		}else{
			partoFormController.setObject(cobertura.getParto());
			partoFormController.showForm();
		}
		
		refreshTableCoberturas();
		handleFichaAnimal();
		
		return true;
	};
	
	Function<Integer, Boolean> registrarAborto = index -> {
		tableCoberturas.getSelectionModel().select(index);
		if ( tableCoberturas.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return false;
		}
		
		Cobertura cobertura = tableCoberturas.getSelectionModel().getSelectedItem();
		
		abortoFormController.setObject(cobertura);
		abortoFormController.showForm();
		
		refreshTableCoberturas();
		handleFichaAnimal();
		
		return true;
	};
	
	Function<Integer, Boolean> confirmarPrenhes = index -> {
		tableCoberturas.getSelectionModel().select(index);
		if ( tableCoberturas.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return false;
		}
		
		Cobertura cobertura = tableCoberturas.getSelectionModel().getSelectedItem();
		
		confirmacaoPrenhesFormController.setObject(cobertura);
    	confirmacaoPrenhesFormController.showForm();
    	
		refreshTableCoberturas();
		handleFichaAnimal();
    	
		return true;
	};
	
	@FXML
	private void handleEncerrarLactacao() {
		if ( getObject().getSexo().equals(Sexo.FEMEA) ){
			lactacaoOverviewController.setAnimal(getObject());
			lactacaoOverviewController.showForm();
			
			handleFichaAnimal();
			
		}else{
			CustomAlert.mensagemInfo("Somente animais fêmeas podem ter a lactação encerrada.");
		}
	}
	
	@FXML
	private void registrarCoberturaAnimal() {
		if ( getObject().getSexo().equals(Sexo.FEMEA) ){
			coberturaFormController.setObject(new Cobertura(getObject(), fichaAnimal.getProximoServico() != null ? fichaAnimal.getProximoServico() : new Date() ));
			coberturaFormController.showForm();
			if ( coberturaFormController.getObject() != null && coberturaFormController.getObject().getId() > 0 ){
				tableCoberturas.getItems().add(coberturaFormController.getObject());
				handleFichaAnimal();
			}
		}else{
			CustomAlert.mensagemInfo("Por favor, selecione um animal fêmea, para ter acesso as coberturas. "
					+ "Selecione outro animal e tente novamente.");
		}
	}
	
	@FXML
	private void registrarProducaoAnimal() {
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
	}
	
	@FXML
	private void exibirFichaAnimal() {
		Object[] params = new Object[]{
				getObject().getId()
		};
		relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.FICHA_COMPLETA_ANIMAL, params);
			
	}
	
	@FXML
	private void registrarDesfazerRegistroMorte() {
		if ( getObject().getSituacaoAnimal().equals(SituacaoAnimal.MORTO) ){
			
			Optional<ButtonType> result = CustomAlert.confirmar("Desfazer Registro Morte", "Tem certeza que deseja desfazer o registro de morte do animal?");
			if (result.get() == ButtonType.OK) {
				morteAnimalService.removeByAnimal(getObject());
			}
			
		}else{
			
			MorteAnimalValidation.validaSituacaoAnimal(getObject());
			
			morteAnimalFormController.setObject(new MorteAnimal(getObject()));
			morteAnimalFormController.showForm();
			
		}
	}
	
	@FXML
	private void registrarDesfazerRegistroVenda() {
		if ( getObject().getSituacaoAnimal().equals(SituacaoAnimal.VENDIDO) ){
			Optional<ButtonType> result = CustomAlert.confirmar("Desfazer Registro Venda", "Tem certeza que deseja desfazer o registro de venda do animal?");
			if (result.get() == ButtonType.OK) {
				vendaAnimalService.removeByAnimal(getObject());
			}
		}else{
			VendaAnimalValidation.validaSituacaoAnimal(getObject());
			vendaAnimalFormController.setAnimalVendido(getObject());
			vendaAnimalFormController.setObject(new VendaAnimal());
			vendaAnimalFormController.showForm();
		}
	}
	
	@FXML
	private void atualizarCadastroAnimal() {
		animalFormController.setObject(getObject());
		animalFormController.showForm();
	}
	
	@FXML
	private void imprimirCoberturas(){
		Object[] params = new Object[]{
				getObject().getId()
		};
		relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.IMPRIMIR_COBERTURAS_ANIMAL, params);
		
	}
	
	@FXML@Override
	protected void closeForm(){
		if ( tableCoberturas != null ){
			((Stage)tableCoberturas.getScene().getWindow()).close();	
		}
	}

	public void setNumeroDigitado(String numeroAnimal) {
		setObject(animalService.findByNumero(numeroAnimal));
		if ( getObject() == null ){
			CustomAlert.mensagemInfo("Nenhum animal encontrado com o número [" + numeroAnimal + "]. Por favor, tente selecionar o animal novamente.");
		}
	}

	@Override
	public String getFormName() {
		return "view/animal/AcessoRapidoAnimal.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Animal";
	}
	
	@Override
	@Resource(name = "animalService")
	protected void setService(IService<Integer, Animal> service) {
		super.setService(service);
	}

	
}
