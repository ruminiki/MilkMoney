package br.com.milkmoney.controller.parametro;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.Parametro;
import br.com.milkmoney.service.IService;

@Controller
public class ParametroOverviewController extends AbstractOverviewController<Integer, Parametro> {

	@FXML private TableColumn<Parametro, String> siglaColumn;
	@FXML private TableColumn<Parametro, String> descricaoColumn;
	@FXML private TableColumn<Parametro, String> valorColumn;

	@Autowired private ParametroFormController formController;
	
	@FXML
	public void initialize() {
		
		siglaColumn.setCellValueFactory(new PropertyValueFactory<Parametro,String>("sigla"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<Parametro,String>("descricao"));
		valorColumn.setCellValueFactory(new PropertyValueFactory<Parametro,String>("valor"));
		super.initialize(formController);
		
	}

	@Override
	public String getFormTitle() {
		return "Par�metro";
	}
	
	@Override
	public String getFormName() {
		return "view/parametro/ParametroOverview.fxml";
	}
	
	@Override
	@Resource(name = "parametroService")
	protected void setService(IService<Integer, Parametro> service) {
		super.setService(service);
	}

}
