package br.com.milkmoney.controller.lote;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.finalidadeLote.FinalidadeLoteReducedOverviewController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FinalidadeLote;
import br.com.milkmoney.model.Limit;
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
	@FXML private VBox vbBoxGroup;
	
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
		
		//controle de adi��o dos animais no lote
		if ( inputPesquisa != null ){
			inputPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
				listAnimais.setItems(animalService.defaultSearch(newValue, Limit.UNLIMITED));
			});
		}
		
		listAnimais.setItems(animalService.findAllFemeasAtivasAsObservableList(DateUtil.today, Limit.UNLIMITED));
		listAnimais.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		// captura o evento de double click da table
		listAnimais.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					addAnimalSelecionado(listAnimais.getSelectionModel().getSelectedItem());
					setTotais();
					listAnimais.getSelectionModel().clearSelection();
				}
			}
		});
		
		btnAdicionar.setOnAction(action -> {
			if ( listAnimais.getSelectionModel().getSelectedItems() != null ){
				for ( Animal animal : listAnimais.getSelectionModel().getSelectedItems() ){
					addAnimalSelecionado(animal);
				}
				setTotais();
				listAnimais.getSelectionModel().clearSelection();
			}
		});
		
		btnAdicionarTodos.setOnAction(action -> {
			if ( listAnimais.getSelectionModel().getSelectedItems() != null ){
				for ( Animal animal : listAnimais.getItems() ){
					addAnimalSelecionado(animal);
				}
				setTotais();
				listAnimais.getSelectionModel().clearSelection();
			}
		});
		
		btnRemover.setOnAction(action -> {
			if ( listSelecionados.getSelectionModel().getSelectedItem() != null ){
				Animal animal = listSelecionados.getSelectionModel().getSelectedItem();
				animal.setLote(null);
				listSelecionados.getItems().remove(animal);	
				setTotais();
			}
		});
		
		btnRemoverTodos.setOnAction(action -> {
			for ( Animal animal : listSelecionados.getItems() ){
				animal.setLote(null);
			}
			listSelecionados.getItems().clear();
			setTotais();
		});
		
		setTotais();
	}
	
	private void addAnimalSelecionado(Animal animal){
		boolean contains = false;
		for ( Animal a1 : listSelecionados.getItems() ){
			if ( a1.getId() == animal.getId() ){
				contains = true;
				break;
			}
		}
		if ( !contains ){
			listSelecionados.getItems().add(animal);
		}
	}
	
	private void setTotais(){
		if ( getObject() != null ){
			vbBoxGroup.setCursor(Cursor.WAIT);
			Task<Void> task = new Task<Void>() {
				@Override
				public Void call() throws InterruptedException {
					try{
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								lblTotalAnimais.setText(String.valueOf(listSelecionados.getItems().size()));
								lblMediaIdade.setText(String.valueOf(((LoteService)service).getMediaIdadeAnimais(listSelecionados.getItems())));
								lblMediaLactacoes.setText(String.valueOf(((LoteService)service).getMediaLactacoesAnimais(listSelecionados.getItems())));
								lblMediaProducao.setText(String.valueOf(((LoteService)service).getMediaProducaoAnimais(listSelecionados.getItems())));						
							}
						});
					}catch(Exception e){
						e.printStackTrace();
					}
					return null;	
				}
			};
			
			Thread thread = new Thread(task);				
			thread.setDaemon(true);
			thread.start();
			
			task.setOnSucceeded(e -> {
				vbBoxGroup.setCursor(Cursor.DEFAULT);
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
