package br.com.milkmoney.controller.animal;

import java.io.File;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
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
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.searchers.SearchFemeas;
import br.com.milkmoney.service.searchers.SearchMachos;
import br.com.milkmoney.util.ImageUtil;

@Controller
public class AnimalFormController extends AbstractFormController<Integer, Animal> {

	@FXML private UCTextField inputNumero, inputNome, inputMae, inputPai, inputValor, inputRaca;
	@FXML private DatePicker inputDataNascimento;
	@FXML private ChoiceBox<String> inputFinalidadeAnimal, inputSexo;
	@FXML private Button btnBuscarMae, btnBuscarPaiMontaNatural, btnBuscarPaiEnseminacaoArtificial;
	@FXML private ImageView inputImage;
	@FXML private Label lblControle;

	//controllers
	@Autowired private AnimalReducedOverviewController animalReducedOverviewController;
	@Autowired private TouroReducedOverviewController touroReducedOverviewController;
	@Autowired private RacaReducedOverviewController racaReducedOverviewController;
	
	@FXML
	public void initialize() {
		
		inputImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/cow-256.png")));
		
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
		}
		
		if ( getObject().getPaiMontaNatural() != null ){
			inputPai.textProperty().set(getObject().getPaiMontaNatural().getNumeroNome());
		}
		
		if ( getObject().getPaiEnseminacaoArtificial() != null ){
			inputPai.textProperty().set(getObject().getPaiEnseminacaoArtificial().toString());
		}
		
		if ( getObject().getRaca() != null ){
			inputRaca.textProperty().set(getObject().getRaca().toString());
		}
		
		// impede que sejam alteradas informações cadastradas pela cobertura e parto
		if ( getObject().isNascimentoCadastrado() ){
			btnBuscarMae.setDisable(true);
			btnBuscarPaiEnseminacaoArtificial.setDisable(true);
			btnBuscarPaiMontaNatural.setDisable(true);
			inputDataNascimento.setDisable(true);
			inputSexo.setDisable(true);
		}
		
		//carrega a imagem
		if ( getObject().getImagem() != null ){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					inputImage.setImage(getObject().getImage());					
				}
			});
		}
		
		MaskFieldUtil.decimal(inputValor);
		
	}

	@FXML
	private void handleSelecionarMae() {
		
		animalReducedOverviewController.setObject(getObject().getMae());
		animalReducedOverviewController.setSearch((SearchFemeas) MainApp.getBean(SearchFemeas.class));
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, true);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, true);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.REMOVE_DISABLED, true);

		animalReducedOverviewController.showForm();
		getObject().setMae(animalReducedOverviewController.getObject());
		
		if ( getObject().getMae() != null ){
			inputMae.textProperty().set(getObject().getMae().getNumeroNome());	
		}else{
			inputMae.textProperty().set("");
		}
		
	}
	
	@FXML
	private void handleSelecionarPaiMontaNatural() {
		
		animalReducedOverviewController.setObject(getObject().getPaiMontaNatural());
		animalReducedOverviewController.setSearch((SearchMachos) MainApp.getBean(SearchMachos.class));
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, true);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, true);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.REMOVE_DISABLED, true);

		animalReducedOverviewController.showForm();
		getObject().setPaiMontaNatural(animalReducedOverviewController.getObject());

		if ( getObject().getPaiMontaNatural() != null ){
			inputPai.textProperty().set(getObject().getPaiMontaNatural().getNumeroNome());	
		}else{
			inputPai.textProperty().set("");
		}

	}
	
	@FXML
	private void handleSelecionarPaiEnseminacaoArtificial() {
		
		touroReducedOverviewController.showForm();
		getObject().setPaiEnseminacaoArtificial(touroReducedOverviewController.getObject());
		
		if ( getObject().getPaiEnseminacaoArtificial() != null ){
			inputPai.textProperty().set(getObject().getPaiEnseminacaoArtificial().toString());	
		}else{
			inputPai.textProperty().set("");
		}
		
	}
	
	@FXML
	private void handleRemoverMae(){
		getObject().setMae(null);
		inputMae.setText("");
		btnBuscarMae.requestFocus();
	}
	
	@FXML
	private void handleRemoverPai(){
		getObject().setPaiMontaNatural(null);
		getObject().setPaiEnseminacaoArtificial(null);
		inputPai.setText("");
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
	
	@FXML
	private void selecionarImagem(){
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Carregar foto do animal");
		
		fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Images", "*.*"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
            new FileChooser.ExtensionFilter("PNG", "*.png")
        );
		
		File file = fileChooser.showOpenDialog(getDialogStage());
		
		lblControle.setText("Aguarde...");
		
		if (file != null) {
			try {
				String destination = ImageUtil.reduceImageQualityAndSave(ImageUtil.UM_MB, file);
				getObject().setImagem(destination);
				inputImage.setImage(getObject().getImage());
			} catch (Exception e) {
				CustomAlert.mensagemErro("Ocorreu um erro ao tentar carregar a imagem. \nPor favor, tente novamente.");
			}
		}
		
		lblControle.setText("");
	}
	
	@Override
	protected void beforeSave() {
		super.beforeSave();
		//remove a imagem antiga do disco, caso tenha sido alterada
		String image = ((AnimalService)service).getImagePath(getObject());
		if ( image != null && !image.equals(getObject().getImagem()) ){
			new File(image).delete();
		}
	}
	
	@Override
	public void handleCancel() {
		//remove a imagem carregada caso o usuário cancele a operação
		String image = ((AnimalService)service).getImagePath(getObject());
		if ( getObject().getImagem() != null && !getObject().getImagem().equals(image) ){
			new File(getObject().getImagem()).deleteOnExit();
		}
		super.handleCancel();
	}
	
	@Override
	public String getFormName() {
		return "view/animal/AnimalForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Animal";
	}
	
	@Override
	@Resource(name = "animalService")
	protected void setService(IService<Integer, Animal> service) {
		super.setService(service);
	}

	
}
