package br.com.milkmoney.controller.cobertura;

import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.cobertura.renderer.TableCellSituacaoCoberturaFactory;
import br.com.milkmoney.controller.confirmacaoPrenhes.ConfirmacaoPrenhesFormController;
import br.com.milkmoney.controller.fichaAnimal.FichaAnimalOverviewController;
import br.com.milkmoney.controller.parto.PartoFormController;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.controller.root.RootLayoutController;
import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.ConfirmacaoPrenhes;
import br.com.milkmoney.model.Parametro;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.ParametroService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.service.searchers.Search;
import br.com.milkmoney.service.searchers.SearchAnimaisDisponiveisParaCobertura;
import br.com.milkmoney.service.searchers.SearchFemeas;
import br.com.milkmoney.service.searchers.SearchFemeasByNumeroNome;
import br.com.milkmoney.service.searchers.SearchFemeasEmPeriodoVoluntarioEspera;
import br.com.milkmoney.service.searchers.SearchFemeasNaoPrenhasAposXDiasAposParto;
import br.com.milkmoney.service.searchers.SearchFemeasNaoPrenhasXDiasAposParto;
import br.com.milkmoney.validation.Validator;


@Controller
public class CoberturaOverviewController extends AbstractOverviewController<Integer, Cobertura> {

	@FXML private TableColumn<Cobertura, String>        dataColumn;
	@FXML private TableColumn<Cobertura, String>        reprodutorColumn;
	@FXML private TableColumn<Cobertura, String>        previsaoPartoColumn;
	@FXML private TableColumn<Cobertura, String>        dataPartoColumn;
	@FXML private TableColumn<Cobertura, String>        tipoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String>        situacaoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String>        statusColumn;
	
	@FXML private TableView<Animal>                     tableAnimais;
	@FXML private TableColumn<Animal, String>           dataNascimentoAnimalColumn;
	@FXML private TableColumn<Animal, String>           situacaoAnimalColumn;
	@FXML private TableColumn<Animal, String>           animalColumn;
	
	@FXML private Label                                 lblHeader;
	@FXML private TextField                             inputPesquisaAnimal;
	
	@Autowired private FichaAnimalOverviewController    fichaAnimalOverviewController;
	@Autowired private CoberturaFormController          coberturaFormController;
	@Autowired private ConfirmacaoPrenhesFormController confirmacaoPrenhesFormController;
	@Autowired private PartoFormController              partoFormController;
	@Autowired private RootLayoutController             rootLayoutController;
	@Autowired private AnimalService                    animalService;
	@Autowired private ParametroService                 parametroService;
	@Autowired private RelatorioService					relatorioService;
	
	//menu context tabela cobertura
	private MenuItem    registrarPartoMenuItem          = new MenuItem("Registrar Parto");
	private MenuItem    confirmarPrenhesMenuItem        = new MenuItem("Confirmação de Prenhes");
	private MenuItem    fichaAnimalMenuItem             = new MenuItem("Ficha Animal");
	private ContextMenu menuTabelaAnimais               = new ContextMenu(fichaAnimalMenuItem);
	private Animal                                      femea;
	
	@FXML
	public void initialize() {
		
		//TABELA ANIMAIS
		dataNascimentoAnimalColumn.setCellFactory(new TableCellDateFactory<Animal,String>("dataNascimento"));
		situacaoAnimalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("situacaoAnimal"));
		animalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numeroNome"));
		
		handleBuscarFemeasDisponeisParaCobertura();
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
		
		tableAnimais.setContextMenu(menuTabelaAnimais);
		
		dataColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("data"));
		reprodutorColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reprodutor"));
		previsaoPartoColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("previsaoParto"));
		dataPartoColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("dataParto"));
		tipoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("tipoCobertura"));
		situacaoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("situacaoCobertura"));
		statusColumn.setCellFactory(new TableCellSituacaoCoberturaFactory<Cobertura,String>("situacaoCobertura"));
		
		super.initialize(coberturaFormController);
		
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
	
	private void handleRegistrarParto(){
		
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return;
		}
		if ( getObject().getParto() == null ){
			if ( getObject().getSituacaoCobertura().matches(SituacaoCobertura.INDEFINIDA + "|" + SituacaoCobertura.PRENHA) ){
				
				partoFormController.setState(State.CREATE_TO_SELECT);
				partoFormController.setObject(new Parto(getObject()));
				partoFormController.showForm();
				
				if ( partoFormController.getObject() != null ){
					getObject().setParto(partoFormController.getObject());
					((CoberturaService)service).registrarParto(getObject());
					refreshObjectInTableView.apply(getObject());
				}	
				
			}else{
				CustomAlert.mensagemAlerta("Regra de Negócio", "A cobertura selecionada tem situação igual a " + getObject().getSituacaoCobertura() + 
						" não sendo possível registrar o parto.");
			}
		}else{
			
			partoFormController.setObject(getObject().getParto());
			partoFormController.showForm();
			refreshObjectInTableView.apply(getObject());
			
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
    	((CoberturaService)service).saveConfirmacaoPrenhes(getObject());
    	refreshObjectInTableView.apply(service.findById(getObject().getId()));
    	
	}
	
	//====FUNCTIONS
	
	Function<Integer, Boolean> permiteEditar = index -> {
		
		if ( index >= 0 ){
			setObject(data.get(index));
		}
		
		if ( getObject() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return false;
		}
		
		if ( getObject().getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
			CustomAlert.mensagemInfo("A cobertura já tem parto registrado, não sendo possível alteração.");
			return false;
		}
		
		return true;
		
	};
	
}
