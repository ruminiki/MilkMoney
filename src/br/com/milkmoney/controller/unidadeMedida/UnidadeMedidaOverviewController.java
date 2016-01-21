package br.com.milkmoney.controller.unidadeMedida;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.UnidadeMedida;
import br.com.milkmoney.service.IService;

@Controller
public class UnidadeMedidaOverviewController extends AbstractOverviewController<Integer, UnidadeMedida> {

	@FXML private TableColumn<UnidadeMedida, String> idColumn;
	@FXML private TableColumn<UnidadeMedida, String> descricaoColumn;
	@FXML private TableColumn<UnidadeMedida, String> siglaColumn;

	@FXML
	public void initialize() {

		idColumn.setCellValueFactory(new PropertyValueFactory<UnidadeMedida,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<UnidadeMedida,String>("descricao"));
		siglaColumn.setCellValueFactory(new PropertyValueFactory<UnidadeMedida,String>("sigla"));
		super.initialize((UnidadeMedidaFormController) MainApp.getBean(UnidadeMedidaFormController.class));
		
	}

	@Override
	public String getFormName() {
		return "view/unidadeMedida/UnidadeMedidaOverview.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Unidade de Medida";
	}
	
	@Override
	public UnidadeMedida getObject() {
		return (UnidadeMedida) super.object;
	}

	@Override
	@Resource(name = "unidadeMedidaService")
	protected void setService(IService<Integer, UnidadeMedida> service) {
		super.setService(service);
	}
	
}
