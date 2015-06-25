package br.com.milksys.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import br.com.milksys.model.State;
import br.com.milksys.service.IService;

@Controller
public abstract class AbstractController<K, E> {
	protected ObservableList<E> data = FXCollections.observableArrayList();
	protected Stage dialogStage;
	protected Object object;
	protected IService<K, E> service;
	protected boolean isInitialized = false;
	@FXML protected TableView<E> table;
	@FXML protected Label lblNumRegistros;
	@FXML protected State state = State.LIST;
	protected AnchorPane form;

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
						showForm(0,0);
					}

					if (event.getCode().equals(KeyCode.DELETE)) {
						handleDelete();
					}

				}

			});

			table.getSelectionModel().selectedItemProperty()
					.addListener((observable, oldValue, newValue) -> selectRowTableHandler(newValue));

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

	public void setObject(Object object) {
		this.object = object;
	}

	protected void setService(IService<K, E> service) {
		this.service = service;
	}

	protected void selectRowTableHandler(Object value) {
		object = value;
	}

	protected void showForm(int width, int height) {
		
		form = (AnchorPane) MainApp.load(getFormName());
		
		if ( height > 0 )
			form.setPrefHeight(height);
		
		if ( width > 0 )
			form.setPrefWidth(width);
		
		dialogStage = new Stage();
		dialogStage.setTitle(getFormTitle());
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);

		Scene scene = new Scene(form);
		dialogStage.setScene(scene);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode().equals(KeyCode.ENTER)) {
					handleOk();
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

	public Object getObject() {
		return object;
	}

	protected void initializeTableOverview(){
		this.data.clear();
		this.data.addAll(service.findAll());
	}
	
	protected abstract String getFormName();
	protected abstract String getFormTitle();
	protected abstract boolean isInputValid();
	
	// ========= HANDLERS INTERFACE=============//

	@FXML
	private void handleNew() throws InstantiationException,	IllegalAccessException, ClassNotFoundException {
		this.state = State.INSERT;
		object = ((Class<?>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1])
				.newInstance();
		showForm(0,0);
	}

	@FXML
	protected void handleEdit() {
		if (object != null) {
			this.state = State.UPDATE;
			showForm(0,0);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Nenhuma Seleção");
			alert.setHeaderText("Nenhum animal selecionado");
			alert.setContentText("Selecione pelo menos um registro na tabela!");
			alert.showAndWait();
		}
	}

	@FXML
	protected void handleDelete() {
		int selectedIndex = table.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmação");
			alert.setHeaderText("Confirme a exclusão do registro");
			alert.setContentText("Tem certeza que deseja remover o registro selecionado?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				service.remove(table.getItems().get(selectedIndex));
				table.getItems().remove(selectedIndex);
				updateLabelNumRegistros();
			}
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Nenhuma Seleção");
			alert.setHeaderText("Nenhum registro selecionado");
			alert.setContentText("Selecione pelo menos um registro na tabela!");
			alert.showAndWait();
		}
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
		this.state = State.LIST;
	}

	@FXML
	@SuppressWarnings("unchecked")
	protected void handleOk() {
		if (isInputValid()) {

			dialogStage.close();
			Method methodGetId;

			try {

				methodGetId = object.getClass().getMethod("getId");
				boolean isNew = ((int) methodGetId.invoke(object)) <= 0;

				service.save((E) object);

				if (isNew) {
					data.add((E) object);
					updateLabelNumRegistros();
				} else {
					if ( table != null )
						data.set(table.getSelectionModel().getSelectedIndex(),(E) object);
				}

			} catch (NoSuchMethodException | SecurityException
					| IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}

		}
		this.state = State.LIST;
	}

}
