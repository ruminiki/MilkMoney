package br.com.milksys.controller.parametro;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.model.Parametro;
import br.com.milksys.service.IService;

@Controller
public class ParametroOverviewController extends AbstractOverviewController<Integer, Parametro> {

	@FXML private TableColumn<Parametro, String> siglaColumn;
	@FXML private TableColumn<Parametro, String> descricaoColumn;
	@FXML private TableColumn<Parametro, String> valorColumn;

	@Autowired private ParametroFormController formController;
	
	@FXML
	public void initialize() {
		
		siglaColumn.setCellValueFactory(new PropertyValueFactory<Parametro,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<Parametro,String>("descricao"));
		valorColumn.setCellValueFactory(new PropertyValueFactory<Parametro,String>("valor"));
		super.initialize(formController);
		
	}

	@Override
	protected String getFormTitle() {
		return "Parâmetro";
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
