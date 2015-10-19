package br.com.milkmoney.controller.compra;

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
import br.com.milkmoney.model.Compra;
import br.com.milkmoney.model.Fornecedor;
import br.com.milkmoney.service.IService;

@Controller
public class CompraOverviewController extends AbstractOverviewController<Integer, Compra> {

	@FXML private TableColumn<Compra, Date>       dataCompraColumn;
	@FXML private TableColumn<Compra, Date>       dataVencimentoColumn;
	@FXML private TableColumn<Fornecedor, String> fornecedorColumn;
	@FXML private TableColumn<Compra, String>     notaFiscalColumn;
	@FXML private TableColumn<Compra, String>     observacaoColumn;
	@FXML private TableColumn<Compra, String>     formaPagamentoColumn;
	@FXML private TableColumn<Compra, String>     valorTotalColumn;
	
	@FXML
	public void initialize() {
		
		dataCompraColumn.setCellFactory(new TableCellDateFactory<Compra,Date>("data"));
		dataVencimentoColumn.setCellFactory(new TableCellDateFactory<Compra,Date>("dataVencimento"));
		fornecedorColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("fornecedor"));
		notaFiscalColumn.setCellValueFactory(new PropertyValueFactory<Compra,String>("notaFiscal"));
		formaPagamentoColumn.setCellValueFactory(new PropertyValueFactory<Compra,String>("formaPagamento"));
		valorTotalColumn.setCellValueFactory(new PropertyDecimalValueFactory<Compra,String>("valorTotal", 2));
		observacaoColumn.setCellValueFactory(new PropertyValueFactory<Compra,String>("observacao"));
		
		super.initialize((CompraFormController)MainApp.getBean(CompraFormController.class));
		
	}
	
	@Override
	public String getFormTitle() {
		return "Compra Insumos";
	}
	
	@Override
	public String getFormName() {
		return "view/compra/CompraOverview.fxml";
	}
	
	@Override
	@Resource(name = "compraService")
	protected void setService(IService<Integer, Compra> service) {
		super.setService(service);
	}
	
}
