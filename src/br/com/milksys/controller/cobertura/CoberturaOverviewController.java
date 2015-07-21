package br.com.milksys.controller.cobertura;

import java.util.Optional;
import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
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

import br.com.milksys.MainApp;
import br.com.milksys.components.CustomAlert;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.controller.cobertura.renderer.TableCellSituacaoCoberturaFactory;
import br.com.milksys.controller.confirmacaoPrenhez.ConfirmacaoPrenhezFormController;
import br.com.milksys.controller.parto.PartoFormController;
import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.ConfirmacaoPrenhez;
import br.com.milksys.model.Parametro;
import br.com.milksys.model.Parto;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.model.State;
import br.com.milksys.service.AnimalService;
import br.com.milksys.service.CoberturaService;
import br.com.milksys.service.IService;
import br.com.milksys.service.ParametroService;
import br.com.milksys.service.searchers.Search;
import br.com.milksys.service.searchers.SearchAnimaisDisponiveisParaCobertura;
import br.com.milksys.service.searchers.SearchFemeas;
import br.com.milksys.service.searchers.SearchFemeasByNumeroNome;
import br.com.milksys.service.searchers.SearchFemeasEmPeriodoVoluntarioEspera;
import br.com.milksys.service.searchers.SearchFemeasNaoPrenhasXDiasAposParto;
import br.com.milksys.validation.Validator;


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
	
	@Autowired private CoberturaFormController          coberturaFormController;
	@Autowired private ConfirmacaoPrenhezFormController confirmacaoPrenhezFormController;
	@Autowired private PartoFormController              partoFormController;
	@Autowired private AnimalService                    animalService;
	@Autowired private ParametroService                 parametroService;
	
	//menu context tabela cobertura
	private MenuItem registrarPartoMenuItem             = new MenuItem("Registrar Parto");
	private MenuItem removerPartoMenuItem               = new MenuItem("Remover Parto");
	private MenuItem confirmarPrenhezMenuItem           = new MenuItem("Confirmação de Prenhez");
	
	private Animal                                      femea;
	
	@FXML
	public void initialize() {
		
		//TABELA ANIMAIS
		dataNascimentoAnimalColumn.setCellFactory(new TableCellDateFactory<Animal,String>("dataNascimento"));
		situacaoAnimalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("situacaoAnimal"));
		animalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numeroNome"));
		
		doSearchAnimais((SearchFemeas) MainApp.getBean(SearchFemeas.class));
		tableAnimais.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> animalSelectHandler());
		
		if ( tableAnimais.getItems().size() > 0 ) {
			tableAnimais.getSelectionModel().clearAndSelect(0);
		}
		
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
		
		removerPartoMenuItem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	handleRemoverParto();
		    }
		});
		
		confirmarPrenhezMenuItem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	handleConfirmarPrenhez();
		    }
		});
		
		getContextMenu().getItems().addAll(new SeparatorMenuItem(), registrarPartoMenuItem, removerPartoMenuItem, confirmarPrenhezMenuItem);
		
		//pesquisa textual animal
		if ( inputPesquisaAnimal != null ){
			inputPesquisaAnimal.textProperty().addListener((observable, oldValue, newValue) -> {
				SearchFemeasByNumeroNome search = (SearchFemeasByNumeroNome) MainApp.getBean(SearchFemeasByNumeroNome.class);
				tableAnimais.setItems(search.doSearch(new Object[]{newValue}));
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
		doSearchAnimais((SearchFemeasNaoPrenhasXDiasAposParto) MainApp.getBean(SearchFemeasNaoPrenhasXDiasAposParto.class),
				new Object[]{primeiroCiclo});
	}
	
	@FXML
	private void handleBuscarNaoPrenhasSegundoCiclo(){
		int periodoVoluntarioEspera = Integer.parseInt(parametroService.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA));
		int primeiroCiclo = periodoVoluntarioEspera + 42;
		doSearchAnimais((SearchFemeasNaoPrenhasXDiasAposParto) MainApp.getBean(SearchFemeasNaoPrenhasXDiasAposParto.class),
				new Object[]{primeiroCiclo});
	}
	
	@FXML
	private void handleBuscarNaoPrenhasTerceiroCiclo(){
		int periodoVoluntarioEspera = Integer.parseInt(parametroService.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA));
		int primeiroCiclo = periodoVoluntarioEspera + 63;
		doSearchAnimais((SearchFemeasNaoPrenhasXDiasAposParto) MainApp.getBean(SearchFemeasNaoPrenhasXDiasAposParto.class),
				new Object[]{primeiroCiclo});
	}
	
	private void doSearchAnimais(Search<Integer, Animal> search, Object ...params){
		tableAnimais.setItems(search.doSearch(params));
		inputPesquisaAnimal.clear();
		tableAnimais.getSelectionModel().clearAndSelect(0);
	}
	
	//====CONTEXT MENU COBERTURAS=======
	private void handleRegistrarParto(){
		
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return;
		}
		
		if ( getObject().getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) ||
				getObject().getSituacaoCobertura().equals(SituacaoCobertura.INDEFINIDA) ||
						getObject().getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
			
			partoFormController.setState(State.CREATE_TO_SELECT);
			
			if ( getObject().getParto() != null && getObject().getParto().getId() > 0 ){
				partoFormController.setObject(getObject().getParto());
			}else{
				partoFormController.setObject(new Parto(getObject()));
			}
			
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
		
	}
	
	private void handleRemoverParto(){
		
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return;
		}
		
		if ( getObject().getParto() != null ){
			
			Optional<ButtonType> result = CustomAlert.confirmarExclusao("Confirmar remoção registro", "Tem certeza que deseja remover o parto registrado?");
			if (result.get() == ButtonType.OK) {
				((CoberturaService)service).removerParto(getObject());
				refreshObjectInTableView.apply(getObject());
			}
			
		}else{
			CustomAlert.mensagemAlerta("Regra de Negócio", "A cobertura selecionada não tem parto registrado.");
		}
		
	}
	
	private void handleConfirmarPrenhez(){
		
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return;
		}
		
		if ( permiteEditar.apply(table.getSelectionModel().getSelectedIndex()) ){
			confirmacaoPrenhezFormController.setState(State.CREATE_TO_SELECT);
			confirmacaoPrenhezFormController.setObject(new ConfirmacaoPrenhez(getObject()));
	    	confirmacaoPrenhezFormController.showForm();
	    	((CoberturaService)service).saveConfirmacaoPrenhez(getObject());
	    	refreshObjectInTableView.apply(service.findById(getObject().getId()));
	    	
		}
    	
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
