package br.com.milksys.controller;

import java.util.HashMap;
import java.util.Map;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.components.CustomAlert;
import br.com.milksys.components.events.SelectAfterInsertEvent;
import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.AbstractEntity;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;
import br.com.milksys.service.searchers.Search;

@Controller
public abstract class AbstractFormController<K, E> {
	
	@FXML private State state = State.LIST;
	@FXML private Button btnSave;
	@FXML private Button btnNew;
	@FXML private Button btnEdit;
	@FXML private Button btnRemove;
	@FXML private AnchorPane form;
	
	private Stage dialogStage;
	private AbstractEntity object;
	private IService<K, E> service;
	
	private boolean closePopUpAfterSave = true;
	private Search<K,E> search;
	private Class<?> controllerOrigin;
	
	public static final String NEW_DISABLED = "NEW_DISABLED";
	public static final String SAVE_DISABLED = "SAVE_DISABLED";
	public static final String EDIT_DISABLED = "EDIT_DISABLED";
	public static final String REMOVE_DISABLED = "REMOVE_DISABLED";
	/**
	 * Armazena a configura��o para deixar disabled os bot�es do formul�rio
	 */
	@SuppressWarnings("serial")
	private Map<String, Boolean> formConfig = new HashMap<String, Boolean>()
	{{put(NEW_DISABLED, false);put(SAVE_DISABLED, false);put(EDIT_DISABLED, false);put(REMOVE_DISABLED, false);}};
	
	/**
	 * Configura os bot�es disableds
	 */
	protected void configureForm(){
		if ( btnNew != null )
			btnNew.setDisable(formConfig.get(NEW_DISABLED));
		if ( btnSave != null )
			btnSave.setDisable(formConfig.get(SAVE_DISABLED));
		if ( btnEdit != null )
			btnEdit.setDisable(formConfig.get(EDIT_DISABLED));
		if ( btnRemove != null )
			btnRemove.setDisable(formConfig.get(REMOVE_DISABLED));
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

	public void showForm() {
		
		form = (AnchorPane) MainApp.load(getFormName());
		
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
		
		this.setState(State.LIST);
		
	}

	protected abstract String getFormName();
	protected abstract String getFormTitle();

	// ========= HANDLERS INTERFACE=============//

	@FXML
	public void handleCancel() {
		dialogStage.close();
		setObject(null); 
	}

	@FXML
	@SuppressWarnings("unchecked")
	protected void handleSave() {
		
		beforeSave();
		
		if ( !state.equals(State.CREATE_TO_SELECT) ){
			
			try {
				service.save((E) object);
			} catch (ValidationException e) {
				CustomAlert.mensagemAlerta(e.getTipo(), e.getMessage());
				return;
			}

		}else{
			try {
				service.validate((E) object);
			} catch (ValidationException e) {
				CustomAlert.mensagemAlerta(e.getTipo(), e.getMessage());
				return;
			}
		}
		
		if (closePopUpAfterSave)
			dialogStage.close();

		this.setState(State.LIST);
		
		afterSave();
		
		if ( getControllerOrigin() != null && MainApp.getBean(getControllerOrigin()) != null &&
				MainApp.getBean(getControllerOrigin()) instanceof EventTarget ) {
			Event.fireEvent((EventTarget)MainApp.getBean(getControllerOrigin()), new SelectAfterInsertEvent(getObject(), Event.ANY));
		}
			
	}
	
	protected void beforeSave(){}
	protected void afterSave(){}
	
	protected void closeForm(){
		if ( dialogStage != null )
			dialogStage.close();
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
		return (E) object;
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

	public Map<String, Boolean> getFormConfig() {
		return formConfig;
	}

	public void setFormConfig(Map<String, Boolean> formConfig) {
		this.formConfig = formConfig;
	}

	public boolean isClosePopUpAfterSave() {
		return closePopUpAfterSave;
	}

	public void setClosePopUpAfterSave(boolean closePopUpAfterSave) {
		this.closePopUpAfterSave = closePopUpAfterSave;
	}
	
}
