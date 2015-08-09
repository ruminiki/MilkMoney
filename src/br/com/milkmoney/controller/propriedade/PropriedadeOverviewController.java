package br.com.milkmoney.controller.propriedade;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.Propriedade;
import br.com.milkmoney.service.IService;

@Controller
public class PropriedadeOverviewController extends AbstractOverviewController<Integer, Propriedade> {

	@FXML private TableColumn<Propriedade, String> descricaoColumn;
	@FXML private TableColumn<Propriedade, String> enderecoColumn;
	@FXML private TableColumn<Propriedade, String> areaColumn;

	@FXML
	public void initialize() {

		enderecoColumn.setCellValueFactory(new PropertyValueFactory<Propriedade,String>("endereco"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<Propriedade,String>("descricao"));
		areaColumn.setCellValueFactory(new PropertyValueFactory<Propriedade,String>("area"));
		
		super.initialize((PropriedadeFormController) MainApp.getBean(PropriedadeFormController.class));
		
	}

	@Override
	public String getFormName() {
		return "view/propriedade/PropriedadeReducedOverview.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Propriedade";
	}
	
	@Override
	@Resource(name = "propriedadeService")
	protected void setService(IService<Integer, Propriedade> service) {
		super.setService(service);
	}
	
}
