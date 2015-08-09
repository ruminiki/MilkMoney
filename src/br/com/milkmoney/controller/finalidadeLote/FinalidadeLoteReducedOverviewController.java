package br.com.milkmoney.controller.finalidadeLote;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.AbstractReducedOverviewController;
import br.com.milkmoney.model.FinalidadeLote;
import br.com.milkmoney.service.IService;

@Controller
public class FinalidadeLoteReducedOverviewController extends AbstractReducedOverviewController<Integer, FinalidadeLote> {

	@FXML private TableColumn<FinalidadeLote, String> idColumn;
	@FXML private TableColumn<FinalidadeLote, String> descricaoColumn;

	@FXML
	public void initialize() {
		
		idColumn.setCellValueFactory(new PropertyValueFactory<FinalidadeLote,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<FinalidadeLote,String>("descricao"));
		super.initialize((FinalidadeLoteFormController)MainApp.getBean(FinalidadeLoteFormController.class));
		
	}

	@Override
	public String getFormTitle() {
		return "Finalidade Lote";
	}
	
	@Override
	public String getFormName() {
		return "view/finalidadeLote/FinalidadeLoteReducedOverview.fxml";
	}
	
	@Override
	@Resource(name = "finalidadeLoteService")
	protected void setService(IService<Integer, FinalidadeLote> service) {
		super.setService(service);
	}

}
