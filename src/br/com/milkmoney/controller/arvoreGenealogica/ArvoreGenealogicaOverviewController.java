package br.com.milkmoney.controller.arvoreGenealogica;

import java.util.function.Function;

import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.arvoreGenealogica.renderer.BoxArvoreGenealogica;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.service.AnimalService;

@Controller
public class ArvoreGenealogicaOverviewController {

	@FXML private HBox hBoxFilhos, hBoxNetos, hBoxPais, hBoxAvos;
	@FXML private TextField inputAnimal;
	@FXML private Label lblAnimal;
	@FXML protected ListView<Animal> listAnimais;
	@FXML private UCTextField inputPesquisa;
	
	@Autowired private AnimalService animalService;
	
	private Animal selectedAnimal;
	
	@FXML
	public void initialize() {
		
		//filter over table view eventos
		FilteredList<Animal> filteredData = new FilteredList<>(animalService.findAllAsObservableList(), animal -> true);
		inputPesquisa.textProperty().addListener(obs->{
	        String filter = inputPesquisa.getText(); 
	        if(filter == null || filter.length() == 0) {
	            filteredData.setPredicate(animal -> true);
	        }else {
	            filteredData.setPredicate(animal -> animal.getNumeroNome().contains(filter));
	        }
	    });
		
        SortedList<Animal> sortedData = new SortedList<>(filteredData);
        listAnimais.setItems(sortedData);
		
		/*listAnimais.getItems().clear();
		listAnimais.getItems().addAll(animalService.findAllAsObservableList());*/
		
		listAnimais.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> {
				selectedAnimal = newValue;
				montaArvoreGenealogica();	
		});
		
	}
	
	private void montaArvoreGenealogica(){
		
		if ( selectedAnimal == null ){
			return;
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				
				lblAnimal.setText(selectedAnimal.getNumeroNome());
				
				hBoxPais.getChildren().clear();
				hBoxAvos.getChildren().clear();
				hBoxFilhos.getChildren().clear();
				hBoxNetos.getChildren().clear();
				
				Animal mae  = selectedAnimal.getMae();
				Object pai  = selectedAnimal.getPaiEnseminacaoArtificial() != null ? selectedAnimal.getPaiEnseminacaoArtificial() : selectedAnimal.getPaiMontaNatural();
				
				hBoxPais.getChildren().add(new BoxArvoreGenealogica ("Pai", pai, carregaArvoreGenealogica));
				hBoxPais.getChildren().add(new BoxArvoreGenealogica ("Mãe", mae, carregaArvoreGenealogica));
				
				Animal avoPaterna = null;
				Object avoPaterno = null;
				
				if ( pai instanceof Animal ){
					avoPaterna = pai != null ? animalService.findMae((Animal)pai) : null;
					avoPaterno = pai != null ? animalService.findPai((Animal)pai) : null;
				}
				
				hBoxAvos.getChildren().add(new BoxArvoreGenealogica ("Avó Paterna", avoPaterna, carregaArvoreGenealogica));
				hBoxAvos.getChildren().add(new BoxArvoreGenealogica ("Avô Paterno", avoPaterno, carregaArvoreGenealogica));
				
				Animal avoMaterna   = mae != null ? animalService.findMae(mae) : null;
				Object avoMaterno   = mae != null ? animalService.findPai(mae) : null;
				
				hBoxAvos.getChildren().add(new BoxArvoreGenealogica ("Avô Materno", avoMaterno, carregaArvoreGenealogica));
				hBoxAvos.getChildren().add(new BoxArvoreGenealogica ("Avó Materna", avoMaterna, carregaArvoreGenealogica));
				
			}
		});
		
	}
	
	Function<Animal, Boolean> carregaArvoreGenealogica = animal -> {
		if ( animal != null && animal.getId() != selectedAnimal.getId() ){
			selectedAnimal = animal;
			listAnimais.getSelectionModel().select(selectedAnimal);
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
	
	public String getFormName(){
		return "view/arvoreGenealogica/ArvoreGenealogicaOverview.fxml";
	}

	public String getFormTitle() {
		return "Árvore Genealógica";
	}
	
}
