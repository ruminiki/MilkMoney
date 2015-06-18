package br.com.milksys.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

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
	@FXML private TextField idField;
	@FXML private UCTextField descricaoField;

	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
			descricaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescricao()));
			super.initialize();
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			descricaoField.textProperty().bindBidirectional(((FinalidadeLote)object).descricaoProperty());
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
	@Resource(name = "finalidadeLoteService")
	protected void setService(IService<Integer, FinalidadeLote> service) {
		super.setService(service);
	}

}
