package br.com.milkmoney.controller.lote;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.finalidadeLote.FinalidadeLoteReducedOverviewController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FinalidadeLote;
import br.com.milkmoney.model.Lote;
import br.com.milkmoney.model.SimNao;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.IService;

@Controller
public class LoteFormController extends AbstractFormController<Integer, Lote>  {
	
	@FXML private UCTextField inputPesquisa, inputDescricao, inputFinalidade;
	@FXML private ComboBox<String> inputAtivo;
	@FXML private ListView<Animal> listAnimais, listAnimaisSelecionados;
	@FXML private Button btnAdicionar, btnAdicionarTodos, btnRemover, btnRemoverTodos;
	@FXML private Label lblTotalAnimais, lblMediaLactacoes, lblMediaProducao, lblMediaIdade;
	
	@Autowired protected AnimalService animalService;
	@Autowired protected FinalidadeLoteReducedOverviewController finalidadeLoteReducedController;

	@FXML
	public void initialize() {
		
		if ( inputPesquisa != null ){
			inputPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
				listAnimais.setItems(animalService.defaultSearch(newValue));
			});
		}
		
		listAnimais.setItems(animalService.findAllFemeasAtivasAsObservableList());
		listAnimais.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		listAnimaisSelecionados.setItems(FXCollections.observableArrayList(getObject().getAnimais()));
		
		// captura o evento de double click da table
		listAnimais.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					if ( !listAnimaisSelecionados.getItems().contains(listAnimais.getSelectionModel().getSelectedItem()) ){
						adicionarAnimalAoLote(listAnimais.getSelectionModel().getSelectedItem());
					}
					listAnimais.getSelectionModel().clearSelection();
				}
			}
		});
		
		btnAdicionar.setOnAction(action -> {
			
			if ( listAnimais.getSelectionModel().getSelectedItems() != null ){
				
				for ( Animal animal : listAnimais.getSelectionModel().getSelectedItems() ){
					
					if ( !listAnimaisSelecionados.getItems().contains(animal) ){
						adicionarAnimalAoLote(animal);
					}
					
				}
				
				listAnimais.getSelectionModel().clearSelection();
				
			}
			
		});
		
		btnAdicionarTodos.setOnAction(action -> {
			
			for ( Animal animal : listAnimais.getItems() ){
				
				if ( !listAnimaisSelecionados.getItems().contains(animal) ){
					adicionarAnimalAoLote(animal);
				}
				
			}
			
		});
		
		btnRemover.setOnAction(action -> {
			if ( listAnimaisSelecionados.getSelectionModel().getSelectedItem() != null ){
				removerAnimalLote(listAnimaisSelecionados.getSelectionModel().getSelectedItem());
			}
		});
		
		btnRemoverTodos.setOnAction(action -> {
			listAnimaisSelecionados.getItems().clear();
		});
		
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputAtivo.setItems(SimNao.getItems());
		inputAtivo.valueProperty().bindBidirectional(getObject().ativoProperty());
		if ( getObject() != null && getObject().getFinalidadeLote() != null ){
			inputFinalidade.setText(getObject().getFinalidadeLote().getDescricao());
		}
		
	}
	
	private void adicionarAnimalAoLote(Animal animal){
		listAnimaisSelecionados.getItems().add(animal);
		getObject().getAnimais().add(animal);
	}
	
	private void removerAnimalLote(Animal animal){
		listAnimaisSelecionados.getItems().remove(animal);
		getObject().getAnimais().remove(animal);
	}
	
	@FXML
	private void handleSelecionarFinalidade(){
		
		finalidadeLoteReducedController.setObject(new FinalidadeLote());
		finalidadeLoteReducedController.showForm();
		
		if ( finalidadeLoteReducedController.getObject() != null && finalidadeLoteReducedController.getObject().getId() > 0 ){
			getObject().setFinalidadeLote(finalidadeLoteReducedController.getObject());
		}
		
		if ( getObject().getFinalidadeLote() != null ){
			inputFinalidade.textProperty().set(getObject().getFinalidadeLote().toString());	
		}else{
			inputFinalidade.textProperty().set("");
		}
		
	}
	
	@Override
	protected String getFormName() {
		return "view/lote/LoteForm.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Lote";
	}
	
	@Override
	@Resource(name = "loteService")
	protected void setService(IService<Integer, Lote> service) {
		super.setService(service);
	}

	
}
