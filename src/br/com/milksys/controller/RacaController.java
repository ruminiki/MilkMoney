package br.com.milksys.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Raca;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;

@Controller
public class RacaController extends AbstractController<Integer, Raca> {

	@FXML private TableColumn<Raca, String> idColumn;
	@FXML private TableColumn<Raca, String> descricaoColumn;
	@FXML private TextField idField;
	@FXML private UCTextField descricaoField;

	@FXML
	public void initialize() {
		
		if (!state.equals(State.INSERT_TO_SELECT)){
			idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
			descricaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescricao()));
		}
		if ( descricaoField != null ){
			descricaoField.textProperty().bindBidirectional(((Raca)object).descricaoProperty());
		}
		
		super.initialize();

	}

	@Override
	protected boolean isInputValid() {
		return true;
	}

	@Override
	protected String getFormName() {
		return "view/raca/RacaForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Raça";
	}

	@Override
	@Resource(name = "racaService")
	protected void setService(IService<Integer, Raca> service) {
		super.setService(service);
	}

}
