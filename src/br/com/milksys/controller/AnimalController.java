package br.com.milksys.controller;

import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Animal;
import br.com.milksys.model.FinalidadeAnimal;
import br.com.milksys.model.Raca;
import br.com.milksys.model.Semen;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.SituacaoAnimal;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;
import br.com.milksys.service.RacaService;
import br.com.milksys.service.SituacaoAnimalService;
import br.com.milksys.service.searchers.SearchFemeas30DiasLactacao;
import br.com.milksys.service.searchers.SearchFemeasAtivas;
import br.com.milksys.service.searchers.SearchFemeasCobertas;
import br.com.milksys.service.searchers.SearchFemeasNaoCobertas;
import br.com.milksys.service.searchers.SearchMachosAtivos;
import br.com.milksys.service.searchers.SearchReprodutoresAtivos;

@Controller
public class AnimalController extends AbstractController<Integer, Animal> {

	@FXML private TableColumn<Animal, String> nomeColumn;
	@FXML private TableColumn<Animal, String> numeroColumn;
	
	@FXML private TableColumn<Animal, Date> dataNascimentoColumn;
	@FXML private TableColumn<Animal, Date> dataUltimoPartoColumn;
	@FXML private TableColumn<Animal, String> diasUltimoPartoColumn;
	
	@FXML private TableColumn<Animal, Date> dataUltimaCoberturaColumn;
	@FXML private TableColumn<Animal, String> diasUltimaCoberturaColumn;
	@FXML private TableColumn<Animal, Date> dataPrevisaoProximoPartoColumn;
	@FXML private TableColumn<Animal, String> situacaoUltimaCoberturaColumn;
	
	@FXML private TableColumn<Raca, String> racaColumn;
	@FXML private TableColumn<String, String> sexoColumn;
	@FXML private TableColumn<SituacaoAnimal, String> situacaoAnimalColumn;
	
	@FXML private UCTextField inputNumero, inputNome, inputMae, inputPaiMontaNatural, inputPaiEnseminacaoArtificial;
	@FXML private DatePicker inputDataNascimento;
	@FXML private ComboBox<Raca> inputRaca;
	@FXML private ComboBox<String> inputSituacaoAnimal, inputFinalidadeAnimal, inputSexo;
	@FXML private Button btnRemoverMae, btnRemoverPaiMontaNatural, btnRemoverPaiEnseminacaoArtificial;
	
	//services
	@Autowired private RacaService racaService;
	@Autowired private SituacaoAnimalService situacaoAnimalService;
	
