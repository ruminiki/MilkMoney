package br.com.milkmoney.controller.cobertura;

import java.util.function.Function;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.components.WaitReport;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.aborto.AbortoFormController;
import br.com.milkmoney.controller.cobertura.renderer.TableCellConfirmarPrenhezHyperlinkFactory;
import br.com.milkmoney.controller.cobertura.renderer.TableCellRegistrarAbortoHyperlinkFactory;
import br.com.milkmoney.controller.cobertura.renderer.TableCellRegistrarPartoHyperlinkFactory;
import br.com.milkmoney.controller.cobertura.renderer.TableCellSituacaoCoberturaFactory;
import br.com.milkmoney.controller.confirmacaoPrenhez.ConfirmacaoPrenhezFormController;
import br.com.milkmoney.controller.lactacao.LactacaoFormController;
import br.com.milkmoney.controller.parto.PartoFormController;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.FichaAnimalService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.LactacaoService;
import br.com.milkmoney.service.ParametroService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.CoberturaValidation;


@Controller
public class CoberturaOverviewController extends AbstractOverviewController<Integer, Cobertura> {

	//COBERTURAS
	@FXML private TableColumn<Cobertura, String>        dataColumn;
	@FXML private TableColumn<Cobertura, String>        reprodutorColumn;
	@FXML private TableColumn<Cobertura, String>        previsaoPartoColumn;
	@FXML private TableColumn<Cobertura, String>        dataPartoColumn;
	@FXML private TableColumn<Cobertura, String>        dataAbortoColumn;
	@FXML private TableColumn<Cobertura, String>        tipoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String>        situacaoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String>        statusColumn;
	@FXML private TableColumn<Cobertura, String>        dataConfirmacaoColumn;
	@FXML private TableColumn<Cobertura, String>        metodoConfirmacaoColumn;
	@FXML private Label									lblHeader;
	
	@Autowired private CoberturaFormController          coberturaFormController;
	@Autowired private ConfirmacaoPrenhezFormController confirmacaoPrenhezFormController;
	@Autowired private AbortoFormController             abortoFormController;
	@Autowired private PartoFormController              partoFormController;
	@Autowired private LactacaoFormController           lactacaoFormController;
	@Autowired private AnimalService                    animalService;
	@Autowired private ParametroService                 parametroService;
	@Autowired private RelatorioService					relatorioService;
	@Autowired private LactacaoService                  lactacaoService;
	@Autowired private FichaAnimalService               fichaAnimalService;
	
	private Animal                                      femea;
	
	@FXML
	public void initialize() {
		
		dataColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("data"));
		reprodutorColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reprodutor"));
		previsaoPartoColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("previsaoParto"));
		dataPartoColumn.setCellFactory(new TableCellRegistrarPartoHyperlinkFactory<Cobertura,String>("dataParto", registrarParto));
		dataAbortoColumn.setCellFactory(new TableCellRegistrarAbortoHyperlinkFactory<Cobertura,String>("dataAborto", registrarAborto));
		tipoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("tipoCobertura"));
		situacaoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("situacaoCobertura"));
		statusColumn.setCellFactory(new TableCellSituacaoCoberturaFactory<Cobertura,String>("situacaoCobertura"));
		dataConfirmacaoColumn.setCellFactory(new TableCellConfirmarPrenhezHyperlinkFactory<Cobertura,String>("dataConfirmacaoPrenhez", confirmarPrenhez));
		metodoConfirmacaoColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("metodoConfirmacaoPrenhez"));
		
		super.initialize(coberturaFormController);
		
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
				this.data.addAll( ((CoberturaService)service).findByAnimal(femea, DateUtil.today));
			}
			
			table.setItems(data);
			table.layout();
			updateLabelNumRegistros();
		}
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
		WaitReport.wait(relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.IMPRIMIR_COBERTURAS_ANIMAL, params), MainApp.primaryStage);
		
	}
	
	//====FUNCTIONS
	
	Function<Integer, Boolean> registrarParto = index -> {
		table.getSelectionModel().select(index);
		
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return false;
		}
		if ( getObject().getParto() == null ){
			
			CoberturaValidation.validaRegistroPartoCobertura(getObject(), lactacaoService.findLastBeforeDate(getObject().getFemea(), getObject().getData()));
			
			partoFormController.setState(State.CREATE_TO_SELECT);
			partoFormController.setObject(new Parto(getObject()));
			partoFormController.showForm();
			
			if ( partoFormController.getObject() != null && partoFormController.getObject().getLactacao() != null ){
				getObject().setParto(partoFormController.getObject());
				((CoberturaService)service).registrarParto(getObject());
				refreshObjectInTableView.apply(service.findById(getObject().getId()));
			}	
			
		}else{
			
			partoFormController.setObject(getObject().getParto());
			partoFormController.showForm();
			refreshObjectInTableView.apply(service.findById(getObject().getId()));
			
		}
		
		return true;
	};
	
	Function<Integer, Boolean> registrarAborto = index -> {
		table.getSelectionModel().select(index);
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return false;
		}
		
		abortoFormController.setObject(getObject());
		abortoFormController.showForm();
    	
		refreshObjectInTableView.apply(service.findById(getObject().getId()));
		return true;
	};
	
	Function<Integer, Boolean> confirmarPrenhez = index -> {
		table.getSelectionModel().select(index);
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return false;
		}
		
		confirmacaoPrenhezFormController.setObject(getObject());
    	confirmacaoPrenhezFormController.showForm();
    	
		refreshObjectInTableView.apply(service.findById(getObject().getId()));
		return true;
	};
	
	
	
}
