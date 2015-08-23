package br.com.milkmoney.controller.insumo;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.AbstractReducedOverviewController;
import br.com.milkmoney.model.Insumo;
import br.com.milkmoney.model.TipoInsumo;
import br.com.milkmoney.model.UnidadeMedida;
import br.com.milkmoney.service.IService;

@Controller
public class InsumoReducedOverviewController extends AbstractReducedOverviewController<Integer, Insumo> {

	@FXML private TableColumn<Insumo, String>        descricaoColumn;
	@FXML private TableColumn<TipoInsumo, String>    tipoInsumoColumn;
	@FXML private TableColumn<UnidadeMedida, String> unidadeMedidaColumn;
	
	@FXML
	public void initialize() {
		
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<Insumo,String>("descricao"));
		tipoInsumoColumn.setCellValueFactory(new PropertyValueFactory<TipoInsumo,String>("tipoInsumo"));
		unidadeMedidaColumn.setCellValueFactory(new PropertyValueFactory<UnidadeMedida,String>("unidadeMedida"));
		super.initialize((InsumoFormController) MainApp.getBean(InsumoFormController.class));
		
	}
	
	@Override
	public String getFormTitle() {
		return "Insumo";
	}
	
	@Override
	public String getFormName() {
		return "view/insumo/InsumoOverview.fxml";
	}
	
	@Override
	@Resource(name = "insumoService")
	protected void setService(IService<Integer, Insumo> service) {
		super.setService(service);
	}

}
