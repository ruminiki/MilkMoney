package br.com.milkmoney.controller.compra;

import java.util.function.Function;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.PropertyDecimalValueFactory;
import br.com.milkmoney.components.TableCellHyperlinkRemoverFactory;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.fornecedor.FornecedorReducedOverviewController;
import br.com.milkmoney.controller.insumo.InsumoReducedOverviewController;
import br.com.milkmoney.model.Compra;
import br.com.milkmoney.model.Fornecedor;
import br.com.milkmoney.model.Insumo;
import br.com.milkmoney.model.ItemCompra;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.util.NumberFormatUtil;
import br.com.milkmoney.validation.ItemCompraValidation;

@Controller
public class CompraFormController extends AbstractFormController<Integer, Compra> {

	//dados da compra
	@FXML private UCTextField inputFornecedor, inputObservacao, inputNotaFiscal;
	@FXML private DatePicker inputDataCompra, inputDataVencimento;
	
	//dados do insumo
	@FXML private UCTextField inputInsumo, inputValorUnitario, inputQuantidade, inputObservacaoInsumo; 
	
	@FXML private TableView<ItemCompra> table;
	@FXML private TableColumn<ItemCompra, String> insumoColumn;
	@FXML private TableColumn<ItemCompra, String> quantidadeColumn;
	@FXML private TableColumn<ItemCompra, String> valorUnitarioColumn;
	@FXML private TableColumn<ItemCompra, String> valorItemColumn;
	@FXML private TableColumn<ItemCompra, String> removerColumn;
	
	@FXML private Label lblTotalCompra;
	
	@Autowired private FornecedorReducedOverviewController fornecedorReducedOverviewController;
	@Autowired private InsumoReducedOverviewController insumoReducedOverviewController;

	private ItemCompra itemCompra = new ItemCompra(getObject());
	
	@FXML
	public void initialize() {
		
		//dados da compra
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		inputDataCompra.valueProperty().bindBidirectional(getObject().dataProperty());
		inputDataVencimento.valueProperty().bindBidirectional(getObject().dataVencimentoProperty());
		inputNotaFiscal.textProperty().bindBidirectional(getObject().notaFiscalProperty());
		
		if ( getObject().getFornecedor() != null ){
			inputFornecedor.textProperty().set(getObject().getFornecedor().toString());
		}
		
		//dados dos itens
		MaskFieldUtil.decimalWithoutMask(inputQuantidade);
		MaskFieldUtil.decimalWithoutMask(inputValorUnitario);
		
		//table animais vendidos
		insumoColumn.setCellValueFactory(new PropertyValueFactory<ItemCompra,String>("insumo"));
		quantidadeColumn.setCellValueFactory(new PropertyDecimalValueFactory<ItemCompra,String>("quantidade", 2));
		valorUnitarioColumn.setCellValueFactory(new PropertyDecimalValueFactory<ItemCompra,String>("valor", 2));
		valorItemColumn.setCellValueFactory(new PropertyDecimalValueFactory<ItemCompra,String>("valorItem", 2));
		removerColumn.setCellValueFactory(new PropertyValueFactory<ItemCompra,String>("insumo"));
		removerColumn.setCellFactory(new TableCellHyperlinkRemoverFactory<ItemCompra, String>(removerItemCompra, false));
		
		table.getItems().clear();
		table.getItems().addAll(getObject().getItensCompra());
		
		atualizaTotalCompra();
		
	}

	@FXML
	private void handleSelecionarFornecedor() {
		fornecedorReducedOverviewController.setObject(new Fornecedor());
		fornecedorReducedOverviewController.showForm();
		
		if ( fornecedorReducedOverviewController.getObject() != null && fornecedorReducedOverviewController.getObject().getId() > 0 ){
			getObject().setFornecedor(fornecedorReducedOverviewController.getObject());
		}
		
		if ( getObject().getFornecedor() != null ){
			inputFornecedor.textProperty().set(getObject().getFornecedor().toString());	
		}else{
			inputFornecedor.textProperty().set("");
		}
	}
	
	@FXML
	private void handleSelecionarInsumo() {
		
		insumoReducedOverviewController.setObject(new Insumo());
		insumoReducedOverviewController.showForm();
		
		if ( insumoReducedOverviewController.getObject() != null && insumoReducedOverviewController.getObject().getId() > 0 ){
			itemCompra.setInsumo(insumoReducedOverviewController.getObject());
		}
		
		if ( itemCompra.getInsumo() != null ){
			inputInsumo.textProperty().set(itemCompra.getInsumo().toString());	
		}else{
			inputInsumo.textProperty().set("");
		}
		
	}
	
	@FXML
	private void handleSelecionarFormaPagamento(){
		
		
	}
	
	@FXML 
	private void adicionarItemCompra(){
		
		itemCompra.setObservacao(inputObservacaoInsumo.getText());
		itemCompra.setValor(NumberFormatUtil.fromString(inputValorUnitario.getText()));
		itemCompra.setQuantidade(NumberFormatUtil.fromString(inputQuantidade.getText()));
		itemCompra.setCompra(getObject());
		
		ItemCompraValidation.validate(itemCompra);
		
		getObject().getItensCompra().add(itemCompra);
		table.getItems().add(itemCompra);
		
		atualizaTotalCompra();
		
		itemCompra = new ItemCompra(getObject());
		
		inputObservacaoInsumo.clear();
		inputValorUnitario.clear();
		inputQuantidade.clear();
		inputInsumo.clear();
		
	}
	
	private void atualizaTotalCompra(){
		lblTotalCompra.setText("R$ " + NumberFormatUtil.decimalFormat(getObject().getValorTotal()));
	}
	
	Function<Integer, Boolean> removerItemCompra = index -> {
		
		if ( index <= table.getItems().size() ){
			
			if ( !getObject().getItensCompra().remove(index) ){
				getObject().getItensCompra().remove(table.getItems().get(index));
			}
			table.getItems().clear();
			table.getItems().addAll(getObject().getItensCompra());
			
			atualizaTotalCompra();
			
			return true;
			
		}
		
		return false;
		
	};
	
	public ItemCompra getItemCompra() {
		return itemCompra;
	}

	public void setItemCompra(ItemCompra itemCompra) {
		this.itemCompra = itemCompra;
	}

	@Override
	public String getFormName() {
		return "view/compra/CompraForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Compra de Insumos";
	}
	
	@Override
	@Resource(name = "compraService")
	protected void setService(IService<Integer, Compra> service) {
		super.setService(service);
	}

	
}
