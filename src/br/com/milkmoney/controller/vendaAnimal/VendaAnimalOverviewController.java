package br.com.milkmoney.controller.vendaAnimal;

import java.util.Date;
import java.util.function.Function;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.PropertyDecimalValueFactory;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.components.TableCellHyperlinkFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.Comprador;
import br.com.milkmoney.model.VendaAnimal;
import br.com.milkmoney.service.IService;

@Controller
public class VendaAnimalOverviewController extends AbstractOverviewController<Integer, VendaAnimal> {

	@FXML private TableColumn<VendaAnimal, Date> dataVendaColumn;
	@FXML private TableColumn<VendaAnimal, Date> dataPrevisaoRecebimentoColumn;
	
	@FXML private TableColumn<VendaAnimal, Integer> quantidadeAnimaisVendidosColumn;
	@FXML private TableColumn<Comprador, String> compradorColumn;
	@FXML private TableColumn<VendaAnimal, String> valorTotalColumn;
	
	@FXML
	public void initialize() {
		
		dataVendaColumn.setCellFactory(new TableCellDateFactory<VendaAnimal,Date>("dataVenda"));
		dataPrevisaoRecebimentoColumn.setCellFactory(new TableCellDateFactory<VendaAnimal,Date>("dataPrevisaoRecebimento"));
		
		quantidadeAnimaisVendidosColumn.setCellValueFactory(new PropertyValueFactory<VendaAnimal,Integer>("quantidadeAnimaisVendidos"));
		quantidadeAnimaisVendidosColumn.setCellFactory(new TableCellHyperlinkFactory<VendaAnimal,Integer>(showAnimaisVenda));
		compradorColumn.setCellValueFactory(new PropertyValueFactory<Comprador,String>("comprador"));
		valorTotalColumn.setCellValueFactory(new PropertyDecimalValueFactory<VendaAnimal,String>("valorTotal"));
		
		super.initialize((VendaAnimalFormController)MainApp.getBean(VendaAnimalFormController.class));
		
	}
	
	Function<Integer, Boolean> showAnimaisVenda = index -> {
		
		if ( index <= table.getItems().size() ){
		
			CustomAlert.mensagemInfo("Hello");
			
			return true;
			
		}
		
		return false;
		
	};

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
