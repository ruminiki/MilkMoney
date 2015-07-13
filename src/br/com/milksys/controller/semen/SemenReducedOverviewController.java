package br.com.milksys.controller.semen;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.controller.AbstractReducedOverviewController;
import br.com.milksys.model.Semen;
import br.com.milksys.service.IService;
import br.com.milksys.service.searchers.SearchSemenComEstoque;

@Controller
public class SemenReducedOverviewController extends AbstractReducedOverviewController<Integer, Semen> {

	@FXML private TableColumn<Semen, String> touroColumn;
	@FXML private TableColumn<Semen, LocalDate> dataCompraColumn;
	@FXML private TableColumn<Semen, String> quantidadeDisponivelColumn;
	
	@Autowired private SemenFormController semenFormController;
	
	@FXML
	public void initialize() {
		
		touroColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("touro"));
		dataCompraColumn.setCellFactory(new TableCellDateFactory<Semen, LocalDate>("dataCompra"));
		quantidadeDisponivelColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("quantidadeDisponivel"));
		
		super.initialize(semenFormController);
		
	}

	@FXML
	private void handleBuscarSemenComoEstoque(){
		setSearch((SearchSemenComEstoque)MainApp.getBean(SearchSemenComEstoque.class));
		refreshTableOverview();
	}
	
	@Override
	public String getFormName() {
		return "view/semen/SemenReducedOverview.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Semen";
	}
	
	@Override
	public Semen getObject() {
		return (Semen) super.object;
	}

	@Override
	@Resource(name = "semenService")
	protected void setService(IService<Integer, Semen> service) {
		super.setService(service);
	}
	
}
