package br.com.milksys.controller.animal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.controller.raca.RacaFormController;
import br.com.milksys.controller.semen.SemenReducedOverviewController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.FinalidadeAnimal;
import br.com.milksys.model.Raca;
import br.com.milksys.model.Semen;
import br.com.milksys.model.Sexo;
import br.com.milksys.service.IService;
import br.com.milksys.service.RacaService;

@Controller
public class AnimalFormController extends AbstractFormController<Integer, Animal> {

	@FXML private UCTextField inputNumero, inputNome, inputMae, inputPaiMontaNatural, inputPaiEnseminacaoArtificial;
	@FXML private DatePicker inputDataNascimento;
	@FXML private ComboBox<Raca> inputRaca;
	@FXML private ComboBox<String> inputSituacaoAnimal, inputFinalidadeAnimal, inputSexo;
	@FXML private Button btnRemoverMae, btnRemoverPaiMontaNatural, btnRemoverPaiEnseminacaoArtificial;
	@FXML private Button btnBuscarMae, btnBuscarPaiMontaNatural, btnBuscarPaiEnseminacaoArtificial;
	//services
	@Autowired private RacaService racaService;
	
	//controllers
	@Autowired private RacaFormController racaFormController;
	@Autowired private AnimalReducedOverviewController animalReducedOverviewController;
	@Autowired private SemenReducedOverviewController semenReducedOverviewController;
	
	@FXML
	public void initialize() {
		
		inputNumero.textProperty().bindBidirectional(getObject().numeroProperty());
		inputNome.textProperty().bindBidirectional(getObject().nomeProperty());
		inputDataNascimento.valueProperty().bindBidirectional(getObject().dataNascimentoProperty());
		
		inputRaca.setItems(racaService.findAllAsObservableList());
		inputRaca.getSelectionModel().selectFirst();
		inputRaca.valueProperty().bindBidirectional(getObject().racaProperty());
		
		inputSexo.setItems(Sexo.getItems());
		inputSexo.valueProperty().bindBidirectional(getObject().sexoProperty());
		
		inputFinalidadeAnimal.setItems(FinalidadeAnimal.getItems());
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
		
		// impede que sejam alteradas informações cadastradas pela cobertura e parto
		if ( getObject().isNascimentoCadastrado() ){
			inputMae.setDisable(true);
			inputPaiEnseminacaoArtificial.setDisable(true);
			inputPaiMontaNatural.setDisable(true);
			btnBuscarMae.setDisable(true);
			btnBuscarPaiEnseminacaoArtificial.setDisable(true);
			btnBuscarPaiMontaNatural.setDisable(true);
			btnRemoverMae.setDisable(true);
			btnRemoverPaiEnseminacaoArtificial.setDisable(true);
			btnRemoverPaiMontaNatural.setDisable(true);
			inputDataNascimento.setDisable(true);
			inputSexo.setDisable(true);
		}
		
	}

	@FXML
	private void handleSelecionarMae() {
		
		animalReducedOverviewController.setObject(new Animal(Sexo.FEMEA));
		//animalReducedController.setSearch(searchFemeasAtivas);
		
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, true);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, true);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.REMOVE_DISABLED, true);

		animalReducedOverviewController.showForm();
		
		if ( animalReducedOverviewController.getObject() != null && animalReducedOverviewController.getObject().getId() > 0 ){
			getObject().setMae(animalReducedOverviewController.getObject());
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
		
		animalReducedOverviewController.setObject(new Animal(Sexo.MACHO));
		//animalReducedController.setSearch(searchFemeasAtivas);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, true);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, true);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.REMOVE_DISABLED, true);

		animalReducedOverviewController.showForm();
		
		setObject(animalAux);
		
		if ( animalReducedOverviewController.getObject() != null && animalReducedOverviewController.getObject().getId() > 0 ){
			getObject().setPaiMontaNatural(animalReducedOverviewController.getObject());
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
		
		semenReducedOverviewController.setObject(new Semen());
		semenReducedOverviewController.showForm();
		
		if ( semenReducedOverviewController.getObject() != null && semenReducedOverviewController.getObject().getId() > 0 ){
			getObject().setPaiEnseminacaoArtificial(semenReducedOverviewController.getObject());
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
		racaFormController.setObject(new Raca());
		racaFormController.showForm();
		if ( racaFormController.getObject() != null && racaFormController.getObject().getId() > 0 ){
			inputRaca.getItems().add(racaFormController.getObject());
			inputRaca.getSelectionModel().select(racaFormController.getObject());
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
