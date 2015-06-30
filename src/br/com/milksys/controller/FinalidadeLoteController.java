package br.com.milksys.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.model.FinalidadeLote;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;

@Controller
public class FinalidadeLoteController extends AbstractController<Integer, FinalidadeLote> {

	@FXML private TableColumn<FinalidadeLote, String> idColumn;
	@FXML private TableColumn<FinalidadeLote, String> descricaoColumn;
	@FXML private UCTextField inputDescricao;

	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			idColumn.setCellValueFactory(new PropertyValueFactory<FinalidadeLote,String>("id"));
			descricaoColumn.setCellValueFactory(new PropertyValueFactory<FinalidadeLote,String>("descricao"));
			super.initialize();
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		}
		
	}

	@Override
	protected boolean isInputValid() {
		return true;
	}

	@Override
	protected String getFormName() {
		return "view/finalidadeLote/FinalidadeLoteForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Finalidade Lote";
	}
	
	@Override
	protected FinalidadeLote getObject() {
		return (FinalidadeLote) super.object;
	}

	@Override
	@Resource(name = "finalidadeLoteService")
	protected void setService(IService<Integer, FinalidadeLote> service) {
		super.setService(service);
	}

}
