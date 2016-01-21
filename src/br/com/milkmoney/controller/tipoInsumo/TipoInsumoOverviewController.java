package br.com.milkmoney.controller.tipoInsumo;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.TipoInsumo;
import br.com.milkmoney.service.IService;

@Controller
public class TipoInsumoOverviewController extends AbstractOverviewController<Integer, TipoInsumo> {

	@FXML private TableColumn<TipoInsumo, String> idColumn;
	@FXML private TableColumn<TipoInsumo, String> descricaoColumn;

	@FXML
	public void initialize() {

		idColumn.setCellValueFactory(new PropertyValueFactory<TipoInsumo,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<TipoInsumo,String>("descricao"));
		super.initialize((TipoInsumoFormController) MainApp.getBean(TipoInsumoFormController.class));
		
	}

	@Override
	public String getFormName() {
		return "view/tipoInsumo/TipoInsumoOverview.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Tipo de Insumo";
	}
	
	@Override
	public TipoInsumo getObject() {
		return (TipoInsumo) super.object;
	}

	@Override
	@Resource(name = "tipoInsumoService")
	protected void setService(IService<Integer, TipoInsumo> service) {
		super.setService(service);
	}
	
}
