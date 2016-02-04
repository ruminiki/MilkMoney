package br.com.milkmoney.controller.producaoIndividual;

import java.time.LocalDate;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.PropertyDecimalValueFactory;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.components.TableCellIndexFactory;
import br.com.milkmoney.components.events.ActionEvent;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.model.ProducaoIndividual;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.LactacaoService;
import br.com.milkmoney.service.ProducaoIndividualService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.service.searchers.SearchFemeasAtivas;

@Controller
public class ProducaoIndividualOverviewController extends AbstractOverviewController<Integer, ProducaoIndividual> implements ApplicationListener<ActionEvent>{

	//lactacoes
	@FXML private TableView<Lactacao> tableLactacoes;
	@FXML private TableColumn<Lactacao, String> numeroLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> dataInicioLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> dataTerminoLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> diasEmLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> mesesEmLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> mediaProducaoColumn;
	
	@FXML private TableColumn<ProducaoIndividual, LocalDate> dataColumn;
	@FXML private TableColumn<ProducaoIndividual, String> primeiraOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> segundaOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> terceiraOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> totalColumn;
	@FXML private TableColumn<ProducaoIndividual, String> valorColumn;
	
	@FXML private Label lblHeader;
	
	@FXML private VBox vBoxChart;
	private AreaChart<String, Number> lineChart;
	
	@Autowired private RelatorioService relatorioService;
	@Autowired private LactacaoService lactacaoService;
	@Autowired private SearchFemeasAtivas searchFemeasAtivas;
	@Autowired private ProducaoIndividualFormController producaoIndividualFormController;
	
	private Animal animal;
	private Lactacao lactacao;
	
