package br.com.milkmoney.controller.touro;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.controller.AbstractReducedOverviewController;
import br.com.milkmoney.model.Touro;
import br.com.milkmoney.service.IService;

@Controller
public class TouroReducedOverviewController extends AbstractReducedOverviewController<Integer, Touro> {

	@FXML private TableColumn<Touro, String> codigoColumn;
	@FXML private TableColumn<Touro, String> nomeColumn;
	
	@Autowired private TouroFormController touroFormController;
	
	@FXML
	public void initialize() {
		
		codigoColumn.setCellValueFactory(new PropertyValueFactory<Touro,String>("codigo"));
		nomeColumn.setCellValueFactory(new PropertyValueFactory<Touro, String>("nome"));
		
		super.initialize(touroFormController);
		
	}

	@Override
	public String getFormName() {
		return "view/touro/TouroReducedOverview.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Touro";
	}
	
	@Override
	public Touro getObject() {
		return (Touro) super.object;
	}

	@Override
	@Resource(name = "touroService")
	protected void setService(IService<Integer, Touro> service) {
		super.setService(service);
	}
	
}
