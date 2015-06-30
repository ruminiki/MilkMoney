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
import br.com.milksys.model.AbstractEntity;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;

@Controller
public abstract class AbstractController<K, E> {
	protected ObservableList<E> data = FXCollections.observableArrayList();
	protected Stage dialogStage;
	protected AbstractEntity object;
	protected IService<K, E> service;
	protected boolean isInitialized = false;
	protected AnchorPane form;
	protected boolean closePopUpAfterSave = true;
	@FXML protected TableView<E> table;
	@FXML protected Label lblNumRegistros;
	@FXML protected State state = State.LIST;

	public void initialize() {

		if (!state.equals(State.INSERT_TO_SELECT)) {

			this.initializeTableOverview();
			
			table.setItems(data);
			
			updateLabelNumRegistros();
			
			// captura o evento de double click da table
			table.setOnMousePressed(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
						handleEdit();
					}
				}

			});

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

	protected void initializeTableOverview(){
		this.data.clear();
		this.data.addAll(service.findAll());
	}
	
	protected abstract String getFormName();
	protected abstract String getFormTitle();
	protected abstract boolean isInputValid();
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
				service.remove(data.get(selectedIndex));
				data.remove(selectedIndex);
				updateLabelNumRegistros();
			}
		} else {
			CustomAlert.nenhumRegistroSelecionado();		
		}
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
		this.state = State.LIST;
	}

	@FXML
	@SuppressWarnings("unchecked")
	protected void handleSave() {
		if (isInputValid()) {

			if (closePopUpAfterSave)
				dialogStage.close();

			boolean isNew = object.getId() <= 0;

			service.save((E) object);

			if (isNew) {
				data.add((E) object);
				updateLabelNumRegistros();
			} else {
				if (table != null && data != null) {
					for (int index = 0; index < data.size(); index++) {
						AbstractEntity o = (AbstractEntity) data.get(index);
						if (o.getId() == object.getId()) {
							data.set(index, (E) object);
						}
					}
				}
			}
			
			this.state = State.LIST;
			
		}
	}

}