	@FXML
	public void initialize() {
		
		//tabela lacta��es
		numeroLactacaoColumn.setCellFactory(new TableCellIndexFactory<Lactacao,String>());
		dataInicioLactacaoColumn.setCellFactory(new TableCellDateFactory<Lactacao,String>("dataInicio"));
		dataTerminoLactacaoColumn.setCellFactory(new TableCellDateFactory<Lactacao,String>("dataFim"));
		diasEmLactacaoColumn.setCellValueFactory(new PropertyValueFactory<Lactacao,String>("diasLactacao"));
		mesesEmLactacaoColumn.setCellValueFactory(new PropertyValueFactory<Lactacao,String>("mesesLactacao"));
		mediaProducaoColumn.setCellValueFactory(new PropertyValueFactory<Lactacao,String>("mediaProducao"));
		
		tableLactacoes.getItems().clear();
		tableLactacoes.setItems(lactacaoService.findLactacoesAnimal(animal));
		
		lblHeader.setText("PRODU��O INDIVIDUAL " + animal.toString());
	
		dataColumn.setCellFactory(new TableCellDateFactory<ProducaoIndividual, LocalDate>("data"));
		primeiraOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("primeiraOrdenha", 0));
		segundaOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("segundaOrdenha", 0));
		terceiraOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("terceiraOrdenha", 0));
		totalColumn.setCellValueFactory(new PropertyValueFactory<ProducaoIndividual,String>("totalProducaoDia"));
		valorColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("valor", 3));
		
		//gr�fico
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        
        xAxis.setLabel("Data");
        
        lineChart = new AreaChart<String,Number>(xAxis,yAxis);
        
        lineChart.setTitle("Registro Produ��o Individual");
        lineChart.setLegendVisible(true);
        
        VBox.setVgrow(lineChart, Priority.SOMETIMES);
        HBox.setHgrow(lineChart, Priority.SOMETIMES);
        
        vBoxChart.getChildren().add(lineChart);
        
        tableLactacoes.getSelectionModel().selectedItemProperty().addListener(
        		(observable, oldValue, newValue) -> selectRowLactacaoTableHandler(newValue));
        
        //seta null para n�o pegar a lacta��o de outro animal
        lactacao = null;
        
        super.initialize(producaoIndividualFormController);

	}
	
	private void selectRowLactacaoTableHandler(Lactacao newValue) {
    	if ( newValue != null && data != null ){
    		lactacao = newValue;
    		data.clear();
    		data.addAll(((ProducaoIndividualService)service).findByLactacao(lactacao));
    		((ProducaoIndividualService)service).atualizaValorProducao(data);
    		lineChart.getData().clear();
    		lineChart.getData().addAll(((ProducaoIndividualService)service).getDataChart(lactacao));
    	}
	}

	@Override
	public void handleNew() {
		if ( lactacao != null && lactacao.getId() > 0 ){
			producaoIndividualFormController.setRefreshObjectInTableView(refreshObjectInTableView);
			producaoIndividualFormController.setState(State.INSERT);
			producaoIndividualFormController.setObject(new ProducaoIndividual(animal, lactacao));
			producaoIndividualFormController.showForm();
		}else{
			CustomAlert.mensagemInfo("� necess�rio selecionar uma lacta��o para o registro da produ��o do animal.");
		}
	}
	
	@Override
	public void handleEdit() {
		if ( getObject() != null ){
			producaoIndividualFormController.setRefreshObjectInTableView(refreshObjectInTableView);
			producaoIndividualFormController.setState(State.INSERT);
			producaoIndividualFormController.setObject(getObject());
			producaoIndividualFormController.showForm();
			refreshTableOverview();
		}
	}
	
	@Override
	protected void handleDelete() {
		super.handleDelete();
		refreshTableOverview();
	}
	
	/*
	 * Recupera o evento disparado pelo form ao salvar o objeto
	 * para ser poss�vel atualizar o gr�fico.
	 */
	@Override
	public void onApplicationEvent(ActionEvent event) {
		if ( event != null ){
			if ( ( event.getEventType().equals(ActionEvent.EVENT_INSERT) || event.getEventType().equals(ActionEvent.EVENT_UPDATE)) ){
				
				if ( event.getSource().getClass().isInstance(getObject()) ){
					refreshTableOverview();
				}
			}
		}
	}
	
	@Override
	protected void refreshTableOverview() {
		data.clear();
		
		if ( lactacao == null ){
			if ( tableLactacoes.getItems() != null && tableLactacoes.getItems().size() > 0 ){
				//table.getSelectionModel().clearAndSelect(tableLactacoes.getItems().size()-1);
				lactacao = tableLactacoes.getItems().get(tableLactacoes.getItems().size()-1);
				//recarrega as lacta��es para atualizar a m�dia de produ��o do per�odo
				//tableLactacoes.setItems(lactacaoService.findLactacoesAnimal(animal));
				//tableLactacoes.getSelectionModel().select(lactacao);
				tableLactacoes.getSelectionModel().clearAndSelect(tableLactacoes.getItems().size()-1);
				refreshTableOverview();
			}else{
				return;
			}
		}else{
			data.addAll(((ProducaoIndividualService)service).findByLactacao(lactacao));
		}
		
		((ProducaoIndividualService)service).atualizaValorProducao(data);
		
		lineChart.getData().clear();
		lineChart.getData().addAll(((ProducaoIndividualService)service).getDataChart(lactacao));
		
	}
	
	@FXML
	private void imprimir(){
		
		Lactacao lactacao = null;
		if ( tableLactacoes.getItems().size() > 0 ){
			lactacao = tableLactacoes.getItems().get(0);	
		}
		
		if ( lactacao == null ){
			CustomAlert.mensagemInfo("O animal selecionado n�o tem nenhum registro de controle leiteiro.");
			return;
		}
		
		Object[] params = new Object[]{
				String.valueOf(lactacao.getAnimal().getId()),
				lactacao.getDataInicio(),
				new Date()
		};
		
		relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
			RelatorioService.RELATORIO_CONTROLE_LEITEIRO, params);
		
	}
	
	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	@Override
	public String getFormName() {
		return "view/producaoIndividual/ProducaoIndividualOverview.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Produ��o Individual";
	}
	
	@Override
	@Resource(name = "producaoIndividualService")
	protected void setService(IService<Integer, ProducaoIndividual> service) {
		super.setService(service);
	}

}
