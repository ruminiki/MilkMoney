package br.com.milkmoney.controller.animal;

import java.io.File;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.raca.RacaReducedOverviewController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FinalidadeAnimal;
import br.com.milkmoney.model.Raca;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.util.ImageUtil;
import br.com.milkmoney.validation.AnimalValidation;

@Controller
public class AnimalCriaFormController extends AbstractFormController<Integer, Animal> {

	@FXML private UCTextField inputNumero, inputNome, inputMae, inputPai, inputValor, inputRaca;
	@FXML private DatePicker inputDataNascimento;
	@FXML private ChoiceBox<String> inputFinalidadeAnimal, inputSexo;
	@FXML private ImageView inputImage;
	@FXML private Button btnRemoverImagem;
	
	//services
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
		}
		
		if ( getObject().getPaiMontaNatural() != null ){
			inputPai.textProperty().set(getObject().getPaiMontaNatural().getNumeroNome());
		}
		
		if ( getObject().getPaiEnseminacaoArtificial() != null ){
			inputPai.textProperty().set(getObject().getPaiEnseminacaoArtificial().toString());
		}
		
		if ( getObject().getRaca() != null ){
			inputRaca.textProperty().set(getObject().getRaca().getDescricao());
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				inputImage.setImage(getObject().getImage());	
			}
		});
		
		btnRemoverImagem.setDisable(getObject().getImagem() == null || getObject().getImagem().isEmpty());
		
		MaskFieldUtil.decimal(inputValor);
		
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
		
		inputImage.getScene().setCursor(Cursor.WAIT);
		
		if (file != null) {
			try {
				String destination = ImageUtil.reduceImageQualityAndSave(ImageUtil.LIMIT, file);
				getObject().setImagem(destination);
				inputImage.setImage(getObject().getImage());
				btnRemoverImagem.setDisable(false);
			} catch (Exception e) {
				CustomAlert.mensagemErro("Ocorreu um erro ao tentar carregar a imagem. \nPor favor, tente novamente.");
			}
		}
		inputImage.getScene().setCursor(Cursor.DEFAULT);
	}
	
	@FXML
	private void removerImagem(){
		getObject().setImagem(null);
		inputImage.setImage(getObject().getImage());
		btnRemoverImagem.setDisable(true);
	}
	
	@Override
	protected void handleSave() {
		//apenas valida, pois o objeto é salvo na mesma transação que salva o parto
		AnimalValidation.validate(getObject());
		closeForm();
	}

	@Override
	public String getFormName() {
		return "view/animal/AnimalCriaForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Animal - Cria";
	}
	
	@Override
	@Resource(name = "animalService")
	protected void setService(IService<Integer, Animal> service) {
		super.setService(service);
	}
	
}
