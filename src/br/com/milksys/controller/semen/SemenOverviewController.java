package br.com.milksys.controller.semen;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.components.PropertyDecimalValueFactory;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.model.Semen;
import br.com.milksys.service.IService;

@Controller
public class SemenOverviewController extends AbstractOverviewController<Integer, Semen> {

	@FXML private TableColumn<Semen, String> descricaoColumn;
	@FXML private TableColumn<Semen, String> touroColumn;
	@FXML private TableColumn<Semen, LocalDate> dataCompraColumn;
	@FXML private TableColumn<Semen, String> valorUnitarioColumn;
	@FXML private TableColumn<Semen, String> valorTotalColumn;
	@FXML private TableColumn<Semen, String> quantidadeAdquiridaColumn;
	@FXML private TableColumn<Semen, String> quantidadeDisponivelColumn;
	@FXML private TableColumn<Semen, String> loteColumn;
	
	@FXML
	public void initialize() {
		
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("descricao"));
		touroColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("touro"));
		dataCompraColumn.setCellFactory(new TableCellDateFactory<Semen, LocalDate>("dataCompra"));
		valorUnitarioColumn.setCellValueFactory(new PropertyDecimalValueFactory<Semen,String>("valorUnitario"));
		valorTotalColumn.setCellValueFactory(new PropertyDecimalValueFactory<Semen,String>("valorTotal"));
		quantidadeAdquiridaColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("quantidade"));
		quantidadeDisponivelColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("quantidadeDisponivel"));
		loteColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("lote"));
		
		super.initialize((SemenFormController)MainApp.getBean(SemenFormController.class));
		
	}

	@Override
	protected String getFormName() {
		return "view/semen/SemenOverview.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Sêmen";
	}

	@Override
	@Resource(name = "semenService")
	protected void setService(IService<Integer, Semen> service) {
		super.setService(service);
	}
	
}