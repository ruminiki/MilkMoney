package br.com.milkmoney.controller.vendaAnimal;

import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.PropertyDecimalValueFactory;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Comprador;
import br.com.milkmoney.model.VendaAnimal;
import br.com.milkmoney.service.IService;

@Controller
public class VendaAnimalOverviewController extends AbstractOverviewController<Integer, VendaAnimal> {

	@FXML private TableColumn<VendaAnimal, Date> dataVendaColumn;
	@FXML private TableColumn<Animal, String> animalColumn;
	@FXML private TableColumn<VendaAnimal, String> motivoVendaColumn;
	@FXML private TableColumn<VendaAnimal, String> destinacaoAnimalColumn;
	@FXML private TableColumn<Comprador, String> compradorColumn;
	@FXML private TableColumn<VendaAnimal, String> valorColumn;
	
	@FXML
	public void initialize() {
		
		dataVendaColumn.setCellFactory(new TableCellDateFactory<VendaAnimal,Date>("dataVenda"));
		animalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("animal"));
		motivoVendaColumn.setCellValueFactory(new PropertyValueFactory<VendaAnimal,String>("motivoVendaAnimal"));
		destinacaoAnimalColumn.setCellValueFactory(new PropertyValueFactory<VendaAnimal,String>("destinacaoAnimal"));
		compradorColumn.setCellValueFactory(new PropertyValueFactory<Comprador,String>("comprador"));
		valorColumn.setCellValueFactory(new PropertyDecimalValueFactory<VendaAnimal,String>("valorAnimal", 2));
		
		super.initialize((VendaAnimalFormController)MainApp.getBean(VendaAnimalFormController.class));
		
	}
	
	@Override
	public String getFormTitle() {
		return "Venda Animal";
	}
	
	@Override
	public String getFormName() {
		return "view/vendaAnimal/VendaAnimalOverview.fxml";
	}
	
	@Override
	@Resource(name = "vendaAnimalService")
	protected void setService(IService<Integer, VendaAnimal> service) {
		super.setService(service);
	}
	
}
