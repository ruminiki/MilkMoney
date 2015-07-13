package br.com.milksys.controller.causaMorteAnimal;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.controller.AbstractReducedOverviewController;
import br.com.milksys.model.CausaMorteAnimal;
import br.com.milksys.service.IService;

@Controller
public class CausaMorteAnimalReducedOverviewController extends AbstractReducedOverviewController<Integer, CausaMorteAnimal> {

	@FXML private TableColumn<CausaMorteAnimal, String> idColumn;
	@FXML private TableColumn<CausaMorteAnimal, String> descricaoColumn;

	@FXML
	public void initialize() {

		idColumn.setCellValueFactory(new PropertyValueFactory<CausaMorteAnimal,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<CausaMorteAnimal,String>("descricao"));
		super.initialize((CausaMorteAnimalFormController) MainApp.getBean(CausaMorteAnimalFormController.class));
		
	}

	@Override
	public String getFormName() {
		return "view/causaMorteAnimal/CausaMorteAnimalReducedOverview.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Causa Morte Animal";
	}
	
	@Override
	public CausaMorteAnimal getObject() {
		return (CausaMorteAnimal) super.object;
	}

	@Override
	@Resource(name = "causaMorteAnimalService")
	protected void setService(IService<Integer, CausaMorteAnimal> service) {
		super.setService(service);
	}
	
}
