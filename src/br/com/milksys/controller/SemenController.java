package br.com.milksys.controller;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.NumberTextField;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Semen;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;
import br.com.milksys.util.NumberFormatUtil;

@Controller
public class SemenController extends AbstractController<Integer, Semen> {

	@FXML private TableColumn<Semen, String> descricaoColumn;
	@FXML private TableColumn<Semen, String> touroColumn;
	@FXML private TableColumn<Semen, LocalDate> dataCompraColumn;
	@FXML private TableColumn<Semen, String> valorUnitarioColumn;
	@FXML private TableColumn<Semen, String> valorTotalColumn;
	@FXML private TableColumn<Semen, String> quantidadeColumn;
	@FXML private TableColumn<Semen, String> loteColumn;
	
	@FXML private UCTextField inputDescricao;
	@FXML private UCTextField inputTouro;
	@FXML private DatePicker inputDataCompra;
	@FXML private NumberTextField inputValor;
	@FXML private NumberTextField inputQuantidade;
	@FXML private UCTextField inputLote;

	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			/*descricaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescricao()));
			dataCompraColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getDataCompra())));
			valorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValor())));
			quantidadeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getQuantidade())));
			loteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLote()));*/
			
			descricaoColumn.setCellValueFactory(cellData -> cellData.getValue().descricaoProperty());
			touroColumn.setCellValueFactory(cellData -> cellData.getValue().touroProperty());
			dataCompraColumn.setCellValueFactory(cellData -> cellData.getValue().dataCompraProperty());
			valorUnitarioColumn.setCellValueFactory(cellData -> cellData.getValue().valorUnitarioProperty());
			valorTotalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValorTotal())));
			quantidadeColumn.setCellValueFactory(cellData -> cellData.getValue().quantidadeProperty());
			loteColumn.setCellValueFactory(cellData -> cellData.getValue().loteProperty());
			
			super.initialize();
			
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			
			inputDescricao.textProperty().bindBidirectional(((Semen)object).descricaoProperty());
			inputTouro.textProperty().bindBidirectional(((Semen)object).touroProperty());
			inputDataCompra.valueProperty().bindBidirectional(((Semen)object).dataCompraProperty());
			inputValor.textProperty().bindBidirectional(((Semen)object).valorUnitarioProperty());
			inputQuantidade.textProperty().bindBidirectional(((Semen)object).quantidadeProperty());
			inputLote.textProperty().bindBidirectional(((Semen)object).loteProperty());
			
		}
		
	}

	@Override
	protected boolean isInputValid() {
		return true;
	}

	@Override
	protected String getFormName() {
		return "view/semen/SemenForm.fxml";
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
