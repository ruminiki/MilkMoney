package br.com.milkmoney.controller.centroCusto;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.AbstractReducedOverviewController;
import br.com.milkmoney.model.CentroCusto;
import br.com.milkmoney.service.IService;

@Controller
public class CentroCustoReducedOverviewController extends AbstractReducedOverviewController<Integer, CentroCusto> {

	@FXML private TableColumn<CentroCusto, String> idColumn;
	@FXML private TableColumn<CentroCusto, String> descricaoColumn;

	@FXML
	public void initialize() {

		idColumn.setCellValueFactory(new PropertyValueFactory<CentroCusto,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<CentroCusto,String>("descricao"));
		super.initialize((CentroCustoFormController) MainApp.getBean(CentroCustoFormController.class));
		
	}

	@Override
	public String getFormName() {
		return "view/centroCusto/CentroCustoReducedOverview.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Centro de Custo";
	}
	
	@Override
	public CentroCusto getObject() {
		return (CentroCusto) super.object;
	}

	@Override
	@Resource(name = "centroCustoService")
	protected void setService(IService<Integer, CentroCusto> service) {
		super.setService(service);
	}
	
}
