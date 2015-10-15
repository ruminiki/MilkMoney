package br.com.milkmoney.controller.cobertura;

import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.cobertura.renderer.TableCellConfirmarPrenhesHyperlinkFactory;
import br.com.milkmoney.controller.cobertura.renderer.TableCellRegistrarPartoHyperlinkFactory;
import br.com.milkmoney.controller.cobertura.renderer.TableCellSituacaoCoberturaFactory;
import br.com.milkmoney.controller.confirmacaoPrenhes.ConfirmacaoPrenhesFormController;
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
import br.com.milkmoney.validation.CoberturaValidation;


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
	@FXML private Label									lblHeader;
	
	@Autowired private CoberturaFormController          coberturaFormController;
	@Autowired private ConfirmacaoPrenhesFormController confirmacaoPrenhesFormController;
	@Autowired private PartoFormController              partoFormController;
	@Autowired private LactacaoFormController           lactacaoFormController;
	@Autowired private AnimalService                    animalService;
	@Autowired private ParametroService                 parametroService;
	@Autowired private RelatorioService					relatorioService;
	@Autowired private LactacaoService                  lactacaoService;
	@Autowired private FichaAnimalService               fichaAnimalService;
	
	//menu context tabela cobertura
	private MenuItem    registrarPartoMenuItem          = new MenuItem("Registrar Parto");
	private MenuItem    confirmarPrenhesMenuItem        = new MenuItem("Confirmação de Prenhes");
	private Animal                                      femea;
	
	@FXML
	public void initialize() {
		
		dataColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("data"));
		reprodutorColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reprodutor"));
		previsaoPartoColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("previsaoParto"));
		dataPartoColumn.setCellFactory(new TableCellRegistrarPartoHyperlinkFactory<Cobertura,String>("dataParto", registrarParto));
		tipoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("tipoCobertura"));
		situacaoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("situacaoCobertura"));
		statusColumn.setCellFactory(new TableCellSituacaoCoberturaFactory<Cobertura,String>("situacaoCobertura"));
		dataConfirmacaoColumn.setCellFactory(new TableCellConfirmarPrenhesHyperlinkFactory<Cobertura,String>("dataConfirmacaoPrenhes", confirmarPrenhes));
		metodoConfirmacaoColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("metodoConfirmacaoPrenhes"));
		
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
		
		lblHeader.setText(femea != null ? femea.toString() : "ANIMAL NÃO SELECIOANDO");
		
	}
	
	@Override
	public Cobertura newObject() {
		return new Cobertura(femea);
	}
	
	@Override
	protected void refreshTableOverview() {
		if ( femea != null ){
			
			this.data.clear();
			this.table.getItems().clear();
			
			if ( inputPesquisa != null && inputPesquisa.getText() != null &&
					inputPesquisa.getText().length() > 0){
				data.addAll(((CoberturaService)service).defaultSearch(inputPesquisa.getText(), femea));
				setSearch(null);
			}else{
				this.data.addAll( ((CoberturaService)service).findByAnimal(femea));
			}
			
			table.setItems(data);
			table.layout();
			updateLabelNumRegistros();
		}
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
	
	public void setFemea(Animal femea) {
		this.femea = femea;
	}
	
	@FXML
	private void imprimir(){
		Object[] params = new Object[]{
				femea.getId()
		};
		relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.IMPRIMIR_COBERTURAS_ANIMAL, params);
		
	}
	
	//====CONTEXT MENUS =======
	
	private void handleRegistrarParto(){
		
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return;
		}
		if ( getObject().getParto() == null ){
			
			CoberturaValidation.validaRegistroPartoCobertura(getObject(), lactacaoService.findLastBeforeDate(getObject().getFemea(), getObject().getData()));
			
			partoFormController.setState(State.CREATE_TO_SELECT);
			partoFormController.setObject(new Parto(getObject()));
			partoFormController.showForm();
			
			if ( partoFormController.getObject() != null ){
				getObject().setParto(partoFormController.getObject());
				((CoberturaService)service).registrarParto(getObject());
				refreshObjectInTableView.apply(getObject());
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
		
		confirmacaoPrenhesFormController.setObject(getObject());
    	confirmacaoPrenhesFormController.showForm();
		refreshObjectInTableView.apply(service.findById(getObject().getId()));
    	
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