	//controllers
	@Autowired private RacaController racaController;
	@Autowired private SituacaoAnimalController situacaoAnimalController;
	@Autowired private AnimalReducedController animalReducedController;
	@Autowired private SemenReducedController semenReducedController;
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			nomeColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("nome"));
			numeroColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
			
			dataNascimentoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataNascimento"));
			dataUltimoPartoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataUltimoParto"));
			diasUltimoPartoColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("diasUltimoParto"));
			
			dataUltimaCoberturaColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataUltimaCobertura"));
			diasUltimaCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("diasUltimaCobertura"));
			dataPrevisaoProximoPartoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataPrevisaoProximoParto"));
			situacaoUltimaCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("situacaoUltimaCobertura"));

			racaColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("raca"));
			sexoColumn.setCellValueFactory(new PropertyValueFactory<String,String>("sexo"));
			
			super.initialize();

		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || 
				state.equals(State.INSERT_TO_SELECT) || state.equals(State.CREATE_TO_SELECT) ){
			
			inputNumero.textProperty().bindBidirectional(getObject().numeroProperty());
			inputNome.textProperty().bindBidirectional(getObject().nomeProperty());
			inputDataNascimento.valueProperty().bindBidirectional(getObject().dataNascimentoProperty());
			
			inputRaca.setItems(racaService.findAllAsObservableList());
			inputRaca.getSelectionModel().selectFirst();
			inputRaca.valueProperty().bindBidirectional(getObject().racaProperty());
			
			inputSexo.setItems(Sexo.getItems());
			inputSexo.getSelectionModel().select(0);
			inputSexo.valueProperty().bindBidirectional(getObject().sexoProperty());
			
			inputFinalidadeAnimal.setItems(FinalidadeAnimal.getItems());
			inputFinalidadeAnimal.getSelectionModel().select(0);
			inputFinalidadeAnimal.valueProperty().bindBidirectional(getObject().finalidadeAnimalProperty());
			
			if ( getObject().getMae() != null ){
				inputMae.textProperty().set(getObject().getMae().getNumeroNome());
				btnRemoverMae.setVisible(true);
			}
			
			if ( getObject().getPaiMontaNatural() != null ){
				inputPaiMontaNatural.textProperty().set(getObject().getPaiMontaNatural().getNumeroNome());
				btnRemoverPaiMontaNatural.setVisible(true);
			}
			
			if ( getObject().getPaiEnseminacaoArtificial() != null ){
				inputPaiEnseminacaoArtificial.textProperty().set(getObject().getPaiEnseminacaoArtificial().getTouro());
				btnRemoverPaiEnseminacaoArtificial.setVisible(true);
			}
			
		}
		
		if (  state.equals(State.INSERT_TO_SELECT) ){
			if ( getControllerOrigin().equals(CoberturaController.class) ){
				inputSexo.setDisable(true);
				if ( getObject() != null && getObject().getSexo() != null && getObject().getSexo().equals(Sexo.MACHO) ){
					inputFinalidadeAnimal.setDisable(true);
				}
			}
		}
		
	}

	@Override
	protected String getFormName() {
		return "view/animal/AnimalForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Animal";
	}
	
	@Override
	protected Animal getObject() {
		return (Animal) super.object;
	}

	@Override
	@Resource(name = "animalService")
	protected void setService(IService<Integer, Animal> service) {
		super.setService(service);
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

	//-----------------------------------------------------
	
	@FXML
	private void handleSelecionarMae() {
		
		animalReducedController.setObject(new Animal(Sexo.FEMEA));
		//animalReducedController.setSearch(searchFemeasAtivas);
		
		animalReducedController.getFormConfig().put(AbstractController.NEW_DISABLED, true);
		animalReducedController.getFormConfig().put(AbstractController.EDIT_DISABLED, true);
		animalReducedController.getFormConfig().put(AbstractController.REMOVE_DISABLED, true);

		animalReducedController.showForm(animalReducedController.getFormName());
		
		if ( animalReducedController.getObject() != null && animalReducedController.getObject().getId() > 0 ){
			getObject().setMae(animalReducedController.getObject());
		}
		
		if ( getObject().getMae() != null ){
			inputMae.textProperty().set(getObject().getMae().getNumeroNome());	
		}else{
			inputMae.textProperty().set("");
		}
		
		btnRemoverMae.setVisible(getObject().getMae() != null);
		
	}
	
	@FXML
	private void handleSelecionarPaiMontaNatural() {
		
		Animal animalAux = getObject();
		
		animalReducedController.setObject(new Animal(Sexo.MACHO));
		//animalReducedController.setSearch(searchFemeasAtivas);
		animalReducedController.getFormConfig().put(AbstractController.NEW_DISABLED, true);
		animalReducedController.getFormConfig().put(AbstractController.EDIT_DISABLED, true);
		animalReducedController.getFormConfig().put(AbstractController.REMOVE_DISABLED, true);

		animalReducedController.showForm(animalReducedController.getFormName());
		
		setObject(animalAux);
		
		if ( animalReducedController.getObject() != null && animalReducedController.getObject().getId() > 0 ){
			getObject().setPaiMontaNatural(animalReducedController.getObject());
		}
		
		if ( getObject().getPaiMontaNatural() != null ){
			inputPaiMontaNatural.textProperty().set(getObject().getPaiMontaNatural().getNumeroNome());	
		}else{
			inputPaiMontaNatural.textProperty().set("");
		}
		btnRemoverPaiMontaNatural.setVisible(getObject().getPaiMontaNatural() != null);
	}
	
	@FXML
	private void handleSelecionarPaiEnseminacaoArtificial() {
		
		semenReducedController.setObject(new Semen());
		semenReducedController.showForm(semenReducedController.getFormName());
		
		if ( semenReducedController.getObject() != null && semenReducedController.getObject().getId() > 0 ){
			getObject().setPaiEnseminacaoArtificial(semenReducedController.getObject());
		}
		
		if ( getObject().getPaiEnseminacaoArtificial() != null ){
			inputPaiEnseminacaoArtificial.textProperty().set(getObject().getPaiEnseminacaoArtificial().getTouro());	
		}else{
			inputPaiEnseminacaoArtificial.textProperty().set("");
		}
		btnRemoverPaiEnseminacaoArtificial.setVisible(getObject().getPaiEnseminacaoArtificial() != null);
		
	}
	
	@FXML
	private void removerMae(){
		getObject().setMae(null);
		inputMae.setText("");
		btnRemoverMae.setVisible(false);
	}
	
	@FXML
	private void removerPaiMontaNatural(){
		getObject().setPaiMontaNatural(null);
		inputPaiMontaNatural.setText("");
		btnRemoverPaiMontaNatural.setVisible(false);
	}
	
	@FXML
	private void removerPaiEnseminacaoArtificial(){
		getObject().setPaiEnseminacaoArtificial(null);
		inputPaiEnseminacaoArtificial.setText("");
		btnRemoverPaiEnseminacaoArtificial.setVisible(false);
	}
	
	@FXML
	private void cadastrarNovaRaca() {
		racaController.state = State.INSERT_TO_SELECT;
		racaController.object = new Raca();
		racaController.showForm(null);
		if ( racaController.getObject() != null && racaController.getObject().getId() > 0 ){
			inputRaca.getItems().add(racaController.getObject());
			inputRaca.getSelectionModel().select(racaController.getObject());
		}
	}
	
}
