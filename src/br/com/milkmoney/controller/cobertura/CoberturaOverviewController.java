package br.com.milkmoney.controller.cobertura;

import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.animal.AnimalFormController;
import br.com.milkmoney.controller.cobertura.renderer.TableCellConfirmarPrenhesHyperlinkFactory;
import br.com.milkmoney.controller.cobertura.renderer.TableCellRegistrarPartoHyperlinkFactory;
import br.com.milkmoney.controller.cobertura.renderer.TableCellSituacaoCoberturaFactory;
import br.com.milkmoney.controller.confirmacaoPrenhes.ConfirmacaoPrenhesFormController;
import br.com.milkmoney.controller.fichaAnimal.FichaAnimalOverviewController;
import br.com.milkmoney.controller.lactacao.LactacaoFormController;
import br.com.milkmoney.controller.parto.PartoFormController;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.controller.root.RootLayoutController;
import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.ConfirmacaoPrenhes;
import br.com.milkmoney.model.Parametro;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.model.SituacaoAnimal;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.LactacaoService;
import br.com.milkmoney.service.ParametroService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.service.searchers.Search;
import br.com.milkmoney.service.searchers.SearchAnimaisDisponiveisParaCobertura;
import br.com.milkmoney.service.searchers.SearchFemeas;
import br.com.milkmoney.service.searchers.SearchFemeasByNumeroNome;
import br.com.milkmoney.service.searchers.SearchFemeasEmPeriodoVoluntarioEspera;
import br.com.milkmoney.service.searchers.SearchFemeasNaoPrenhasAposXDiasAposParto;
import br.com.milkmoney.service.searchers.SearchFemeasNaoPrenhasXDiasAposParto;
import br.com.milkmoney.validation.CoberturaValidation;
import br.com.milkmoney.validation.Validator;


@Controller
public class CoberturaOverviewController extends AbstractOverviewController<Integer, Cobertura> {

	//COBERTURAS
	@FXML private TableColumn<Cobertura, String>        dataColumn;
	@FXML private TableColumn<Cobertura, String>        reprodutorColumn;
	@FXML private TableColumn<Cobertura, String>        previsaoPartoColumn;
	@FXML private TableColumn<Cobertura, String>        dataPartoColumn;
	@FXML private TableColumn<Cobertura, String>        tipoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String>        situacaoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String>        statusColumn;
	@FXML private TableColumn<Cobertura, String>        dataConfirmacaoColumn;
	@FXML private TableColumn<Cobertura, String>        metodoConfirmacaoColumn;
	//ANIMAIS
	@FXML private TableView<Animal>                     tableAnimais;
	@FXML private TableColumn<Animal, String>           dataNascimentoAnimalColumn;
	@FXML private TableColumn<Animal, String>           situacaoAnimalColumn;
	@FXML private TableColumn<Animal, String>           animalColumn;
	@FXML private TableColumn<Animal, Date>             dataUltimoPartoColumn;
	@FXML private TableColumn<Animal, String>           diasUltimoPartoColumn;
	
	@FXML private TableColumn<Animal, Date>             dataUltimaCoberturaColumn;
	@FXML private TableColumn<Animal, String>           diasUltimaCoberturaColumn;
	@FXML private TableColumn<Animal, Date>             dataPrevisaoLactacaoColumn;
	@FXML private TableColumn<Animal, Date>             dataPrevisaoProximoPartoColumn;
	@FXML private TableColumn<Animal, String>           situacaoUltimaCoberturaColumn;
	
	@FXML private Label                                 lblHeader;
	@FXML private TextField                             inputPesquisaAnimal;
	
	@Autowired private FichaAnimalOverviewController    fichaAnimalOverviewController;
	@Autowired private CoberturaFormController          coberturaFormController;
	@Autowired private ConfirmacaoPrenhesFormController confirmacaoPrenhesFormController;
	@Autowired private PartoFormController              partoFormController;
	@Autowired private RootLayoutController             rootLayoutController;
	@Autowired private AnimalFormController             animalFormController;
	@Autowired private LactacaoFormController           lactacaoFormController;
	@Autowired private AnimalService                    animalService;
	@Autowired private ParametroService                 parametroService;
	@Autowired private RelatorioService					relatorioService;
	@Autowired private LactacaoService                  lactacaoService;
	
