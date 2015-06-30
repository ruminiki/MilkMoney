package br.com.milksys.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

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
	@FXML private UCTextField inputDescricao;

	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			idColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("id"));
			descricaoColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("descricao"));
			super.initialize();
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		}
		
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
	protected Raca getObject() {
		return (Raca)super.object;
	}

	@Override
	@Resource(name = "racaService")
	protected void setService(IService<Integer, Raca> service) {
		super.setService(service);
	}

}
