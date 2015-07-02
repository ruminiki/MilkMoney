package br.com.milksys.controller;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.components.CustomAlert;
import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.AbstractEntity;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;
import br.com.milksys.service.searchers.Search;

@Controller
public abstract class AbstractController<K, E> {
	
	@FXML protected TableView<E> table;
	@FXML protected Label lblNumRegistros;
	@FXML protected State state = State.LIST;
	
	protected ObservableList<E> data = FXCollections.observableArrayList();
	protected Stage dialogStage;
	protected AbstractEntity object;
	protected IService<K, E> service;
	protected boolean isInitialized = false;
	protected AnchorPane form;
	protected boolean closePopUpAfterSave = true;
	
	private Search<K,E> search;
	private Class<?> controllerOrigin;

	public void initialize() {

		if (!state.equals(State.INSERT_TO_SELECT)) {

			this.refreshTableOverview();
			table.setItems(data);
			updateLabelNumRegistros();
			
			configureDoubleClickTable();

			// captura o evento de ENTER de DELETE na tabela
			table.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {

					if (event.getCode().equals(KeyCode.ENTER)) {
						showForm(null);
					}

					if (event.getCode().equals(KeyCode.DELETE)) {
						handleDelete();
					}

				}

			});

			table.getSelectionModel().selectedItemProperty()
					.addListener((observable, oldValue, newValue) -> selectRowTableHandler((AbstractEntity) newValue));

			isInitialized = true;
		}

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
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
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

	protected void showForm(String formName) {
		
		formName = (formName != null && !formName.isEmpty()) ? formName : getFormName();
		
		form = (AnchorPane) MainApp.load(formName);
		
		dialogStage = new Stage();
		dialogStage.setTitle(getFormTitle());
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);

		Scene scene = new Scene(form);
		dialogStage.setScene(scene);

		form.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode().equals(KeyCode.ENTER)) {
					handleSave();
				}

				if (event.getCode().equals(KeyCode.ESCAPE)) {
					handleCancel();
				}

			}

		});
		
		dialogStage.setResizable(false);
		dialogStage.showAndWait();
		
		this.state = State.LIST;
		
	}

	protected void refreshTableOverview(){
		
		this.data.clear();
		if ( search != null ){
			data.addAll(search.doSearch());
		}else{
			this.data.addAll(service.findAll());
		}
		table.layout();
		
	}
	
	protected abstract String getFormName();
	protected abstract String getFormTitle();
	protected abstract Object getObject();
	
	// ========= HANDLERS INTERFACE=============//

	@FXML
	protected void handleNew() throws InstantiationException,	IllegalAccessException, ClassNotFoundException {
		this.state = State.INSERT;
		object = (AbstractEntity) ((Class<?>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1])
				.newInstance();
		showForm(null);
	}

	@FXML
	protected void handleEdit() {
		if (object != null) {
			this.state = State.UPDATE;
			showForm(null);
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

	@FXML@SuppressWarnings("unchecked")
	protected void handleCancel() {
		dialogStage.close();
		this.state = State.LIST;
		refreshObjectInTableView((AbstractEntity)service.findById( (K) new Integer(object.getId())) );
		setObject(null); 
	}

	@FXML
	@SuppressWarnings("unchecked")
	protected void handleSave() {
		
		if ( !state.equals(State.CREATE_TO_SELECT) ){
			
			boolean isNew = object.getId() <= 0;

			try {
				service.save((E) object);
			} catch (ValidationException e) {
				CustomAlert.mensagemAlerta(e.getTipo(), e.getMessage());
				return;
			}
			
			if (isNew) {
				data.add((E) object);
				updateLabelNumRegistros();
			} else {
				if (table != null && data != null) {
					refreshObjectInTableView(object);
				}
			}
		}
		
		if (closePopUpAfterSave)
			dialogStage.close();

		this.state = State.LIST;
			
	}
	
	@SuppressWarnings("unchecked")
	protected void refreshObjectInTableView(AbstractEntity object){
		if ( object != null ){
			for (int index = 0; index < data.size(); index++) {
				AbstractEntity o = (AbstractEntity) data.get(index);
				if (o.getId() == object.getId()) {
					data.set(index, (E) object);
				}
			}
		}
	}

	//==========getters e setters
	
	public Class<?> getControllerOrigin() {
		return controllerOrigin;
	}

	public void setControllerOrigin(Class<?> controller) {
		this.controllerOrigin = controller;
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
	
}
