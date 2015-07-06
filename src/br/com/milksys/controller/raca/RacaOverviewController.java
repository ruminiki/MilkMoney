package br.com.milksys.controller.raca;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.model.Raca;
import br.com.milksys.service.IService;

@Controller
public class RacaOverviewController extends AbstractOverviewController<Integer, Raca> {

	@FXML private TableColumn<Raca, String> idColumn;
	@FXML private TableColumn<Raca, String> descricaoColumn;

	@Autowired private RacaFormController formController;
	
	@FXML
	public void initialize() {
		
		idColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("descricao"));
		super.initialize(formController);
		
	}

	@Override
	protected String getFormTitle() {
		return "Raça";
	}
	
	@Override
	public String getFormName() {
		return "view/raca/RacaOverview.fxml";
	}
	
	@Override
	@Resource(name = "racaService")
	protected void setService(IService<Integer, Raca> service) {
		super.setService(service);
	}

}
