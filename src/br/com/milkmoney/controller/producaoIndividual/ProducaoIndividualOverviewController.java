package br.com.milkmoney.controller.producaoIndividual;

import java.time.LocalDate;

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
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.model.ProducaoIndividual;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.LactacaoService;
import br.com.milkmoney.service.ProducaoIndividualService;
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
	
	@Autowired private LactacaoService lactacaoService;
	@Autowired private SearchFemeasAtivas searchFemeasAtivas;
	@Autowired private ProducaoIndividualFormController producaoIndividualFormController;
	
	private Animal animal;
	private Lactacao lactacao;
	
	@FXML
	public void initialize() {
		
		//tabela lactações
		numeroLactacaoColumn.setCellFactory(new TableCellIndexFactory<Lactacao,String>());
		dataInicioLactacaoColumn.setCellFactory(new TableCellDateFactory<Lactacao,String>("dataInicio"));
		dataTerminoLactacaoColumn.setCellFactory(new TableCellDateFactory<Lactacao,String>("dataFim"));
		diasEmLactacaoColumn.setCellValueFactory(new PropertyValueFactory<Lactacao,String>("diasLactacao"));
		mesesEmLactacaoColumn.setCellValueFactory(new PropertyValueFactory<Lactacao,String>("mesesLactacao"));
		mediaProducaoColumn.setCellValueFactory(new PropertyValueFactory<Lactacao,String>("mediaProducao"));
		
		tableLactacoes.getItems().clear();
		tableLactacoes.setItems(lactacaoService.findLactacoesAnimal(animal));
		
		lblHeader.setText("PRODUÇÃO INDIVIDUAL " + animal.toString());
	
		dataColumn.setCellFactory(new TableCellDateFactory<ProducaoIndividual, LocalDate>("data"));
		primeiraOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("primeiraOrdenha"));
		segundaOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("segundaOrdenha"));
		terceiraOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("terceiraOrdenha"));
		totalColumn.setCellValueFactory(new PropertyValueFactory<ProducaoIndividual,String>("totalProducaoDia"));
		valorColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("valor"));
		
		//gráfico
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        
        xAxis.setLabel("Data");
        
        lineChart = new AreaChart<String,Number>(xAxis,yAxis);
        
        lineChart.setTitle("Registro Produção Individual");
        lineChart.setLegendVisible(true);
        
        VBox.setVgrow(lineChart, Priority.SOMETIMES);
        HBox.setHgrow(lineChart, Priority.SOMETIMES);
        
        vBoxChart.getChildren().add(lineChart);
        
        tableLactacoes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectRowLactacaoTableHandler(newValue));
        
        lactacao = null;
        
		super.initialize(producaoIndividualFormController);
		//seleciona a ultima lactacao
		tableLactacoes.getSelectionModel().clearAndSelect(tableLactacoes.getItems().size()-1);
	}
	
	private void selectRowLactacaoTableHandler(Lactacao newValue) {
    	if ( newValue != null && data != null ){
    		lactacao = newValue;
    		data.clear();
    		data.addAll(((ProducaoIndividualService)service).findByAnimalPeriodo(animal, lactacao.getDataInicio(), lactacao.getDataFim()));
    		((ProducaoIndividualService)service).atualizaValorProducao(data);
    		lineChart.getData().clear();
    		lineChart.getData().addAll(((ProducaoIndividualService)service).getDataChart(animal, lactacao.getDataInicio(), lactacao.getDataFim()));
    	}
	}

	@Override
	public void handleNew() {
		if ( lactacao != null && lactacao.getId() > 0 ){
			setObject(new ProducaoIndividual(animal, lactacao));
			producaoIndividualFormController.setRefreshObjectInTableView(refreshObjectInTableView);
			producaoIndividualFormController.setState(State.INSERT);
			producaoIndividualFormController.setObject(getObject());
			producaoIndividualFormController.showForm();
		}else{
			CustomAlert.mensagemInfo("É necessário selecionar uma lactação para o registro da produção do animal.");
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
	 * para ser possível atualizar o gráfico.
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
				table.getSelectionModel().clearAndSelect(tableLactacoes.getItems().size()-1);
				lactacao = tableLactacoes.getItems().get(tableLactacoes.getItems().size()-1);
				refreshTableOverview();
			}else{
				return;
			}
		}else{
			data.addAll(((ProducaoIndividualService)service).findByAnimalPeriodo(animal, lactacao.getDataInicio(), lactacao.getDataFim()));
		}
		
		((ProducaoIndividualService)service).atualizaValorProducao(data);
		
		lineChart.getData().clear();
		lineChart.getData().addAll(((ProducaoIndividualService)service).getDataChart(animal, lactacao.getDataInicio(), lactacao.getDataFim()));
		
		//recarrega as lactações para atualizar a média de produção do período
		tableLactacoes.setItems(lactacaoService.findLactacoesAnimal(animal));
		//tableLactacoes.getSelectionModel().select(lactacao);
		
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
		return "Produção Individual";
	}
	
	@Override
	@Resource(name = "producaoIndividualService")
	protected void setService(IService<Integer, ProducaoIndividual> service) {
		super.setService(service);
	}

}
