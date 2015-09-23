package br.com.milkmoney.controller.animal;

import java.util.Date;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
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
import br.com.milkmoney.controller.fichaAnimal.FichaAnimalOverviewController;
import br.com.milkmoney.controller.morteAnimal.MorteAnimalFormController;
import br.com.milkmoney.controller.producaoIndividual.ProducaoIndividualOverviewController;
import br.com.milkmoney.controller.raca.RacaOverviewController;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.controller.root.RootLayoutController;
import br.com.milkmoney.controller.vendaAnimal.VendaAnimalFormController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.model.MorteAnimal;
import br.com.milkmoney.model.Raca;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.model.SituacaoAnimal;
import br.com.milkmoney.model.VendaAnimal;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.FichaAnimalService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.MorteAnimalService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.service.VendaAnimalService;
import br.com.milkmoney.service.searchers.SearchAnimaisMortos;
import br.com.milkmoney.service.searchers.SearchAnimaisVendidos;
import br.com.milkmoney.service.searchers.SearchFemeas30DiasLactacao;
import br.com.milkmoney.service.searchers.SearchFemeas60DiasLactacao;
import br.com.milkmoney.service.searchers.SearchFemeasASecar;
import br.com.milkmoney.service.searchers.SearchFemeasAtivas;
import br.com.milkmoney.service.searchers.SearchFemeasCobertas;
import br.com.milkmoney.service.searchers.SearchFemeasEmLactacao;
import br.com.milkmoney.service.searchers.SearchFemeasMais60DiasLactacao;
import br.com.milkmoney.service.searchers.SearchFemeasNaoCobertas;
import br.com.milkmoney.service.searchers.SearchFemeasSecas;
import br.com.milkmoney.service.searchers.SearchMachos;
import br.com.milkmoney.service.searchers.SearchReprodutoresAtivos;
import br.com.milkmoney.util.DateUtil;
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
	@FXML private Label lblNumeroServicos, lblDataUltimaCobertura, lblProximoServico, lblNumeroPartos, lblIdadePrimeiroParto, 
						lblIdadePrimeiraCobertura, lblDiasEmLactacao, lblDiasEmAberto, lblIntervaloPrimeiroParto, lblDataSecar;
	@FXML private VBox vBoxChart;
	
	//services
	@Autowired private MorteAnimalService morteAnimalService;
	@Autowired private VendaAnimalService vendaAnimalService;
	@Autowired private FichaAnimalService fichaAnimalService;
	@Autowired private RelatorioService relatorioService;
	
	//controllers
	@Autowired private RacaOverviewController racaController;
	@Autowired private AnimalReducedOverviewController animalReducedController;
	@Autowired private MorteAnimalFormController morteAnimalFormController;
	@Autowired private VendaAnimalFormController vendaAnimalFormController;
	@Autowired private FichaAnimalOverviewController fichaAnimalOverviewController;
	@Autowired private ProducaoIndividualOverviewController producaoIndividualOverviewController;
	@Autowired private RootLayoutController rootLayoutController;
	
	private MenuItem registrarMorteMenuItem               = new MenuItem();
	private MenuItem registrarVendaMenuItem               = new MenuItem();
	
	private PieChart chart;
	
	@FXML
	public void initialize() {
		
		situacaoAnimalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("situacaoAnimal"));
		nomeColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("nome"));
		numeroColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
		dataNascimentoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataNascimento"));
		idadeColumn.setCellValueFactory(new PropertyValueFactory<Animal,Long>("idade"));
		racaColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("raca"));
		sexoColumn.setCellValueFactory(new PropertyValueFactory<String,String>("sexoFormatado"));
		
		super.initialize((AnimalFormController)MainApp.getBean(AnimalFormController.class));
		
		chart = new PieChart(((AnimalService)service).getChartData());
	    chart.setTitle("Situação Animais Rebanho");
        vBoxChart.getChildren().add(chart);
       
		registrarMorteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	handleDesfazerOuRegistrarMorte();
		    }
		});
		
		registrarVendaMenuItem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	handleDesfazeOuRegistrarVenda();
		    }
		});
		
		getContextMenu().getItems().addAll(new SeparatorMenuItem(), 
				registrarMorteMenuItem, registrarVendaMenuItem, new SeparatorMenuItem());
		
	}
	
	@Override
	protected void selectRowTableHandler(Animal animal) {
		super.selectRowTableHandler(animal);
		if ( animal != null ){
			FichaAnimal fichaAnimal = fichaAnimalService.generateFichaAnimal(animal);
			
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
				lblDataSecar.setText(animal.getDataPrevisaoEncerramentoLactacao() != null ? DateUtil.format(animal.getDataPrevisaoEncerramentoLactacao()) : "--");
			}
		}
		
	}
	
	@Override
	protected void handleRightClick() {
		super.handleRightClick();
		
		registrarMorteMenuItem.setText(getObject().getSituacaoAnimal().equals(SituacaoAnimal.MORTO) ? "Desfazer Registro Morte" : "Registrar Morte");
		registrarVendaMenuItem.setText(getObject().getSituacaoAnimal().equals(SituacaoAnimal.VENDIDO) ? "Desfazer Registro Venda" : "Registrar Venda");
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
	
	@FXML
	private void handleOpenProducaoAnimal(){
		
		if ( table.getSelectionModel().getSelectedItem() != null ){
			if ( getObject().getSexo().equals(Sexo.FEMEA) ){
				producaoIndividualOverviewController.setAnimal(getObject());
				producaoIndividualOverviewController.showForm();
			}else{
				CustomAlert.mensagemInfo("Somente podem ter registro de produção, animais fêmeas. "
						+ "Por favor, selecione outro animal e tente novamente.");
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		
	}
	
	@FXML
	private void handleOpenFichaAnimal(){
		if ( table.getSelectionModel().getSelectedItem() != null ){
			fichaAnimalOverviewController.setAnimal(getObject());
			fichaAnimalOverviewController.showForm();
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
	}
	
	private void handleDesfazerOuRegistrarMorte(){
		
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
			
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		
	};
	
	private void handleDesfazeOuRegistrarVenda(){
		
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
			
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		
	};
	
	//-------------FILTRO RÁPIDO----------------------------------
	
	@FXML
	private void handleFindFemeas(){
		setSearch((SearchFemeasAtivas)MainApp.getBean(SearchFemeasAtivas.class));
		refreshTableOverview();
	}
	
	@FXML
	private void handleFindMachos(){
		setSearch((SearchMachos)MainApp.getBean(SearchMachos.class));
		refreshTableOverview();
	}
	
	@FXML
	private void handleFindReprodutores(){
		setSearch((SearchReprodutoresAtivos)MainApp.getBean(SearchReprodutoresAtivos.class));
		refreshTableOverview();
	}
	
	@FXML
	private void handleFindFemeasEmLactacao(){
		setSearch((SearchFemeasEmLactacao)MainApp.getBean(SearchFemeasEmLactacao.class));
		refreshTableOverview();
	}
	
	
	@FXML
	private void handleFindFemeasCobertas(){
		setSearch((SearchFemeasCobertas)MainApp.getBean(SearchFemeasCobertas.class));
		refreshTableOverview();
	}
	
	@FXML
	private void handleFindFemeasNaoCobertas(){
		setSearch((SearchFemeasNaoCobertas)MainApp.getBean(SearchFemeasNaoCobertas.class));
		refreshTableOverview();
	}
	
	@FXML
	private void handleFindFemeas30DiasLactacao(){
		setSearch((SearchFemeas30DiasLactacao)MainApp.getBean(SearchFemeas30DiasLactacao.class));
		refreshTableOverview();
	}
	
	@FXML
	private void handleFindFemeas60DiasLactacao(){
		setSearch((SearchFemeas60DiasLactacao)MainApp.getBean(SearchFemeas60DiasLactacao.class));
		refreshTableOverview();
	}
	
	@FXML
	private void handleFindFemeasMais60DiasLactacao(){
		setSearch((SearchFemeasMais60DiasLactacao)MainApp.getBean(SearchFemeasMais60DiasLactacao.class));
		refreshTableOverview();
	}
	
	@FXML
	private void handleFindFemeasASecar(){
		setSearch((SearchFemeasASecar)MainApp.getBean(SearchFemeasASecar.class));
		refreshTableOverview();
	}
	
	@FXML
	private void handleFindFemeasSecas(){
		setSearch((SearchFemeasSecas)MainApp.getBean(SearchFemeasSecas.class));
		refreshTableOverview();
	}
	
	@FXML
	private void handleFindAnimaisVendidos(){
		setSearch((SearchAnimaisVendidos)MainApp.getBean(SearchAnimaisVendidos.class));
		refreshTableOverview();
	}
	
	@FXML
	private void handleFindAnimaisMortos(){
		setSearch((SearchAnimaisMortos)MainApp.getBean(SearchAnimaisMortos.class));
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
		
		rootLayoutController.setMessage("O relatório está sendo executado...");
		
	}
	
}
