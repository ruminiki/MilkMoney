package br.com.milkmoney.controller.complicacaoParto;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.TableCellOptionSelectFactory;
import br.com.milkmoney.controller.AbstractReducedOverviewController;
import br.com.milkmoney.model.ComplicacaoParto;
import br.com.milkmoney.service.IService;

@Controller
public class ComplicacaoPartoReducedOverviewController extends AbstractReducedOverviewController<Integer, ComplicacaoParto> {

	@FXML private TableColumn<ComplicacaoParto, String> idColumn;
	@FXML private TableColumn<ComplicacaoParto, String> descricaoColumn;
	@FXML private TableColumn<ComplicacaoParto, String> opcoesColumn;

	@FXML
	public void initialize() {

		idColumn.setCellValueFactory(new PropertyValueFactory<ComplicacaoParto,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<ComplicacaoParto,String>("descricao"));
		opcoesColumn.setCellValueFactory(new PropertyValueFactory<ComplicacaoParto,String>("id"));
		opcoesColumn.setCellFactory(new TableCellOptionSelectFactory<ComplicacaoParto,String>(selecionar));

		super.initialize((ComplicacaoPartoFormController) MainApp.getBean(ComplicacaoPartoFormController.class));
		
	}

	@Override
	public String getFormName() {
		return "view/complicacaoParto/ComplicacaoPartoReducedOverview.fxml";
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
