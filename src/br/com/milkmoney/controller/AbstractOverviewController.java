package br.com.milkmoney.controller;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.model.AbstractEntity;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.searchers.Search;

@Controller
public abstract class AbstractOverviewController<K, E>{
	
	@FXML protected TableView<E>      table;
	@FXML protected Label             lblNumRegistros;
	@FXML protected Button            btnNew;
	@FXML protected Button            btnEdit;
	@FXML protected Button            btnRemove;
	@FXML protected AnchorPane        form;
	@FXML protected TextField         inputPesquisa;
	
	private         Stage             dialogStage;
	private         State             state         = State.LIST;
	protected       ObservableList<E> data          = FXCollections.observableArrayList();
	protected       E                 object;
	protected       IService<K, E>    service;
	
	private         Search<K,E>       search;
	private         Object[]          searchParams;
	
	private AbstractFormController<K, E> formController;
	
	public static final String NEW_DISABLED = "NEW_DISABLED";
	public static final String EDIT_DISABLED = "EDIT_DISABLED";
	public static final String REMOVE_DISABLED = "REMOVE_DISABLED";
	/**
	 * Armazena a configuração para deixar disabled os botões do formulário
	 */
	@SuppressWarnings("serial")
	private Map<String, Boolean> formConfig = new HashMap<String, Boolean>()
	{{put(NEW_DISABLED, false);put(EDIT_DISABLED, false);put(REMOVE_DISABLED, false);}};
	
	private ContextMenu contextMenu = new ContextMenu();
	
	MenuItem atualizar = new MenuItem("Atualizar");
	MenuItem editar = new MenuItem("Editar");
	MenuItem remover = new MenuItem("Remover");
	
