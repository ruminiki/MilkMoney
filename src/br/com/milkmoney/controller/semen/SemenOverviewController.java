package br.com.milkmoney.controller.semen;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.PropertyDecimalValueFactory;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.Semen;
import br.com.milkmoney.model.Touro;
import br.com.milkmoney.service.IService;

@Controller
public class SemenOverviewController extends AbstractOverviewController<Integer, Semen> {

	@FXML private TableColumn<Touro, String> touroColumn;
	@FXML private TableColumn<Semen, LocalDate> dataCompraColumn;
	@FXML private TableColumn<Semen, String> valorUnitarioColumn;
	@FXML private TableColumn<Semen, String> valorTotalColumn;
	@FXML private TableColumn<Semen, String> quantidadeAdquiridaColumn;
	@FXML private TableColumn<Semen, String> quantidadeDisponivelColumn;
	
	@FXML
	public void initialize() {
		
		touroColumn.setCellValueFactory(new PropertyValueFactory<Touro,String>("touro"));
		dataCompraColumn.setCellFactory(new TableCellDateFactory<Semen, LocalDate>("dataCompra"));
		valorUnitarioColumn.setCellValueFactory(new PropertyDecimalValueFactory<Semen,String>("valorUnitario"));
		valorTotalColumn.setCellValueFactory(new PropertyDecimalValueFactory<Semen,String>("valorTotal"));
		quantidadeAdquiridaColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("quantidade"));
		quantidadeDisponivelColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("quantidadeDisponivel"));
		
		super.initialize((SemenFormController)MainApp.getBean(SemenFormController.class));
		
	}

	@Override
	public String getFormName() {
		return "view/semen/SemenOverview.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Sêmen";
	}

	@Override
	@Resource(name = "semenService")
	protected void setService(IService<Integer, Semen> service) {
		super.setService(service);
	}
	
}