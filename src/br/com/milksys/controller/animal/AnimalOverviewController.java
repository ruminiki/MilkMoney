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
import br.com.milksys.controller.encerramentoLactacao.EncerramentoLactacaoFormController;
import br.com.milksys.controller.morteAnimal.MorteAnimalFormController;
import br.com.milksys.controller.raca.RacaOverviewController;
import br.com.milksys.controller.semen.SemenReducedOverviewController;
import br.com.milksys.controller.vendaAnimal.VendaAnimalFormController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.EncerramentoLactacao;
import br.com.milksys.model.MorteAnimal;
import br.com.milksys.model.Raca;
import br.com.milksys.model.SituacaoAnimal;
import br.com.milksys.model.VendaAnimal;
import br.com.milksys.service.IService;
import br.com.milksys.service.RacaService;
import br.com.milksys.service.searchers.SearchAnimaisMortos;
import br.com.milksys.service.searchers.SearchAnimaisVendidos;
import br.com.milksys.service.searchers.SearchFemeas30DiasLactacao;
import br.com.milksys.service.searchers.SearchFemeas60DiasLactacao;
import br.com.milksys.service.searchers.SearchFemeasASecar;
import br.com.milksys.service.searchers.SearchFemeasAtivas;
import br.com.milksys.service.searchers.SearchFemeasCobertas;
import br.com.milksys.service.searchers.SearchFemeasEmLactacao;
import br.com.milksys.service.searchers.SearchFemeasMais60DiasLactacao;
import br.com.milksys.service.searchers.SearchFemeasNaoCobertas;
import br.com.milksys.service.searchers.SearchFemeasSecas;
import br.com.milksys.service.searchers.SearchMachosAtivos;
import br.com.milksys.service.searchers.SearchReprodutoresAtivos;
import br.com.milksys.validation.EncerramentoLactacaoValidation;
import br.com.milksys.validation.MorteAnimalValidation;
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
	@FXML private TableColumn<Animal, Date> dataPrevisaoEncerramentoLactacaoColumn;
	@FXML private TableColumn<Animal, Date> dataPrevisaoProximoPartoColumn;
	@FXML private TableColumn<Animal, String> situacaoUltimaCoberturaColumn;
	
	@FXML private TableColumn<Raca, String> racaColumn;
	@FXML private TableColumn<String, String> sexoColumn;
	@FXML private TableColumn<Animal, String> situacaoAnimalColumn;
	@FXML private TableColumn<Animal, Long> idadeColumn;
	
	//services
	@Autowired private RacaService racaService;
	
	//controllers
	@Autowired private RacaOverviewController racaController;
	@Autowired private AnimalReducedOverviewController animalReducedController;
	@Autowired private SemenReducedOverviewController semenReducedController;
	@Autowired private MorteAnimalFormController morteAnimalFormController;
	@Autowired private VendaAnimalFormController vendaAnimalFormController;
	@Autowired private EncerramentoLactacaoFormController encerramentoLactacaoFormController;
	
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
		dataPrevisaoEncerramentoLactacaoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataPrevisaoEncerramentoLactacao"));
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
			
			MorteAnimalValidation.validaSituacaoAnimal(getObject());
			
			morteAnimalFormController.setObject(new MorteAnimal(getObject()));
			morteAnimalFormController.showForm();
			if ( morteAnimalFormController.getObject() != null && morteAnimalFormController.getObject().getId() > 0 ){
				//getObject().setSituacaoAnimal(new SituacaoAnimal(SituacaoAnimal.MORTO));
				getObject().setSituacaoAnimal(SituacaoAnimal.MORTO);
			}
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		
	}
	
	@FXML
	private void registrarVendaAnimal() {
		
		if ( table.getSelectionModel().getSelectedItem() != null ){
			
			VendaAnimalValidation.validaSituacaoAnimal(getObject());
			
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
	
	@FXML
	private void encerrarLactacao() {
		
		if ( table.getSelectionModel().getSelectedItem() != null ){
			
			EncerramentoLactacaoValidation.validaSituacaoAnimal(getObject());
			
			encerramentoLactacaoFormController.setObject(new EncerramentoLactacao(getObject()));
			encerramentoLactacaoFormController.showForm();
			if ( encerramentoLactacaoFormController.getObject() != null && encerramentoLactacaoFormController.getObject().getId() > 0 ){
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
	
}