	public void initialize(AbstractFormController<K, E> formController) {
		
		this.formController = formController;
		table.setFixedCellSize(25);
		this.refreshTableOverview();
		table.setItems(data);
		updateLabelNumRegistros();
		
		configureDoubleClickTable();

		// captura o evento de ENTER de DELETE na tabela
		table.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode().equals(KeyCode.DELETE)) {
					handleDelete();
					event.consume();
				}

			}

		});

		table.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selectRowTableHandler(newValue));

		//configura os botões do formulário
		configureForm();
		
		if ( inputPesquisa != null ){
			inputPesquisa.textProperty().addListener((observable, oldValue, newValue) -> refreshTableOverview());
		}
		
		atualizar.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	refreshTableOverview();
		    }
		});
		
		editar.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	handleEdit();
		    }
		});
		
		remover.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	handleDelete();
		    }
		});
		
		
		contextMenu.getItems().clear();
		contextMenu.getItems().addAll(atualizar, editar, remover);
		contextMenu.setPrefWidth(120);
		table.setContextMenu(contextMenu);
		
	}
	
	/**
	 * Configura os botões disableds
	 */
	protected void configureForm(){
		if ( btnNew != null )
			btnNew.setDisable(formConfig.get(NEW_DISABLED));
		if ( btnEdit != null )
			btnEdit.setDisable(formConfig.get(EDIT_DISABLED));
		if ( btnRemove != null )
			btnRemove.setDisable(formConfig.get(REMOVE_DISABLED));
	}
	
	protected void configureDoubleClickTable(){
		// captura o evento de double click da table
		table.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					handleEdit();
				}
				
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 1) {
					handleSelectItemTable();
				}
				
				if (event.isSecondaryButtonDown()) {
					handleRightClick();
				}
			}

		});
	}
	
	protected void handleSelectItemTable(){
		setObject(table.getSelectionModel().getSelectedItem());
	}
	
	protected void handleRightClick(){
		
	}

	protected void updateLabelNumRegistros(){
		if ( lblNumRegistros != null ){
			lblNumRegistros.setText(String.valueOf(data.size()));
		}
	}
	
	public void setObject(E object) {
		this.object = object;
	}

	protected void setService(IService<K, E> service) {
		this.service = service;
	}

	protected void selectRowTableHandler(E value) {
		object = value;
	}

	protected void refreshTableOverview(){
		
		this.data.clear();
		this.table.getItems().clear();
		
		if ( inputPesquisa != null && inputPesquisa.getText() != null &&
				inputPesquisa.getText().length() > 0){
			data.addAll(handleDefaultSearch());
			setSearch(null);
		}else{
			if ( search != null ){
				data.addAll(search.doSearch(searchParams));
			}else{
				this.data.addAll(service.findAll());
			}
		}
		
		table.setItems(data);
		table.layout();
		updateLabelNumRegistros();
		
	}
	
	public ObservableList<E> handleDefaultSearch() {
		return service.defaultSearch(inputPesquisa.getText());
	}
	
	@FXML
	public void clearFilter(){
		if ( inputPesquisa != null ){
			inputPesquisa.clear();
		}
		setSearch(null);
		refreshTableOverview();
	}
	
	public void refreshObjectInTableView(E object){
		if ( object != null ){
			for (int index = 0; index < data.size(); index++) {
				AbstractEntity o = (AbstractEntity) data.get(index);
				if (o.getId() == ((AbstractEntity) object).getId()) {
					data.set(index, (E) object);
					//table.layout();
				}
			}
		}
	}
	
	public Function<E, Boolean> refreshObjectInTableView = (object) -> {
		if ( object != null ){
			refreshObjectInTableView(object);
			return true;
		}
		return false;
	};
	
	public void addObjectInTableView(E object){
		if ( object != null ){
			getData().add((E) object);
			updateLabelNumRegistros();
		}
	}
	
	public Function<E, Boolean> addObjectInTableView = (object) -> {
		if ( object != null ){
			addObjectInTableView(object);
		}
		return false;
	};
	
	public abstract String getFormTitle();
	public abstract String getFormName();
	
	public void showForm() {
		
		form = (AnchorPane) MainApp.load(getFormName());
		
		dialogStage = new Stage();
		dialogStage.setTitle(getFormTitle());
		dialogStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream(MainApp.APPLICATION_ICON)));
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);

		Scene scene = new Scene(form);
		dialogStage.setScene(scene);

		dialogStage.setResizable(false);
		dialogStage.showAndWait();
		
		this.setState(State.LIST);
		
	}
	
	@FXML
	protected void closeForm(){
		if ( dialogStage != null ){
			dialogStage.close();
		}else{
			if ( table != null ){
				Stage stage = (Stage)table.getScene().getWindow();
				// se for popup
				if ( stage.getModality().equals(Modality.APPLICATION_MODAL) ){
					((Stage)table.getScene().getWindow()).close();	
				}else{
					MainApp.resetLayout();
				}
			}
		}
	}
	
	// ========= HANDLERS INTERFACE=============//

	@FXML
	public void handleNew() {
		this.setState(State.INSERT);
		formController.setRefreshObjectInTableView(refreshObjectInTableView);
		formController.setAddObjectInTableView(addObjectInTableView);
		formController.setState(State.INSERT);
		formController.setObject(newObject());
		formController.showForm();
	}

	@FXML
	public void handleEdit() {
		
		//setObject(service.findById( (K) ((Integer)((AbstractEntity) getObject()).getId()) ));
		
		if (object != null) {
			formController.setRefreshObjectInTableView(refreshObjectInTableView);
			formController.setAddObjectInTableView(addObjectInTableView);
			formController.setState(State.UPDATE);
			formController.setObject(getObject());
			formController.showForm();
		} else {
			CustomAlert.nenhumRegistroSelecionado();
		}
		
	}

	@FXML
	protected void handleDelete() {
		int selectedIndex = table.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Optional<ButtonType> result = CustomAlert.confirmarExclusao();
			if (result.get() == ButtonType.OK) {
				
				try {
					service.remove(data.get(selectedIndex));
				} catch (Exception e) {
					CustomAlert.mensagemAlerta("", e.getMessage());
					return;
				}
				
				data.remove(selectedIndex);
				updateLabelNumRegistros();
			}
		} else {
			CustomAlert.nenhumRegistroSelecionado();		
		}
	}

	//==========getters e setters
	public E getObject(){
		
		if ( object == null ){
			newObject();
		}
		return (E) object;
		
	}
	
	@SuppressWarnings("unchecked")
	public E newObject(){
		
		try {
			return (E) ((Class<?>) ((ParameterizedType) this.getClass()
					.getGenericSuperclass()).getActualTypeArguments()[1])
					.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
		
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Search<K,E> getSearch() {
		return search;
	}

	public void setSearch(Search<K,E> search, Object ...objects) {
		this.search = search;
		this.searchParams = objects;
	}

	public ObservableList<E> getData() {
		return data;
	}

	public void setData(ObservableList<E> data) {
		this.data = data;
	}

	public Map<String, Boolean> getFormConfig() {
		return formConfig;
	}

	public void setFormConfig(Map<String, Boolean> formConfig) {
		this.formConfig = formConfig;
	}

	public ContextMenu getContextMenu() {
		return contextMenu;
	}

	public void setContextMenu(ContextMenu contextMenu) {
		this.contextMenu = contextMenu;
	}

	protected AbstractFormController<K, E> getFormController() {
		return formController;
	}

	protected void setFormController(AbstractFormController<K, E> formController) {
		this.formController = formController;
	}
	
	

}
