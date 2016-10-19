package br.com.milkmoney.controller.lote;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
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
import br.com.milkmoney.service.LoteService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class LoteFormController extends AbstractFormController<Integer, Lote>  {
	
	@FXML private UCTextField inputPesquisa, inputDescricao, inputFinalidade;
	@FXML private ComboBox<String> inputAtivo;
	@FXML private TextArea inputObservacao;
	@FXML protected ListView<Animal> listAnimais, listSelecionados;
	@FXML protected Button btnAdicionar, btnAdicionarTodos, btnRemover, btnRemoverTodos;
	@FXML private Label lblTotalAnimais, lblMediaLactacoes, lblMediaProducao, lblMediaIdade;
	
	@Autowired protected AnimalService animalService;
	@Autowired protected FinalidadeLoteReducedOverviewController finalidadeLoteReducedController;

	@FXML
	public void initialize() {
		
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputAtivo.setItems(SimNao.getItems());
		inputAtivo.valueProperty().bindBidirectional(getObject().ativoProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		
		if ( getObject() != null && getObject().getFinalidadeLote() != null ){
			inputFinalidade.setText(getObject().getFinalidadeLote().getDescricao());
		}
		
		if ( getObject() != null ){
			listSelecionados.getItems().setAll(getObject().getAnimais());
		}
		
		//controle de adição dos animais no lote
		if ( inputPesquisa != null ){
			inputPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
				listAnimais.setItems(animalService.defaultSearch(newValue));
			});
		}
		
		listAnimais.setItems(animalService.findAllFemeasAtivasAsObservableList(DateUtil.today));
		listAnimais.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		// captura o evento de double click da table
		listAnimais.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					if ( !listSelecionados.getItems().contains(listAnimais.getSelectionModel().getSelectedItem()) ){
						listSelecionados.getItems().add(listAnimais.getSelectionModel().getSelectedItem());
					}
					listAnimais.getSelectionModel().clearSelection();
				}
			}
		});
		
		listSelecionados.getItems().addListener(new ListChangeListener<Animal>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Animal> arg0) {
				setTotais();
			}
		});
				
		btnAdicionar.setOnAction(action -> {
			
			if ( listAnimais.getSelectionModel().getSelectedItems() != null ){
				
				for ( Animal animal : listAnimais.getSelectionModel().getSelectedItems() ){
					
					if ( !listSelecionados.getItems().contains(animal) ){
						listSelecionados.getItems().add(animal);
					}
					
				}
				
				listAnimais.getSelectionModel().clearSelection();
				
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
				Animal animal = listSelecionados.getSelectionModel().getSelectedItem();
				animal.setLote(null);
				listSelecionados.getItems().remove(animal);	
			}
		});
		
		btnRemoverTodos.setOnAction(action -> {
			for ( Animal animal : listSelecionados.getItems() ){
				animal.setLote(null);
			}
			listSelecionados.getItems().clear();
		});
		
		setTotais();
	}
	
	private void setTotais(){
		if ( getObject() != null ){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblTotalAnimais.setText(String.valueOf(listSelecionados.getItems().size()));
					lblMediaIdade.setText(String.valueOf(((LoteService)service).getMediaIdadeAnimais(listSelecionados.getItems())));
					lblMediaLactacoes.setText(String.valueOf(((LoteService)service).getMediaLactacoesAnimais(listSelecionados.getItems())));
					lblMediaProducao.setText(String.valueOf(((LoteService)service).getMediaProducaoAnimais(listSelecionados.getItems())));						
				}
			});
		}
	}
	
	@Override
	protected void beforeSave() {
		getObject().setAnimais(listSelecionados.getItems());
		
		for ( Animal animal : getObject().getAnimais() ){
			animal.setLote(getObject());
		}
		
		super.beforeSave();
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
	public String getFormName() {
		return "view/lote/LoteForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Lote";
	}
	
	@Override
	@Resource(name = "loteService")
	protected void setService(IService<Integer, Lote> service) {
		super.setService(service);
	}

	
}
