package br.com.milkmoney.controller.animal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.raca.RacaReducedOverviewController;
import br.com.milkmoney.controller.touro.TouroReducedOverviewController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FinalidadeAnimal;
import br.com.milkmoney.model.Raca;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.model.Touro;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.searchers.SearchFemeas;
import br.com.milkmoney.service.searchers.SearchMachos;

@Controller
public class AnimalFormController extends AbstractFormController<Integer, Animal> {

	@FXML private UCTextField inputNumero, inputNome, inputMae, inputPai, inputValor, inputRaca;
	@FXML private DatePicker inputDataNascimento;
	@FXML private ComboBox<String> inputSituacaoAnimal, inputFinalidadeAnimal, inputSexo;
	@FXML private Button btnRemoverMae, btnRemoverPai, btnBuscarMae, btnBuscarPaiMontaNatural, btnBuscarPaiEnseminacaoArtificial;

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
			inputPai.textProperty().set(getObject().getPaiMontaNatural().getNumeroNome());
			btnRemoverPai.setVisible(true);
		}
		
		if ( getObject().getPaiEnseminacaoArtificial() != null ){
			inputPai.textProperty().set(getObject().getPaiEnseminacaoArtificial().toString());
			btnRemoverPai.setVisible(true);
		}
		
		if ( getObject().getRaca() != null ){
			inputRaca.textProperty().set(getObject().getRaca().toString());
		}
		
		// impede que sejam alteradas informações cadastradas pela cobertura e parto
		if ( getObject().isNascimentoCadastrado() ){
			btnBuscarMae.setDisable(true);
			btnBuscarPaiEnseminacaoArtificial.setDisable(true);
			btnBuscarPaiMontaNatural.setDisable(true);
			btnRemoverMae.setDisable(true);
			btnRemoverPai.setDisable(true);
			inputDataNascimento.setDisable(true);
			inputSexo.setDisable(true);
		}
		
		MaskFieldUtil.decimal(inputValor);
		
	}

	@FXML
	private void handleSelecionarMae() {
		
		animalReducedOverviewController.setObject(new Animal(Sexo.FEMEA));
		animalReducedOverviewController.setSearch((SearchFemeas) MainApp.getBean(SearchFemeas.class));
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
		animalReducedOverviewController.setSearch((SearchMachos) MainApp.getBean(SearchMachos.class));
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, true);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, true);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.REMOVE_DISABLED, true);

		animalReducedOverviewController.showForm();
		
		setObject(animalAux);
		
		if ( animalReducedOverviewController.getObject() != null && animalReducedOverviewController.getObject().getId() > 0 ){
			getObject().setPaiMontaNatural(animalReducedOverviewController.getObject());
		}
		
		if ( getObject().getPaiMontaNatural() != null ){
			inputPai.textProperty().set(getObject().getPaiMontaNatural().getNumeroNome());	
		}else{
			inputPai.textProperty().set("");
		}
		btnRemoverPai.setVisible(getObject().getPaiMontaNatural() != null || getObject().getPaiEnseminacaoArtificial() != null);
	}
	
	@FXML
	private void handleSelecionarPaiEnseminacaoArtificial() {
		
		touroReducedOverviewController.setObject(new Touro());
		touroReducedOverviewController.showForm();
		
		if ( touroReducedOverviewController.getObject() != null && touroReducedOverviewController.getObject().getId() > 0 ){
			getObject().setPaiEnseminacaoArtificial(touroReducedOverviewController.getObject());
		}
		
		if ( getObject().getPaiEnseminacaoArtificial() != null ){
			inputPai.textProperty().set(getObject().getPaiEnseminacaoArtificial().toString());	
		}else{
			inputPai.textProperty().set("");
		}
		btnRemoverPai.setVisible(getObject().getPaiMontaNatural() != null || getObject().getPaiEnseminacaoArtificial() != null);
		
	}
	
	@FXML
	private void handleRemoverMae(){
		getObject().setMae(null);
		inputMae.setText("");
		btnRemoverMae.setVisible(false);
		btnBuscarMae.requestFocus();
	}
	
	@FXML
	private void handleRemoverPai(){
		getObject().setPaiMontaNatural(null);
		getObject().setPaiEnseminacaoArtificial(null);
		inputPai.setText("");
		btnRemoverPai.setVisible(false);
		btnBuscarPaiEnseminacaoArtificial.requestFocus();
	}
	
	@FXML
	private void handleSelecionarRaca() {
		
		racaReducedOverviewController.setObject(new Raca());
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
