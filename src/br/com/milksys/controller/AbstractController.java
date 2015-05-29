package br.com.milksys.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.annotations.ColumnBind;
import br.com.milksys.service.IService;

@Controller
public abstract class AbstractController<K, E> {
	@FXML
	protected TableView<E> table;
	@FXML
	protected Label lblNumRegistros;
	protected ObservableList<E> data = FXCollections.observableArrayList();
	protected Stage dialogStage;
	protected Object object;
	protected boolean okClicked = false;
	protected IService<K, E> service;
	private   boolean isInitialized = false;

	public void initialize() {

		if ( !isInitialized ){ 
			
			//evento de sele��o de objeto na tabela
			data.addListener((ListChangeListener.Change<? extends E> c) ->{lblNumRegistros.setText(String.valueOf(data.size()));});
			
			// sempre que cria a tela ele carrega o controller novamente duplicando os registros na tabela.
			data.addAll(service.findAll());
			table.setItems(data);
			
			// captura o evento de double click da tables
			table.setOnMousePressed(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
						handleEdit();
					}
				}

			});
			
			// captura o evento de ENTER de DELETE na tabela
			table.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					
					if ( event.getCode().equals(KeyCode.ENTER) ){
						showForm();
					}
					
					if ( event.getCode().equals(KeyCode.DELETE) ){
						handleDelete();
					}
					
				}

			});
			
			table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectRowTableHandler(newValue));

			isInitialized = true;
		}

		//CONFIGURA O BIND DOS CAMPOS DE TELA COM O MODELO
		//recupera os atributos TextFields da classe filha e faz o bind com o object
		for ( Field f : this.getClass().getDeclaredFields() ){
			//SE O ATRIBUTO FOR STRING E TEXT FIELD
			if ( f.getType().equals(UCTextField.class) ){
				Annotation a = f.getAnnotation(ColumnBind.class);
				if ( a != null ){
					String columnName = ((ColumnBind)a).name();
					//se o atributo com o nome do atributo do objeto foi setado na annotation
					if ( columnName != null && !columnName.isEmpty() ){
						try {
							if ( object != null ){
								
								Method method = object.getClass().getMethod(columnName);
								method.setAccessible(true);
								
								f.setAccessible(true);
								Object faux = f.get(this);
								
								UCTextField tf = ((UCTextField) faux);
								tf.textProperty().bindBidirectional((StringProperty)method.invoke(object));
								
							}
						} catch (NoSuchMethodException | SecurityException | 
								IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
			}
			//para outros tipos fazer o mesmo procedimento
			
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

	protected void showForm() {
		AnchorPane form = (AnchorPane) MainApp.load(getFormName());

		dialogStage = new Stage();
		dialogStage.setTitle(getFormTitle());
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);

		Scene scene = new Scene(form);
		dialogStage.setScene(scene);
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					
					if ( event.getCode().equals(KeyCode.ENTER) ){
						handleOk();
					}
					
					if ( event.getCode().equals(KeyCode.ESCAPE) ){
						handleCancel();
					}
					
				}

			});
		
		dialogStage.showAndWait();
	}

	protected abstract String getFormName();
	protected abstract String getFormTitle();
	protected abstract boolean isInputValid();
	
	//========= HANDLERS INTERFACE=============//

	@FXML
	private void handleNew() throws InstantiationException,	IllegalAccessException, ClassNotFoundException {
		
		object = ((Class<?>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).newInstance();
		showForm();
	}

	@FXML
	protected void handleEdit() {
		if (object != null) {
			showForm();
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Nenhuma Sele��o");
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
			alert.setTitle("Confirma��oso");
			alert.setHeaderText("Confirme a exclus�o do registro");
			alert.setContentText("Tem certeza que deseja remover o registro selecionado?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				service.remove(table.getItems().get(selectedIndex));
				table.getItems().remove(selectedIndex);
			}
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Nenhuma Sele��o");
			alert.setHeaderText("Nenhum registro selecionado");
			alert.setContentText("Selecione pelo menos um registro na tabela!");
			alert.showAndWait();
		}
	}
	
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	
	@FXML @SuppressWarnings("unchecked")
	private void handleOk(){
		if (isInputValid()) {
			
			dialogStage.close();
			Method methodGetId;
			
			try {
				
				methodGetId = object.getClass().getMethod("getId");
				boolean isNew = ((int) methodGetId.invoke(object)) <= 0;
				if( isNew ){
					data.add((E) object);
				}else{
					data.set(table.getSelectionModel().getSelectedIndex(), (E) object);
				}
				
				service.save((E) object);
				
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | 
					IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			
		}
	}
	
}
