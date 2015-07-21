package br.com.milksys.controller.cobertura;

import java.util.Optional;
import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.CustomAlert;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.controller.cobertura.renderer.TableCellSituacaoCoberturaFactory;
import br.com.milksys.controller.confirmacaoPrenhez.ConfirmacaoPrenhezFormController;
import br.com.milksys.controller.parto.PartoFormController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.ConfirmacaoPrenhez;
import br.com.milksys.model.Parto;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.model.State;
import br.com.milksys.service.AnimalService;
import br.com.milksys.service.CoberturaService;
import br.com.milksys.service.IService;


@Controller
public class CoberturaOverviewController extends AbstractOverviewController<Integer, Cobertura> {

	@FXML private TableColumn<Cobertura, String> dataColumn;
	@FXML private TableColumn<Cobertura, String> reprodutorColumn;
	@FXML private TableColumn<Cobertura, String> previsaoPartoColumn;
	@FXML private TableColumn<Cobertura, String> dataPartoColumn;
	@FXML private TableColumn<Cobertura, String> previsaoEncerramentoLactacaoColumn;
	@FXML private TableColumn<Cobertura, String> tipoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String> situacaoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String> statusColumn;
	
	@FXML private ListView<Animal> listAnimal;
	@FXML private Label lblHeader;
	
	@Autowired private CoberturaFormController coberturaFormController;
	@Autowired private ConfirmacaoPrenhezFormController confirmacaoPrenhezFormController;
	@Autowired private PartoFormController partoFormController;
	@Autowired private AnimalService animalService;
	
	private MenuItem registrarPartoMenuItem   = new MenuItem("Registrar Parto");
	private MenuItem removerPartoMenuItem     = new MenuItem("Remover Parto");
	private MenuItem confirmarPrenhezMenuItem = new MenuItem("Confirmação de Prenhez");
	
	@FXML
	public void initialize() {
		
		//list animais
		listAnimal.setItems(animalService.findAllFemeasAtivasAsObservableList());
		listAnimal.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> animalSelectHandler());
		
		if ( listAnimal.getItems().size() > 0 ) {
			listAnimal.getSelectionModel().clearAndSelect(0);
		}
		
		dataColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("data"));
		reprodutorColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reprodutor"));
		previsaoPartoColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("previsaoParto"));
		dataPartoColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("dataParto"));
		previsaoEncerramentoLactacaoColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("previsaoEncerramentoLactacao"));
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
		
	}
	
	private void animalSelectHandler(){
		lblHeader.setText("COBERTURAS - " + listAnimal.getSelectionModel().getSelectedItem().toString());
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
				this.data.addAll( ((CoberturaService)service).findByAnimal(listAnimal.getSelectionModel().getSelectedItem()));
			}
		}
		
		table.setItems(data);
		table.layout();
		updateLabelNumRegistros();
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
	
	//====HANDLERS
	
	@FXML
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
	
	@FXML
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
			
			confirmacaoPrenhezFormController.setObject(new ConfirmacaoPrenhez(getObject()));
	    	confirmacaoPrenhezFormController.showForm();
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
