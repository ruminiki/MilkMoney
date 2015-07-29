package br.com.milkmoney.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.events.ActionEvent;
import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.AbstractEntity;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.searchers.Search;

@Controller
public abstract class AbstractFormController<K, E> {

	@FXML private State state = State.LIST;
	@FXML private Button btnSave;
	@FXML private Button btnNew;
	@FXML private Button btnEdit;
	@FXML private Button btnRemove;
	@FXML private AnchorPane form;
	
	private Stage dialogStage;
	private E object;
	protected IService<K, E> service;
	
	private boolean closePopUpAfterSave = true;
	private Search<K,E> search;
	private Class<?> controllerOrigin;

	@Autowired
	private ApplicationEventPublisher publisher;
	
	private Function<E, Boolean> refreshObjectInTableView;
	private Function<E, Boolean> addObjectInTableView;
	
	/**
	 * Armazena a configuração para deixar disabled os botões do formulário
	 */
	public static final String SAVE_DISABLED = "SAVE_DISABLED";
	@SuppressWarnings("serial")
	private Map<String, Boolean> formConfig = new HashMap<String, Boolean>(){{put(SAVE_DISABLED, false);}};
	
	/**
	 * Configura os botões disableds
	 */
	protected void configureForm(){
		if ( btnSave != null )
			btnSave.setDisable(formConfig.get(SAVE_DISABLED));
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
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

	
	
	@FXML @SuppressWarnings("unchecked")
	public void handleCancel() {
		dialogStage.close();
		setObject(service.findById( (K) ((Integer)((AbstractEntity) getObject()).getId()) ));
		
		if ( refreshObjectInTableView != null )
			refreshObjectInTableView.apply(getObject());
		
		setObject(null); 
	}

	@FXML
	protected void handleSave() {
		
		beforeSave();
		
		boolean isNew =  ((AbstractEntity)object).getId() <= 0;
		
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

		if ( isNew ){
			if ( addObjectInTableView != null ){
				addObjectInTableView.apply(object);
			}
			publisher.publishEvent(new ActionEvent(getObject(), ActionEvent.EVENT_INSERT));
		}else{
			if ( refreshObjectInTableView != null ){
				refreshObjectInTableView.apply(object);
			}
			publisher.publishEvent(new ActionEvent(getObject(), ActionEvent.EVENT_INSERT));
		}
		
		this.setState(State.LIST);
		
		afterSave();
		
	}
	
	protected void beforeSave(){}
	protected void afterSave(){}
	
	protected void closeForm(){
		if ( dialogStage != null )
			dialogStage.close();
	}
	
	//==========getters e setters
	
	public Function<E, Boolean> getAddObjectInTableView() {
		return addObjectInTableView;
	}

	public void setAddObjectInTableView(Function<E, Boolean> addObjectInTableView) {
		this.addObjectInTableView = addObjectInTableView;
	}
	
	public Function<E, Boolean> getRefreshObjectInTableView() {
		return refreshObjectInTableView;
	}

	public void setRefreshObjectInTableView(Function<E, Boolean> refreshObjectInTableView) {
		this.refreshObjectInTableView = refreshObjectInTableView;
	}

	public Class<?> getControllerOrigin() {
		return controllerOrigin;
	}

	public void setControllerOrigin(Class<?> controller) {
		this.controllerOrigin = controller;
	}

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

	public void closePopUpAfterSave(boolean closePopUpAfterSave) {
		this.closePopUpAfterSave = closePopUpAfterSave;
	}
	
	public Stage getDialogStage() {
		return dialogStage;
	}

}
