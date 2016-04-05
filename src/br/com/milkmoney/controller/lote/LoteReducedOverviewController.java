package br.com.milkmoney.controller.lote;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.AbstractReducedOverviewController;
import br.com.milkmoney.model.Lote;
import br.com.milkmoney.service.IService;

@Controller
public class LoteReducedOverviewController extends AbstractReducedOverviewController<Integer, Lote> {

	@FXML private TableColumn<Lote, String> idColumn;
	@FXML private TableColumn<Lote, String> descricaoColumn; 

	@FXML
	public void initialize() {

		idColumn.setCellValueFactory(new PropertyValueFactory<Lote,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<Lote,String>("descricao"));
		super.initialize((LoteFormController) MainApp.getBean(LoteFormController.class));
		
	}

	@Override
	public String getFormName() {
		return "view/lote/LoteReducedOverview.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Lote";
	}
	
	@Override
	public Lote getObject() {
		return (Lote) super.object;
	}

	@Override
	@Resource(name = "loteService")
	protected void setService(IService<Integer, Lote> service) {
		super.setService(service);
	}
	
}
