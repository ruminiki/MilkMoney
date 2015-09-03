package br.com.milkmoney.controller.animal;

import java.util.Date;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.cobertura.CoberturaOverviewController;
import br.com.milkmoney.controller.fichaAnimal.FichaAnimalOverviewController;
import br.com.milkmoney.controller.lactacao.LactacaoFormController;
import br.com.milkmoney.controller.morteAnimal.MorteAnimalFormController;
import br.com.milkmoney.controller.producaoIndividual.ProducaoIndividualOverviewController;
import br.com.milkmoney.controller.raca.RacaOverviewController;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.controller.root.RootLayoutController;
import br.com.milkmoney.controller.vendaAnimal.VendaAnimalFormController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.MorteAnimal;
import br.com.milkmoney.model.Raca;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.model.SituacaoAnimal;
import br.com.milkmoney.model.VendaAnimal;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.LactacaoService;
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
import br.com.milkmoney.validation.MorteAnimalValidation;
import br.com.milkmoney.validation.VendaAnimalValidation;

@Controller
public class AnimalOverviewController extends AbstractOverviewController<Integer, Animal> {

	@FXML private TableColumn<Animal, String> nomeColumn;
	@FXML private TableColumn<Animal, String> numeroColumn;
	
	@FXML private TableColumn<Animal, Date> dataNascimentoColumn;
	@FXML private TableColumn<Animal, Date> dataUltimoPartoColumn;
	@FXML private TableColumn<Animal, String> diasUltimoPartoColumn;
	
	@FXML private TableColumn<Animal, Date> dataUltimaCoberturaColumn;
	@FXML private TableColumn<Animal, String> diasUltimaCoberturaColumn;
	@FXML private TableColumn<Animal, Date> dataPrevisaoLactacaoColumn;
	@FXML private TableColumn<Animal, Date> dataPrevisaoProximoPartoColumn;
	@FXML private TableColumn<Animal, String> situacaoUltimaCoberturaColumn;
	
	@FXML private TableColumn<Raca, String> racaColumn;
	@FXML private TableColumn<String, String> sexoColumn;
	@FXML private TableColumn<Animal, String> situacaoAnimalColumn;
	@FXML private TableColumn<Animal, Long> idadeColumn;
	
	@FXML private HBox containerTable;
	
	//services
	@Autowired private LactacaoService lactacaoService;
	@Autowired private MorteAnimalService morteAnimalService;
	@Autowired private VendaAnimalService vendaAnimalService;
	@Autowired private RelatorioService relatorioService;
	
	//controllers
	@Autowired private RacaOverviewController racaController;
	@Autowired private AnimalReducedOverviewController animalReducedController;
	@Autowired private MorteAnimalFormController morteAnimalFormController;
	@Autowired private VendaAnimalFormController vendaAnimalFormController;
	@Autowired private LactacaoFormController lactacaoFormController;
	@Autowired private CoberturaOverviewController coberturaOverviewController;
	@Autowired private FichaAnimalOverviewController fichaAnimalOverviewController;
	@Autowired private ProducaoIndividualOverviewController producaoIndividualOverviewController;
	@Autowired private RootLayoutController rootLayoutController;
	
	private MenuItem encerramentoLactacaoMenuItem = new MenuItem("Encerrar Lactação");
	private MenuItem registrarMorteMenuItem               = new MenuItem();
	private MenuItem registrarVendaMenuItem               = new MenuItem();
	private MenuItem registroProducaoIndividualMenuItem   = new MenuItem("Registro Produção Individual");
	private MenuItem fichaAnimalMenuItem                  = new MenuItem("Ficha Animal");
	
	@FXML
	public void initialize() {
		
		situacaoAnimalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("situacaoAnimal"));
		nomeColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("nome"));
		numeroColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
		dataNascimentoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataNascimento"));
		idadeColumn.setCellValueFactory(new PropertyValueFactory<Animal,Long>("idade"));
		
		dataUltimoPartoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataUltimoParto"));
		diasUltimoPartoColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("diasUltimoParto"));
		
		dataUltimaCoberturaColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataUltimaCobertura"));
		diasUltimaCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("diasUltimaCobertura"));
		dataPrevisaoLactacaoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataPrevisaoEncerramentoLactacao"));
		dataPrevisaoProximoPartoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataPrevisaoProximoParto"));
		situacaoUltimaCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("situacaoUltimaCobertura"));

		racaColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("raca"));
		sexoColumn.setCellValueFactory(new PropertyValueFactory<String,String>("sexo"));
		
		super.initialize((AnimalFormController)MainApp.getBean(AnimalFormController.class));
		
		encerramentoLactacaoMenuItem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	handleDesfazerOuEncerrarLactacao();
		    }
		});
		
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
		
		registroProducaoIndividualMenuItem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	handleOpenMarcacoesLeiteAnimal();
		    }
		});
		
		fichaAnimalMenuItem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	handleOpenFichaAnimal();
		    }
		});
		
		getContextMenu().getItems().addAll(new SeparatorMenuItem(), encerramentoLactacaoMenuItem, 
				registrarMorteMenuItem, registrarVendaMenuItem, new SeparatorMenuItem(), registroProducaoIndividualMenuItem, fichaAnimalMenuItem);
		
	}
	
	@Override
	protected void handleRightClick() {
		super.handleRightClick();
		
		encerramentoLactacaoMenuItem.setDisable(getObject().getSexo().equals(Sexo.MACHO) || !(getObject().getSituacaoAnimal().equals(SituacaoAnimal.SECO) || getObject().getSituacaoAnimal().equals(SituacaoAnimal.EM_LACTACAO)));
		encerramentoLactacaoMenuItem.setText(getObject().getSituacaoAnimal().equals(SituacaoAnimal.SECO) ? "Desfazer Encerramento Lactação" : "Encerrar Lactação");
		registroProducaoIndividualMenuItem.setDisable(getObject().getSexo().equals(Sexo.MACHO));
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
	
	private void handleOpenMarcacoesLeiteAnimal(){
		
		producaoIndividualOverviewController.setAnimal(getObject());
		producaoIndividualOverviewController.showForm();
		
	}
	
	private void handleOpenFichaAnimal(){
		
		fichaAnimalOverviewController.setAnimal(getObject());
		fichaAnimalOverviewController.showForm();
		
	}
	
	private void handleDesfazerOuEncerrarLactacao(){
		
		if ( table.getSelectionModel().getSelectedItem() != null ){
			
			if ( getObject().getSituacaoAnimal().equals(SituacaoAnimal.SECO) ){
			
				Optional<ButtonType> result = CustomAlert.confirmar("Desfazer Encerramento Lactação", "Tem certeza que deseja desfazer o encerramento da última lactação?");
				if (result.get() == ButtonType.OK) {
					lactacaoService.desfazerEncerramentoLactacao(getObject());
					refreshObjectInTableView.apply(service.findById(getObject().getId()));
				}
				
			}else{
				if ( getObject().getSituacaoAnimal().equals(SituacaoAnimal.EM_LACTACAO) ){
					lactacaoFormController.setObject(lactacaoService.findUltimaLactacaoAnimal(getObject()));
					lactacaoFormController.showForm();
					refreshObjectInTableView.apply(service.findById(getObject().getId()));
				}
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		
	};
	
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
	@FXML
	private void handleCoberturas(){
		coberturaOverviewController.setObject(new Cobertura());
		coberturaOverviewController.showForm();
		refreshTableOverview();
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
