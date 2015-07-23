package br.com.milksys.controller.reports;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.components.UCTextField;
import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Animal;
import br.com.milksys.service.AnimalService;
import br.com.milksys.service.RelatorioService;
import br.com.milksys.validation.Validator;

@Controller
public class FormularioRegistroPartoParametrosController {

	@FXML private UCTextField inputPesquisa;
	@FXML private ListView<Animal> listAnimais, listSelecionados;
	@FXML private Button btnAdicionar, btnAdicionarTodos, btnRemover, btnRemoverTodos;
	
	@Autowired private AnimalService animalService;
	@Autowired private RelatorioService relatorioService;

	@FXML
	public void initialize() {
		
		if ( inputPesquisa != null ){
			inputPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
				listAnimais.setItems(animalService.defaultSearch(newValue));
			});
		}
		
		listAnimais.setItems(animalService.findAllFemeasAtivasAsObservableList());
		
		btnAdicionar.setOnAction(action -> {
			
			if ( listAnimais.getSelectionModel().getSelectedItem() != null ){
				if ( !listSelecionados.getItems().contains(listAnimais.getSelectionModel().getSelectedItem()) ){
					listSelecionados.getItems().add(listAnimais.getSelectionModel().getSelectedItem());
				}
			}
			
		});
		
		btnAdicionarTodos.setOnAction(action -> {
			
			for ( Animal animal : listAnimais.getItems() ){
				
				if ( !listSelecionados.getItems().contains(animal) ){
					listSelecionados.getItems().add(animal);
				}
				
			}
			
		});
		
		btnRemover.setOnAction(action -> {
			if ( listSelecionados.getSelectionModel().getSelectedItem() != null ){
				listSelecionados.getItems().remove(listSelecionados.getSelectionModel().getSelectedItem());	
			}
		});
		
		btnRemoverTodos.setOnAction(action -> {
			listSelecionados.getItems().clear();
		});
		
	}
	
	@FXML
	private void handleCancelar(){
		if ( btnAdicionar != null ){
			Stage stage = (Stage)btnAdicionar.getScene().getWindow();
			// se for popup
			if ( stage.getModality().equals(Modality.APPLICATION_MODAL) ){
				((Stage)btnAdicionar.getScene().getWindow()).close();	
			}else{
				MainApp.resetLayout();
			}
		}
	}
	
	@FXML
	private void handleExecutar(){
		
		if ( listSelecionados.getItems().size() <= 0 ){
			throw new ValidationException(Validator.CAMPO_OBRIGATORIO, "Por favor, selecione os animais para executar o relatório.");
		}
		
		//os ids dos animais selecionados são passados como parâmetro
		StringBuilder sb = new StringBuilder();
		
		for ( Animal animal : listSelecionados.getItems() ){
			sb.append(animal.getId());
			sb.append(",");
		}
		
		sb.replace(sb.length(), sb.length(), "");
		
		relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.FORMULARIO_CAMPO_REGISTRO_PARTO, sb.toString());
	}

}
