package br.com.milkmoney.controller.animal;

import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ButtonType;
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
import br.com.milkmoney.controller.lactacao.LactacaoOverviewController;
import br.com.milkmoney.controller.morteAnimal.MorteAnimalFormController;
import br.com.milkmoney.controller.parto.PartoFormController;
import br.com.milkmoney.controller.producaoIndividual.ProducaoIndividualOverviewController;
import br.com.milkmoney.controller.raca.RacaOverviewController;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.controller.vendaAnimal.VendaAnimalFormController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.ConfirmacaoPrenhes;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.model.MorteAnimal;
import br.com.milkmoney.model.Parametro;
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
import br.com.milkmoney.service.searchers.Search;
import br.com.milkmoney.service.searchers.SearchAnimaisDisponiveisParaCobertura;
import br.com.milkmoney.service.searchers.SearchAnimaisMortos;
import br.com.milkmoney.service.searchers.SearchAnimaisVendidos;
import br.com.milkmoney.service.searchers.SearchFemeas30DiasLactacao;
import br.com.milkmoney.service.searchers.SearchFemeas60DiasLactacao;
import br.com.milkmoney.service.searchers.SearchFemeasASecar;
import br.com.milkmoney.service.searchers.SearchFemeasAtivas;
import br.com.milkmoney.service.searchers.SearchFemeasCobertas;
import br.com.milkmoney.service.searchers.SearchFemeasEmLactacao;
import br.com.milkmoney.service.searchers.SearchFemeasEmPeriodoVoluntarioEspera;
import br.com.milkmoney.service.searchers.SearchFemeasMais60DiasLactacao;
import br.com.milkmoney.service.searchers.SearchFemeasNaoCobertas;
import br.com.milkmoney.service.searchers.SearchFemeasNaoPrenhasAposXDiasAposParto;
import br.com.milkmoney.service.searchers.SearchFemeasNaoPrenhasXDiasAposParto;
import br.com.milkmoney.service.searchers.SearchFemeasSecas;
import br.com.milkmoney.service.searchers.SearchMachos;
import br.com.milkmoney.service.searchers.SearchReprodutoresAtivos;
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
						lblAnimal, lblDataUltimoParto, lblDataProximoParto, lblSituacaoUltimaCobertura;
	@FXML private Hyperlink hlVisualizarUltimoParto, hlSecarAnimal, hlRegistrarParto, hlEditarCobertura, hlRegistrarCobertura, hlConfirmarPrenhes;
	@FXML private VBox vBoxChart, sideBar;
	
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
	@Autowired private PartoFormController partoFormController;
	
	private PieChart chart;
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
		
		chart = new PieChart(((AnimalService)service).getChartData());
	    chart.setTitle("Situação Animais Rebanho");
        vBoxChart.getChildren().add(chart);
       
	}
	
	@Override
	protected void selectRowTableHandler(Animal animal) {
		super.selectRowTableHandler(animal);
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
				
				//links
				hlVisualizarUltimoParto.setVisible(fichaAnimal.getDataUltimoParto() != null);
				if (fichaAnimal.getDataUltimoParto() != null){
					hlVisualizarUltimoParto.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							partoFormController.setObject(partoService.findLastParto(getObject()));
							partoFormController.showForm();
							setObject(service.findById(getObject().getId()));
							table.getSelectionModel().select(getObject());
						}
					});
				}
				
				hlSecarAnimal.setVisible(fichaAnimal.getDataPrevisaoEncerramentoLactacao() != null);
				if (fichaAnimal.getDataPrevisaoEncerramentoLactacao() != null){
					hlSecarAnimal.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							encerrarLactacaoAnimal.apply(table.getItems().indexOf(getObject()));
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
							coberturaFormController.setObject(new Cobertura(getObject()));
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
						hlConfirmarPrenhes.setText("visualizar");
					}
					hlConfirmarPrenhes.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							Cobertura cobertura = coberturaService.findLastCoberturaAnimal(getObject());
							if ( cobertura != null ){
								confirmacaoPrenhesFormController.setState(State.CREATE_TO_SELECT);
								confirmacaoPrenhesFormController.setObject(new ConfirmacaoPrenhes(cobertura));
						    	confirmacaoPrenhesFormController.showForm();
						    	
						    	if ( !cobertura.getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
						    		coberturaService.saveConfirmacaoPrenhes(cobertura);
						    		setObject(service.findById(getObject().getId()));
						    		table.getSelectionModel().select(getObject());
						    	}
							}
						}
					});
				}
			}
			
			lblAnimal.setText(animal.toString());
			
		}
		
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
				table.getSelectionModel().select(index);
				
			}else{
				CustomAlert.mensagemInfo("Somente animais fêmeas podem ter a lactação encerrada.");
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Integer, Boolean> registrarCoberturaAnimal = index -> {
		if ( table.getSelectionModel().getSelectedItem() != null ){
			if ( getObject().getSexo().equals(Sexo.FEMEA) ){
				coberturaOverviewController.setObject(new Cobertura(getObject()));
				coberturaOverviewController.setFemea(getObject());
				//se o animal tiver morto ou vendido apenas consulta as coberturas
				if ( getObject().getSituacaoAnimal().matches(SituacaoAnimal.MORTO + "|" + SituacaoAnimal.VENDIDO) ){
					coberturaOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, true);
					coberturaOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, true);
					coberturaOverviewController.getFormConfig().put(AbstractOverviewController.REMOVE_DISABLED, true);
				}
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
				if ( getObject().getSituacaoAnimal().matches(SituacaoAnimal.MORTO + "|" + SituacaoAnimal.VENDIDO) ){
					producaoIndividualOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, true);
					producaoIndividualOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, true);
					producaoIndividualOverviewController.getFormConfig().put(AbstractOverviewController.REMOVE_DISABLED, true);
				}
				
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
					RelatorioService.FICHA_COMPLETA_ANIMAL, table.getScene().getCursor(), params);
			
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
	
	@FXML
	private void handleFindFemeas(){
		doSearchAnimais((SearchFemeasAtivas)MainApp.getBean(SearchFemeasAtivas.class));
	}
	
	@FXML
	private void handleFindMachos(){
		doSearchAnimais((SearchMachos)MainApp.getBean(SearchMachos.class));
	}
	
	@FXML
	private void handleFindReprodutores(){
		doSearchAnimais((SearchReprodutoresAtivos)MainApp.getBean(SearchReprodutoresAtivos.class));
	}
	
	@FXML
	private void handleFindFemeasEmLactacao(){
		doSearchAnimais((SearchFemeasEmLactacao)MainApp.getBean(SearchFemeasEmLactacao.class));
	}
	
	
	@FXML
	private void handleFindFemeasCobertas(){
		doSearchAnimais((SearchFemeasCobertas)MainApp.getBean(SearchFemeasCobertas.class));
	}
	
	@FXML
	private void handleFindFemeasNaoCobertas(){
		doSearchAnimais((SearchFemeasNaoCobertas)MainApp.getBean(SearchFemeasNaoCobertas.class));
	}
	
	@FXML
	private void handleFindFemeas30DiasLactacao(){
		doSearchAnimais((SearchFemeas30DiasLactacao)MainApp.getBean(SearchFemeas30DiasLactacao.class));
	}
	
	@FXML
	private void handleFindFemeas60DiasLactacao(){
		doSearchAnimais((SearchFemeas60DiasLactacao)MainApp.getBean(SearchFemeas60DiasLactacao.class));
	}
	
	@FXML
	private void handleFindFemeasMais60DiasLactacao(){
		doSearchAnimais((SearchFemeasMais60DiasLactacao)MainApp.getBean(SearchFemeasMais60DiasLactacao.class));
	}
	
	@FXML
	private void handleFindFemeasASecar(){
		doSearchAnimais((SearchFemeasASecar)MainApp.getBean(SearchFemeasASecar.class));
	}
	
	@FXML
	private void handleFindFemeasSecas(){
		doSearchAnimais((SearchFemeasSecas)MainApp.getBean(SearchFemeasSecas.class));
	}
	
	@FXML
	private void handleFindAnimaisVendidos(){
		doSearchAnimais((SearchAnimaisVendidos)MainApp.getBean(SearchAnimaisVendidos.class));
	}
	
	@FXML
	private void handleFindAnimaisMortos(){
		doSearchAnimais((SearchAnimaisMortos)MainApp.getBean(SearchAnimaisMortos.class));
	}
	
	//------FILTROS COBERTURA--
	@FXML
	private void handleBuscarFemeasDisponeisParaCobertura(){
		doSearchAnimais((SearchAnimaisDisponiveisParaCobertura) MainApp.getBean(SearchAnimaisDisponiveisParaCobertura.class));
	}
	
	@FXML
	private void handleBuscarEmPeriodoVolutarioDeEspera(){
		doSearchAnimais((SearchFemeasEmPeriodoVoluntarioEspera) MainApp.getBean(SearchFemeasEmPeriodoVoluntarioEspera.class));
	}
	
	@FXML
	private void handleBuscarNaoPrenhasPrimeiroCiclo(){
		int periodoVoluntarioEspera = Integer.parseInt(parametroService.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA));
		doSearchAnimais((SearchFemeasNaoPrenhasXDiasAposParto) MainApp.getBean(SearchFemeasNaoPrenhasXDiasAposParto.class),periodoVoluntarioEspera + 21);
	}
	
	@FXML
	private void handleBuscarNaoPrenhasSegundoCiclo(){
		int periodoVoluntarioEspera = Integer.parseInt(parametroService.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA));
		doSearchAnimais((SearchFemeasNaoPrenhasXDiasAposParto) MainApp.getBean(SearchFemeasNaoPrenhasXDiasAposParto.class),periodoVoluntarioEspera + 42);
	}
	
	@FXML
	private void handleBuscarNaoPrenhasTerceiroCiclo(){
		int periodoVoluntarioEspera = Integer.parseInt(parametroService.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA));
		doSearchAnimais((SearchFemeasNaoPrenhasXDiasAposParto) MainApp.getBean(SearchFemeasNaoPrenhasXDiasAposParto.class),periodoVoluntarioEspera + 63);
	}
	
	@FXML
	private void handleBuscarNaoPrenhasAposTerceiroCiclo(){
		int periodoVoluntarioEspera = Integer.parseInt(parametroService.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA));
		int primeiroCiclo = periodoVoluntarioEspera + 63;
		doSearchAnimais((SearchFemeasNaoPrenhasAposXDiasAposParto) MainApp.getBean(SearchFemeasNaoPrenhasAposXDiasAposParto.class),primeiroCiclo);
	}
	
	private void doSearchAnimais(Search<Integer, Animal> search, Object ...params){
		setSearch(search, params);
		refreshTableOverview();
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
