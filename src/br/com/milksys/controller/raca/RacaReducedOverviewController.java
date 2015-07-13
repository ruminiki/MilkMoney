package br.com.milksys.controller.raca;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.controller.AbstractReducedOverviewController;
import br.com.milksys.model.Raca;
import br.com.milksys.service.IService;

@Controller
public class RacaReducedOverviewController extends AbstractReducedOverviewController<Integer, Raca> {

	@FXML private TableColumn<Raca, String> idColumn;
	@FXML private TableColumn<Raca, String> descricaoColumn;

	@FXML
	public void initialize() {

		idColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("descricao"));
		super.initialize((RacaFormController) MainApp.getBean(RacaFormController.class));
		
	}

	@Override
	public String getFormName() {
		return "view/raca/RacaReducedOverview.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Raça";
	}
	
	@Override
	public Raca getObject() {
		return (Raca) super.object;
	}

	@Override
	@Resource(name = "racaService")
	protected void setService(IService<Integer, Raca> service) {
		super.setService(service);
	}
	
}
