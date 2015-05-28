package br.com.milksys.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.annotations.ColumnBind;
import br.com.milksys.model.Raca;
import br.com.milksys.service.IService;

@Controller
public class RacaController extends AbstractController<Integer, Raca> {

	@FXML
	private TableColumn<Raca, String> idColumn;
	@FXML
	private TableColumn<Raca, String> descricaoColumn;
	@FXML
	private TextField idField;
	@FXML
	@ColumnBind(name = "descricaoProperty")
	private UCTextField descricaoField;

	@FXML
	public void initialize() {
		idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(	String.valueOf(cellData.getValue().getId())));
		descricaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescricao()));

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
		return "Ra�a";
	}

	@Override
	@Resource(name = "racaService")
	protected void setService(IService<Integer, Raca> service) {
		super.setService(service);
	}

}
