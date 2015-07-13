package br.com.milksys.controller.animal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.controller.raca.RacaReducedOverviewController;
import br.com.milksys.controller.touro.TouroReducedOverviewController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.FinalidadeAnimal;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.Touro;
import br.com.milksys.service.IService;

@Controller
public class AnimalFormController extends AbstractFormController<Integer, Animal> {

	@FXML private UCTextField inputNumero, inputNome, inputMae, inputPaiMontaNatural, inputPaiEnseminacaoArtificial, inputValor, inputRaca;
	@FXML private DatePicker inputDataNascimento;
	@FXML private ComboBox<String> inputSituacaoAnimal, inputFinalidadeAnimal, inputSexo;
	@FXML private Button btnRemoverMae, btnRemoverPaiMontaNatural, btnRemoverPaiEnseminacaoArtificial;
	@FXML private Button btnBuscarMae, btnBuscarPaiMontaNatural, btnBuscarPaiEnseminacaoArtificial;

	//controllers
	@Autowired private AnimalReducedOverviewController animalReducedOverviewController;
	@Autowired private TouroReducedOverviewController touroReducedOverviewController;
	@Autowired private RacaReducedOverviewController racaReducedOverviewController;
	
	@FXML
	public void initialize() {
		
		inputNumero.textProperty().bindBidirectional(getObject().numeroProperty());
		inputNome.textProperty().bindBidirectional(getObject().nomeProperty());
		inputDataNascimento.valueProperty().bindBidirectional(getObject().dataNascimentoProperty());
		inputValor.textProperty().bindBidirectional(getObject().valorProperty());
		
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
			inputPaiEnseminacaoArtificial.textProperty().set(getObject().getPaiEnseminacaoArtificial().toString());
			btnRemoverPaiEnseminacaoArtificial.setVisible(true);
		}
		
		if ( getObject().getRaca() != null ){
			inputRaca.textProperty().set(getObject().getRaca().toString());
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
		
		MaskFieldUtil.decimal(inputValor);
		
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
		
		touroReducedOverviewController.setObject(new Touro());
		touroReducedOverviewController.showForm();
		
		if ( touroReducedOverviewController.getObject() != null && touroReducedOverviewController.getObject().getId() > 0 ){
			getObject().setPaiEnseminacaoArtificial(touroReducedOverviewController.getObject());
		}
		
		if ( getObject().getPaiEnseminacaoArtificial() != null ){
			inputPaiEnseminacaoArtificial.textProperty().set(getObject().getPaiEnseminacaoArtificial().toString());	
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
	private void handleSelecionarRaca() {
		
		racaReducedOverviewController.setObject(new Touro());
		racaReducedOverviewController.showForm();
		
		if ( racaReducedOverviewController.getObject() != null && racaReducedOverviewController.getObject().getId() > 0 ){
			getObject().setRaca(racaReducedOverviewController.getObject());
		}
		
		if ( getObject().getRaca() != null ){
			inputRaca.textProperty().set(getObject().getRaca().toString());	
		}else{
			inputRaca.textProperty().set("");
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
