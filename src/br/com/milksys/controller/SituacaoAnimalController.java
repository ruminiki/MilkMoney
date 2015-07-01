package br.com.milksys.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.model.SituacaoAnimal;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;

@Controller
public class SituacaoAnimalController extends AbstractController<Integer, SituacaoAnimal> {

	@FXML private TableColumn<SituacaoAnimal, String> idColumn;
	@FXML private TableColumn<SituacaoAnimal, String> descricaoColumn;
	@FXML private TableColumn<SituacaoAnimal, String> animalAtivoColumn;
	@FXML private UCTextField inputDescricao;
	@FXML private ToggleButton inputAnimalAtivo;

	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			idColumn.setCellValueFactory(new PropertyValueFactory<SituacaoAnimal,String>("id"));
			descricaoColumn.setCellValueFactory(new PropertyValueFactory<SituacaoAnimal,String>("descricao"));
			animalAtivoColumn.setCellValueFactory(new PropertyValueFactory<SituacaoAnimal,String>("animalAtivoText"));
			super.initialize();
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
			inputAnimalAtivo.selectedProperty().bindBidirectional(getObject().animalAtivoProperty());
			inputAnimalAtivo.selectedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable,
						Boolean oldValue, Boolean newValue) {
					updateLabelAtivoInativo(newValue);
				}

			});
			
			updateLabelAtivoInativo(getObject().getAnimalAtivo());
		}
		
	}

	private void updateLabelAtivoInativo(Boolean value) {
		if ( value ){
			inputAnimalAtivo.setText("Ativo");
		}else{
			inputAnimalAtivo.setText("Inativo");
		}
	}
	
	@Override
	protected String getFormName() {
		return "view/situacaoAnimal/SituacaoAnimalForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Situação Animal";
	}
	
	@Override
	protected SituacaoAnimal getObject() {
		return (SituacaoAnimal)super.object;
	}

	@Override
	@Resource(name = "situacaoAnimalService")
	protected void setService(IService<Integer, SituacaoAnimal> service) {
		super.setService(service);
	}

}
