package br.com.milksys.controller;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.springframework.stereotype.Controller;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import br.com.milksys.service.IService;

@Controller
public class AbstractController<K, E> {
	@FXML
	protected TableView<E> table;
	protected ObservableList<E> data = FXCollections.observableArrayList();
	protected Stage dialogStage;
	protected Object object;
	protected boolean okClicked = false;
	protected IService<K, E> service;
	
	public void initialize() {

		// FIXME descobrir se está sendo incluido ou alterado um item
		// sempre que cria a tela ele carrega o controller novamente duplicando
		// os registros na tabela.
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

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	protected void selectRowTableHandler(Object value) {
		object = value;
	}

	protected void showFormDialog(Object value) {
		throw new NotImplementedException();
	}

	/**
	 * Chamado quando o usuário clica no botão novo. Abre uma janela para
	 * inserir um novo objeto.
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException 
	 */
	@FXML
	private void handleNew() throws InstantiationException,	IllegalAccessException, ClassNotFoundException {
		
		object = ((Class<?>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).newInstance();
		showFormDialog(object);
	}

	@FXML
	protected void handleEdit() {
		if (object != null) {
			showFormDialog(object);
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
		if (selectedIndex > 0) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmação");
			alert.setHeaderText("Confirme a exclusão do registro");
			alert.setContentText("Tem certeza que deseja remover o registro selecionado?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				service.remove(table.getItems().get(selectedIndex));
				table.getItems().remove(selectedIndex);
			}
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Nenhuma Seleção");
			alert.setHeaderText("Nenhum registro selecionado");
			alert.setContentText("Selecione pelo menos um registro na tabela!");
			alert.showAndWait();
		}
	}

}
