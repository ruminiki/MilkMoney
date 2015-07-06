package br.com.milksys.controller.animal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractController;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.controller.raca.RacaOverviewController;
import br.com.milksys.controller.semen.SemenReducedOverviewController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.FinalidadeAnimal;
import br.com.milksys.model.Raca;
import br.com.milksys.model.Semen;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;
import br.com.milksys.service.RacaService;

@Controller
public class AnimalFormController extends AbstractFormController<Integer, Animal> {

	@FXML private UCTextField inputNumero, inputNome, inputMae, inputPaiMontaNatural, inputPaiEnseminacaoArtificial;
	@FXML private DatePicker inputDataNascimento;
	@FXML private ComboBox<Raca> inputRaca;
	@FXML private ComboBox<String> inputSituacaoAnimal, inputFinalidadeAnimal, inputSexo;
	@FXML private Button btnRemoverMae, btnRemoverPaiMontaNatural, btnRemoverPaiEnseminacaoArtificial;
	
	//services
	@Autowired private RacaService racaService;
	
	//controllers
	@Autowired private RacaOverviewController racaController;
	@Autowired private AnimalReducedOverviewController animalReducedController;
	@Autowired private SemenReducedOverviewController semenReducedController;
	
	@FXML
	public void initialize() {
		
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
		
		/*if ( getControllerOrigin().equals(CoberturaOverviewController.class) ){
			inputSexo.setDisable(true);
			if ( getObject() != null && getObject().getSexo() != null && getObject().getSexo().equals(Sexo.MACHO) ){
				inputFinalidadeAnimal.setDisable(true);
			}
		}*/
		
	}

	@FXML
	private void handleSelecionarMae() {
		
		animalReducedController.setObject(new Animal(Sexo.FEMEA));
		//animalReducedController.setSearch(searchFemeasAtivas);
		
		animalReducedController.getFormConfig().put(AbstractController.NEW_DISABLED, true);
		animalReducedController.getFormConfig().put(AbstractController.EDIT_DISABLED, true);
		animalReducedController.getFormConfig().put(AbstractController.REMOVE_DISABLED, true);

		animalReducedController.showForm();
		
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

		animalReducedController.showForm();
		
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
		semenReducedController.showForm();
		
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
		racaController.setState(State.INSERT_TO_SELECT);
		racaController.setObject(new Raca());
		//racaController.showForm(null);
		if ( racaController.getObject() != null && racaController.getObject().getId() > 0 ){
			inputRaca.getItems().add(racaController.getObject());
			inputRaca.getSelectionModel().select(racaController.getObject());
		}
	}
	
	@Override
	public String getFormName() {
		return "view/animal/AnimalForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Animal";
	}
	
	@Override
	@Resource(name = "animalService")
	protected void setService(IService<Integer, Animal> service) {
		super.setService(service);
	}

	
}
