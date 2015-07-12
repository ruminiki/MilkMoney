package br.com.milksys.controller;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.components.CustomAlert;
import br.com.milksys.model.AbstractEntity;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;
import br.com.milksys.service.searchers.Search;

@Controller
public abstract class AbstractOverviewController<K, E>{
	
	@FXML protected TableView<E> table;
	@FXML protected Label lblNumRegistros;
	@FXML protected Button btnNew;
	@FXML protected Button btnEdit;
	@FXML protected Button btnRemove;
	@FXML protected AnchorPane form;
	@FXML protected TextField inputPesquisa;
	
	private Stage dialogStage;
	protected State state = State.LIST;
	protected ObservableList<E> data = FXCollections.observableArrayList();
	protected AbstractEntity object;
	protected IService<K, E> service;
	private Search<K,E> search;
	private Class<?> controllerOrigin;
	
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
	

	public void initialize(AbstractFormController<K, E> formController) {
		
		this.formController = formController;

		this.refreshTableOverview();
		table.setItems(data);
		updateLabelNumRegistros();
		
		configureDoubleClickTable();

		// captura o evento de ENTER de DELETE na tabela
		table.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode().equals(KeyCode.ENTER)) {
					formController.showForm();
				}

				if (event.getCode().equals(KeyCode.DELETE)) {
					handleDelete();
				}

			}

		});

		table.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selectRowTableHandler((AbstractEntity) newValue));

		//configura os botões do formulário
		configureForm();
		
		if ( inputPesquisa != null ){
			inputPesquisa.textProperty().addListener((observable, oldValue, newValue) -> refreshTableOverview());
		}
		
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
					setObject((AbstractEntity) table.getSelectionModel().getSelectedItem());
				}
			}

		});
	}

	protected void updateLabelNumRegistros(){
		if ( lblNumRegistros != null ){
			lblNumRegistros.setText(String.valueOf(data.size()));
		}
	}
	
	public void setObject(AbstractEntity object) {
		this.object = object;
	}

	protected void setService(IService<K, E> service) {
		this.service = service;
	}

	protected void selectRowTableHandler(AbstractEntity value) {
		object = value;
	}

	protected void refreshTableOverview(){
		
		this.data.clear();
		
		if ( inputPesquisa != null && inputPesquisa.getText() != null &&
				inputPesquisa.getText().length() > 0){
			data.addAll(handleDefaultSearch());
			setSearch(null);
		}else{
			if ( search != null ){
				data.addAll(search.doSearch());
			}else{
				this.data.addAll(service.findAll());
			}
		}
		
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
	
	public Function<E, Boolean> refreshObjectInTableView = (object) -> {
		if ( object != null ){
			for (int index = 0; index < data.size(); index++) {
				AbstractEntity o = (AbstractEntity) data.get(index);
				if (o.getId() == ((AbstractEntity) object).getId()) {
					data.set(index, (E) object);
					table.layout();
					return true;
				}
			}
		}
		return false;
	};
	
	public Function<E, Boolean> addObjectInTableView = (object) -> {
		if ( object != null ){
			getData().add((E) object);
			updateLabelNumRegistros();
		}
		return false;
	};
	
	protected abstract String getFormTitle();
	protected abstract String getFormName();
	
	public void showForm() {
		
		form = (AnchorPane) MainApp.load(getFormName());
		
		dialogStage = new Stage();
		dialogStage.setTitle(getFormTitle());
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);

		Scene scene = new Scene(form);
		dialogStage.setScene(scene);

		dialogStage.setResizable(false);
		dialogStage.showAndWait();
		
		this.setState(State.LIST);
		
	}
	
	protected void closeForm(){
		if ( dialogStage != null )
			dialogStage.close();
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
	
	public Class<?> getControllerOrigin() {
		return controllerOrigin;
	}

	public void setControllerOrigin(Class<?> controller) {
		this.controllerOrigin = controller;
	}
	
	@SuppressWarnings("unchecked")
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

	public void setSearch(Search<K,E> search) {
		this.search = search;
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
	
}
