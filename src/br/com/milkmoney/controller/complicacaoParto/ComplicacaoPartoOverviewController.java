package br.com.milkmoney.controller.complicacaoParto;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.ComplicacaoParto;
import br.com.milkmoney.service.IService;

@Controller
public class ComplicacaoPartoOverviewController extends AbstractOverviewController<Integer, ComplicacaoParto> {

	@FXML private TableColumn<ComplicacaoParto, String> idColumn;
	@FXML private TableColumn<ComplicacaoParto, String> descricaoColumn;

	@FXML
	public void initialize() {

		idColumn.setCellValueFactory(new PropertyValueFactory<ComplicacaoParto,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<ComplicacaoParto,String>("descricao"));
		super.initialize((ComplicacaoPartoFormController) MainApp.getBean(ComplicacaoPartoFormController.class));
		
	}

	@Override
	public String getFormName() {
		return "view/complicacaoParto/ComplicacaoPartoOverview.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Complicação Parto";
	}
	
	@Override
	public ComplicacaoParto getObject() {
		return (ComplicacaoParto) super.object;
	}

	@Override
	@Resource(name = "complicacaoPartoService")
	protected void setService(IService<Integer, ComplicacaoParto> service) {
		super.setService(service);
	}
	
}
