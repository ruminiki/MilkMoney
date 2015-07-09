package br.com.milksys.controller.animal;

import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.components.CustomAlert;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.controller.morteAnimal.MorteAnimalFormController;
import br.com.milksys.controller.raca.RacaOverviewController;
import br.com.milksys.controller.semen.SemenReducedOverviewController;
import br.com.milksys.controller.vendaAnimal.VendaAnimalFormController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.MorteAnimal;
import br.com.milksys.model.Raca;
import br.com.milksys.model.SituacaoAnimal;
import br.com.milksys.model.VendaAnimal;
import br.com.milksys.service.IService;
import br.com.milksys.service.RacaService;
import br.com.milksys.service.searchers.SearchFemeas30DiasLactacao;
import br.com.milksys.service.searchers.SearchFemeasAtivas;
import br.com.milksys.service.searchers.SearchFemeasCobertas;
import br.com.milksys.service.searchers.SearchFemeasNaoCobertas;
import br.com.milksys.service.searchers.SearchMachosAtivos;
import br.com.milksys.service.searchers.SearchReprodutoresAtivos;
import br.com.milksys.validation.VendaAnimalValidation;

@Controller
public class AnimalOverviewController extends AbstractOverviewController<Integer, Animal> {

	@FXML private TableColumn<Animal, String> nomeColumn;
	@FXML private TableColumn<Animal, String> numeroColumn;
	
	@FXML private TableColumn<Animal, Date> dataNascimentoColumn;
	@FXML private TableColumn<Animal, Date> dataUltimoPartoColumn;
	@FXML private TableColumn<Animal, String> diasUltimoPartoColumn;
	
	@FXML private TableColumn<Animal, Date> dataUltimaCoberturaColumn;
	@FXML private TableColumn<Animal, String> diasUltimaCoberturaColumn;
	@FXML private TableColumn<Animal, Date> dataPrevisaoSecagemColumn;
	@FXML private TableColumn<Animal, Date> dataPrevisaoProximoPartoColumn;
	@FXML private TableColumn<Animal, String> situacaoUltimaCoberturaColumn;
	
	@FXML private TableColumn<Raca, String> racaColumn;
	@FXML private TableColumn<String, String> sexoColumn;
	@FXML private TableColumn<SituacaoAnimal, String> situacaoAnimalColumn;
	
	//services
	@Autowired private RacaService racaService;
	
	//controllers
	@Autowired private RacaOverviewController racaController;
	@Autowired private AnimalReducedOverviewController animalReducedController;
	@Autowired private SemenReducedOverviewController semenReducedController;
	@Autowired private MorteAnimalFormController morteAnimalFormController;
	@Autowired private VendaAnimalFormController vendaAnimalFormController;
	
	@FXML
	public void initialize() {
		
		nomeColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("nome"));
		numeroColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
		
		dataNascimentoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataNascimento"));
		dataUltimoPartoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataUltimoParto"));
		diasUltimoPartoColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("diasUltimoParto"));
		
		dataUltimaCoberturaColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataUltimaCobertura"));
		diasUltimaCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("diasUltimaCobertura"));
		dataPrevisaoSecagemColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataPrevisaoSecagem"));
		dataPrevisaoProximoPartoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataPrevisaoProximoParto"));
		situacaoUltimaCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("situacaoUltimaCobertura"));

		racaColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("raca"));
		sexoColumn.setCellValueFactory(new PropertyValueFactory<String,String>("sexo"));
		
		super.initialize((AnimalFormController)MainApp.getBean(AnimalFormController.class));
		
	}

	@Override
	protected String getFormTitle() {
		return "Animal";
	}
	
	@Override
	protected String getFormName() {
		return "view/animal/AnimalOverview.fxml";
	}
	
	@Override
	@Resource(name = "animalService")
	protected void setService(IService<Integer, Animal> service) {
		super.setService(service);
	}
	
	@FXML
	private void registrarMorteAnimal() {
		
		if ( table.getSelectionModel().getSelectedItem() != null ){
			
			VendaAnimalValidation.validaSituacaoAnimal(getObject());
			
			morteAnimalFormController.setObject(new MorteAnimal(getObject()));
			morteAnimalFormController.showForm();
			if ( morteAnimalFormController.getObject() != null && morteAnimalFormController.getObject().getId() > 0 ){
				getObject().setSituacaoAnimal(SituacaoAnimal.MORTO);
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		
	}
	
	@FXML
	private void registrarVendaAnimal() {
		
		if ( table.getSelectionModel().getSelectedItem() != null ){
			
			vendaAnimalFormController.getAnimalVendido().setAnimal(getObject());
			vendaAnimalFormController.setObject(new VendaAnimal());
			vendaAnimalFormController.showForm();
			if ( vendaAnimalFormController.getObject() != null && vendaAnimalFormController.getObject().getId() > 0 ){
				getObject().setSituacaoAnimal(SituacaoAnimal.VENDIDO);
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		
	}
	
	
	//-------------FILTRO RÁPIDO----------------------------------
	
	@FXML
	private void handleFindFemeas(){
		setSearch((SearchFemeasAtivas)MainApp.getBean(SearchFemeasAtivas.class));
		refreshTableOverview();
	}
	
	@FXML
	private void handleFindMachos(){
		setSearch((SearchMachosAtivos)MainApp.getBean(SearchMachosAtivos.class));
		refreshTableOverview();
	}
	
	@FXML
	private void handleFindReprodutores(){
		setSearch((SearchReprodutoresAtivos)MainApp.getBean(SearchReprodutoresAtivos.class));
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
	private void handleLimpar(){
		setSearch(null);
		refreshTableOverview();
	}

}
