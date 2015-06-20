package br.com.milksys.controller;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.NumberTextField;
import br.com.milksys.model.PrecoLeite;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;
import br.com.milksys.util.NumberFormatUtil;
import br.com.milksys.util.Util;

@Controller
public class PrecoLeiteController extends AbstractController<Integer, PrecoLeite> {

	@FXML private TableColumn<PrecoLeite, String> mesReferenciaColumn;
	@FXML private TableColumn<PrecoLeite, String> anoReferenciaColumn;
	@FXML private TableColumn<PrecoLeite, String> valorColumn;
	@FXML private NumberTextField inputValor;
	@FXML private ComboBox<String> inputMesReferencia;
	@FXML private ComboBox<Number> inputAnoReferencia;
	private ObservableList<String> meses = Util.generateListMonths();
	private ObservableList<Number> anos = Util.generateListNumbers(1980, LocalDate.now().getYear());

	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			mesReferenciaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMesReferencia()));
			anoReferenciaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAnoReferencia())));
			valorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValor())));
			super.initialize();
			
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			
			if (state.equals(State.INSERT)){
				((PrecoLeite)object).setAnoReferencia(LocalDate.now().getYear());
				((PrecoLeite)object).setMesReferencia(meses.get(LocalDate.now().getMonthValue()-1));	
			}
			
			inputMesReferencia.setItems(meses);
			inputMesReferencia.getSelectionModel().select(LocalDate.now().getMonthValue()-1);
			inputMesReferencia.valueProperty().bindBidirectional(((PrecoLeite)object).mesReferenciaProperty());
			
			inputAnoReferencia.setItems(anos);
			inputAnoReferencia.getSelectionModel().selectLast();
			inputAnoReferencia.valueProperty().bindBidirectional(((PrecoLeite)object).anoReferenciaProperty());
			
			inputValor.textProperty().bindBidirectional(((PrecoLeite)object).valorProperty());
			
		}
		
	}

	@Override
	protected boolean isInputValid() {
		return true;
	}

	@Override
	protected String getFormName() {
		return "view/precoLeite/PrecoLeiteForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Preço Leite";
	}

	@Override
	@Resource(name = "precoLeiteService")
	protected void setService(IService<Integer, PrecoLeite> service) {
		super.setService(service);
	}

}
