package br.com.milksys.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.model.State;
import br.com.milksys.model.TipoCobertura;
import br.com.milksys.service.IService;

@Controller
public class TipoCoberturaController extends AbstractController<Integer, TipoCobertura> {

	@FXML private TableColumn<TipoCobertura, String> idColumn;
	@FXML private TableColumn<TipoCobertura, String> descricaoColumn;
	@FXML private TextField idField;
	@FXML private UCTextField inputDescricao;

	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			idColumn.setCellValueFactory(new PropertyValueFactory<TipoCobertura, String>("id"));
			descricaoColumn.setCellValueFactory(new PropertyValueFactory<TipoCobertura, String>("descricao"));
			super.initialize();
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		}
		
	}

	@Override
	protected boolean isInputValid() {
		return true;
	}

	@Override
	protected String getFormName() {
		return "view/tipoCobertura/TipoCoberturaForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Tipo Cobertura";
	}

	@Override
	protected TipoCobertura getObject() {
		return (TipoCobertura) super.object;
	}
	
	@Override
	@Resource(name = "tipoCoberturaService")
	protected void setService(IService<Integer, TipoCobertura> service) {
		super.setService(service);
	}

}
