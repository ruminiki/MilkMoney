package br.com.milkmoney.controller.arvoreGenealogica;

import java.util.function.Function;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.animal.AnimalReducedOverviewController;
import br.com.milkmoney.controller.arvoreGenealogica.renderer.BoxArvoreGenealogica;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Sexo;

@Controller
public class ArvoreGenealogicaOverviewController {

	@FXML private HBox hBoxFilhos, hBoxNetos, hBoxPais, hBoxAvos;
	@FXML private TextField inputAnimal;
	@FXML private Label lblAnimal;
	
	@Autowired private AnimalReducedOverviewController animalReducedOverviewController;
	
	private Animal selectedAnimal;
	
	@FXML
	public void initialize() {
		
	}
	
	private void montaArvoreGenealogica(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				
				lblAnimal.setText(selectedAnimal.getNumeroNome());
				
				hBoxPais.getChildren().clear();
				hBoxAvos.getChildren().clear();
				hBoxFilhos.getChildren().clear();
				hBoxNetos.getChildren().clear();
				
				hBoxPais.getChildren().add(new BoxArvoreGenealogica ("Pai", selectedAnimal.getPaiMontaNatural(), selectedAnimal.getPaiEnseminacaoArtificial(), carregaArvoreGenealogica));
				hBoxPais.getChildren().add(new BoxArvoreGenealogica ("Mãe", selectedAnimal.getMae(), null, carregaArvoreGenealogica));
				
				hBoxAvos.getChildren().add(new BoxArvoreGenealogica ("Avô Materno", selectedAnimal, null, carregaArvoreGenealogica));
				hBoxAvos.getChildren().add(new BoxArvoreGenealogica ("Avó Materna", selectedAnimal, null, carregaArvoreGenealogica));
				
				hBoxAvos.getChildren().add(new BoxArvoreGenealogica ("Avó Paterna", selectedAnimal, null, carregaArvoreGenealogica));
				hBoxAvos.getChildren().add(new BoxArvoreGenealogica ("Avô Paterno", selectedAnimal, null, carregaArvoreGenealogica));
				
			}
		});
		
	}
	
	Function<Animal, Boolean> carregaArvoreGenealogica = animal -> {
		if ( animal != null && animal.getId() != selectedAnimal.getId() ){
			selectedAnimal = animal;
			montaArvoreGenealogica();
		}
		return true;
	};
	
	public void showForm(Animal animal) {	
		
		this.selectedAnimal = animal;
		
		AnchorPane form = (AnchorPane) MainApp.load(getFormName());
		Stage dialogStage = new Stage();
		dialogStage.setTitle(getFormTitle());
		dialogStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream(MainApp.APPLICATION_ICON)));
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);

		Scene scene = new Scene(form);
		dialogStage.setScene(scene);
		dialogStage.setResizable(false);
		
		dialogStage.show();
		
		montaArvoreGenealogica();
		
	}
	
	@FXML
	private void handleSelecionarAnimal() {
		
		animalReducedOverviewController.setObject(new Animal(Sexo.FEMEA));
		
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, false);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, false);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.REMOVE_DISABLED, true);	
		animalReducedOverviewController.showForm();
		selectedAnimal = animalReducedOverviewController.getObject();
		
		if ( selectedAnimal != null ){
			inputAnimal.textProperty().set(selectedAnimal.getNumeroNome());	
			montaArvoreGenealogica();
		}else{
			inputAnimal.textProperty().set("");
		}
		
	}
	
	public String getFormName(){
		return "view/arvoreGenealogica/ArvoreGenealogicaOverview.fxml";
	}

	public String getFormTitle() {
		return "Árvore Genealógica";
	}
	
}
