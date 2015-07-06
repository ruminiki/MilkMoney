package br.com.milksys.controller.finalidadeLote;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.model.FinalidadeLote;
import br.com.milksys.service.IService;

@Controller
public class FinalidadeLoteOverviewController extends AbstractOverviewController<Integer, FinalidadeLote> {

	@FXML private TableColumn<FinalidadeLote, String> idColumn;
	@FXML private TableColumn<FinalidadeLote, String> descricaoColumn;

	@FXML
	public void initialize() {
		
		idColumn.setCellValueFactory(new PropertyValueFactory<FinalidadeLote,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<FinalidadeLote,String>("descricao"));
		super.initialize((FinalidadeLoteFormController)MainApp.getBean(FinalidadeLoteFormController.class));
		
	}

	@Override
	protected String getFormTitle() {
		return "Finalidade Lote";
	}
	
	@Override
	protected String getFormName() {
		return "view/finalidadeLote/FinalidadeLoteOverview.fxml";
	}
	
	@Override
	@Resource(name = "finalidadeLoteService")
	protected void setService(IService<Integer, FinalidadeLote> service) {
		super.setService(service);
	}

}