	//menu context tabela cobertura
	private MenuItem    registrarPartoMenuItem          = new MenuItem("Registrar Parto");
	private MenuItem    confirmarPrenhesMenuItem        = new MenuItem("Confirmação de Prenhes");
	private MenuItem    fichaAnimalMenuItem             = new MenuItem("Ficha Animal");
	private MenuItem    editarAnimalMenuItem            = new MenuItem("Editar Animal");
	private MenuItem    encerramentoLactacaoMenuItem    = new MenuItem("Encerrar Lactação");
	private ContextMenu menuTabelaAnimais               = new ContextMenu(editarAnimalMenuItem, encerramentoLactacaoMenuItem, fichaAnimalMenuItem);
	private Animal                                      femea;
	
	@FXML
	public void initialize() {
		
		//TABELA ANIMAIS
		dataNascimentoAnimalColumn.setCellFactory(new TableCellDateFactory<Animal,String>("dataNascimento"));
		situacaoAnimalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("situacaoAnimal"));
		animalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numeroNome"));
		dataUltimoPartoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataUltimoParto"));
		diasUltimoPartoColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("diasUltimoParto"));
		dataUltimaCoberturaColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataUltimaCobertura"));
		diasUltimaCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("diasUltimaCobertura"));
		dataPrevisaoLactacaoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataPrevisaoEncerramentoLactacao"));
		dataPrevisaoProximoPartoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataPrevisaoProximoParto"));
		situacaoUltimaCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("situacaoUltimaCobertura"));
		
		handleBuscarTodasAsFemeas();
		tableAnimais.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> animalSelectHandler());
		
		if ( tableAnimais.getItems().size() > 0 ) {
			tableAnimais.getSelectionModel().clearAndSelect(0);
			animalSelectHandler();
		}
		
		fichaAnimalMenuItem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	handleOpenFichaAnimal();
		    }
		});
		
		editarAnimalMenuItem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	handleEditAnimal();
		    }
		});
		
		encerramentoLactacaoMenuItem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	handleDesfazerOuEncerrarLactacao();
		    }
		});
		
		tableAnimais.setContextMenu(menuTabelaAnimais);
		
		dataColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("data"));
		reprodutorColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reprodutor"));
		previsaoPartoColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("previsaoParto"));
		dataPartoColumn.setCellFactory(new TableCellRegistrarPartoHyperlinkFactory<Cobertura,String>("dataParto", registrarParto));
		tipoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("tipoCobertura"));
		situacaoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("situacaoCobertura"));
		statusColumn.setCellFactory(new TableCellSituacaoCoberturaFactory<Cobertura,String>("situacaoCobertura"));
		dataConfirmacaoColumn.setCellFactory(new TableCellConfirmarPrenhesHyperlinkFactory<Cobertura,String>("dataConfirmacaoPrenhes", confirmarPrenhes));
		metodoConfirmacaoColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("metodoConfirmacaoPrenhes"));
		
		tableAnimais.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isSecondaryButtonDown()) {
					encerramentoLactacaoMenuItem.setDisable(femea.getSexo().equals(Sexo.MACHO) || !(femea.getSituacaoAnimal().equals(SituacaoAnimal.SECO) || femea.getSituacaoAnimal().equals(SituacaoAnimal.EM_LACTACAO)));
					encerramentoLactacaoMenuItem.setText(femea.getSituacaoAnimal().equals(SituacaoAnimal.SECO) ? "Desfazer Encerramento Lactação" : "Encerrar Lactação");
				}
			}
		});
		
		registrarPartoMenuItem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	handleRegistrarParto();
		    }
		});
		
		confirmarPrenhesMenuItem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	handleConfirmarPrenhes();
		    }
		});
		
		getContextMenu().getItems().addAll(new SeparatorMenuItem(), registrarPartoMenuItem, confirmarPrenhesMenuItem);
		
		//pesquisa textual animal
		if ( inputPesquisaAnimal != null ){
			inputPesquisaAnimal.textProperty().addListener((observable, oldValue, newValue) -> {
				SearchFemeasByNumeroNome search = (SearchFemeasByNumeroNome) MainApp.getBean(SearchFemeasByNumeroNome.class);
				tableAnimais.setItems(search.doSearch(newValue));
			});
		}
		
		
		super.initialize(coberturaFormController);
		
	}
	
	private void animalSelectHandler(){
		this.femea = tableAnimais.getSelectionModel().getSelectedItem();
		if ( femea != null ){
			lblHeader.setText("COBERTURAS - " + femea.toString());
		}else{
			lblHeader.setText("NENHUM ANIMAL SELECIONADO");
		}
		refreshTableOverview();
	}
	
	@Override
	protected void refreshTableOverview() {
		this.data.clear();
		this.table.getItems().clear();
		
		if ( inputPesquisa != null && inputPesquisa.getText() != null &&
				inputPesquisa.getText().length() > 0){
			data.addAll(handleDefaultSearch());
			setSearch(null);
		}else{
			if ( super.getSearch() != null ){
				data.addAll(super.getSearch().doSearch());
			}else{
				this.data.addAll( ((CoberturaService)service).findByAnimal(tableAnimais.getSelectionModel().getSelectedItem()));
			}
		}
		
		table.setItems(data);
		table.layout();
		updateLabelNumRegistros();
	}
	
	private void refreshAnimalTableOverview(Animal animal){
		if ( animal != null ){
			for (int index = 0; index < tableAnimais.getItems().size(); index++) {
				Animal a = tableAnimais.getItems().get(index);
				if (a.getId() == animal.getId()) {
					tableAnimais.getItems().set(index, animal);
					tableAnimais.getSelectionModel().select(animal);
					break;
				}
			}
		}
	}
	
	//sobrescreve o método para carregar o objeto customizado para a tela de cadastro
	@Override
	public Cobertura newObject() {
		if ( femea == null ){
			throw new ValidationException(Validator.CAMPO_OBRIGATORIO, "Por favor, selecione um animal para registrar a cobertura.");
		}
		return new Cobertura(femea);
	}
	
	@Override
	protected void handleRightClick() {
		super.handleRightClick();
		
		registrarPartoMenuItem.setDisable(getObject().getSituacaoCobertura().equals(SituacaoCobertura.VAZIA));
		registrarPartoMenuItem.setText(getObject().getParto() != null ? "Visualizar Parto" : "Registrar Parto");
		
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
	
	//====FILTROS ANIMAIS=======
	
	@FXML
	private void handleBuscarFemeasDisponeisParaCobertura(){
		doSearchAnimais((SearchAnimaisDisponiveisParaCobertura) MainApp.getBean(SearchAnimaisDisponiveisParaCobertura.class));
	}
	
	@FXML
	private void handleBuscarTodasAsFemeas(){
		doSearchAnimais((SearchFemeas) MainApp.getBean(SearchFemeas.class));
	}
	
	@FXML
	private void handleBuscarEmPeriodoVolutarioDeEspera(){
		doSearchAnimais((SearchFemeasEmPeriodoVoluntarioEspera) MainApp.getBean(SearchFemeasEmPeriodoVoluntarioEspera.class));
	}
	
	@FXML
	private void handleBuscarNaoPrenhasPrimeiroCiclo(){
		int periodoVoluntarioEspera = Integer.parseInt(parametroService.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA));
		int primeiroCiclo = periodoVoluntarioEspera + 21;
		doSearchAnimais((SearchFemeasNaoPrenhasXDiasAposParto) MainApp.getBean(SearchFemeasNaoPrenhasXDiasAposParto.class),primeiroCiclo);
	}
	
	@FXML
	private void handleBuscarNaoPrenhasSegundoCiclo(){
		int periodoVoluntarioEspera = Integer.parseInt(parametroService.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA));
		int primeiroCiclo = periodoVoluntarioEspera + 42;
		doSearchAnimais((SearchFemeasNaoPrenhasXDiasAposParto) MainApp.getBean(SearchFemeasNaoPrenhasXDiasAposParto.class),primeiroCiclo);
	}
	
	@FXML
	private void handleBuscarNaoPrenhasTerceiroCiclo(){
		int periodoVoluntarioEspera = Integer.parseInt(parametroService.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA));
		int primeiroCiclo = periodoVoluntarioEspera + 63;
		doSearchAnimais((SearchFemeasNaoPrenhasXDiasAposParto) MainApp.getBean(SearchFemeasNaoPrenhasXDiasAposParto.class),primeiroCiclo);
	}
	
	@FXML
	private void handleBuscarNaoPrenhasAposTerceiroCiclo(){
		int periodoVoluntarioEspera = Integer.parseInt(parametroService.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA));
		int primeiroCiclo = periodoVoluntarioEspera + 63;
		doSearchAnimais((SearchFemeasNaoPrenhasAposXDiasAposParto) MainApp.getBean(SearchFemeasNaoPrenhasAposXDiasAposParto.class),primeiroCiclo);
	}
	
	
	private void doSearchAnimais(Search<Integer, Animal> search, Object ...params){
		tableAnimais.setItems(search.doSearch(params));
		inputPesquisaAnimal.clear();
		tableAnimais.getSelectionModel().clearAndSelect(0);
	}
	
	//------ANÁLISE REPORT----
	@FXML
	private void gerarRelatorioAnaliseReprodutiva(){
		//os ids dos animais selecionados são passados como parâmetro
		StringBuilder sb = new StringBuilder();
		
		for ( Animal animal : tableAnimais.getItems() ){
			sb.append(animal.getId());
			sb.append(",");
		}
		
		sb.replace(sb.length(), sb.length(), "");
		
		relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.FICHA_ANIMAL, sb.toString());
		rootLayoutController.setMessage("O relatório está sendo executado...");
	}
	
	//====CONTEXT MENUS =======
	
	private void handleOpenFichaAnimal(){
		
		fichaAnimalOverviewController.setAnimal(femea);
		fichaAnimalOverviewController.showForm();
		
	}
	
	private void handleEditAnimal(){
		
		animalFormController.setObject(femea);
		animalFormController.showForm();
		
	}
	
	private void handleDesfazerOuEncerrarLactacao(){
		
		if ( tableAnimais.getSelectionModel().getSelectedItem() != null ){
			
			if ( femea.getSituacaoAnimal().equals(SituacaoAnimal.SECO) ){
			
				Optional<ButtonType> result = CustomAlert.confirmar("Desfazer Encerramento Lactação", "Tem certeza que deseja desfazer o encerramento da última lactação?");
				if (result.get() == ButtonType.OK) {
					lactacaoService.desfazerEncerramentoLactacao(femea);
					refreshAnimalTableOverview(animalService.findById(femea.getId()));
				}
				
			}else{
				if ( femea.getSituacaoAnimal().equals(SituacaoAnimal.EM_LACTACAO) ){
					lactacaoFormController.setObject(lactacaoService.findUltimaLactacaoAnimal(femea));
					lactacaoFormController.showForm();
					refreshAnimalTableOverview(animalService.findById(femea.getId()));
				}
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		
	};
	
	private void handleRegistrarParto(){
		
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return;
		}
		if ( getObject().getParto() == null ){
			
			CoberturaValidation.validaRegistroPartoCobertura(getObject(), lactacaoService.findUltimaLactacaoAnimal(getObject().getFemea()));
			
			partoFormController.setState(State.CREATE_TO_SELECT);
			partoFormController.setObject(new Parto(getObject()));
			partoFormController.showForm();
			
			if ( partoFormController.getObject() != null ){
				getObject().setParto(partoFormController.getObject());
				((CoberturaService)service).registrarParto(getObject());
				refreshObjectInTableView.apply(getObject());
				refreshAnimalTableOverview(animalService.findById(femea.getId()));
			}	
			
		}else{
			
			partoFormController.setObject(getObject().getParto());
			partoFormController.showForm();
			refreshObjectInTableView.apply(getObject());
			refreshAnimalTableOverview(animalService.findById(femea.getId()));
			
		}
		
		
	}
	
	private void handleConfirmarPrenhes(){
		
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return;
		}
		
		confirmacaoPrenhesFormController.setState(State.CREATE_TO_SELECT);
		confirmacaoPrenhesFormController.setObject(new ConfirmacaoPrenhes(getObject()));
    	confirmacaoPrenhesFormController.showForm();
    	
    	if ( !getObject().getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
    		((CoberturaService)service).saveConfirmacaoPrenhes(getObject());
    		refreshObjectInTableView.apply(service.findById(getObject().getId()));
    	}
    	
	}
	
	//====FUNCTIONS
	
	Function<Integer, Boolean> registrarParto = index -> {
		table.getSelectionModel().select(index);
		handleRegistrarParto();
		return true;
	};
	
	Function<Integer, Boolean> confirmarPrenhes = index -> {
		table.getSelectionModel().select(index);
		handleConfirmarPrenhes();
		return true;
	};
	
	
	
}
