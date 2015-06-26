package br.com.milksys.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;

@Controller
public class SituacaoCoberturaController extends AbstractController<Integer, SituacaoCobertura> {

	@FXML private TableColumn<SituacaoCobertura, String> idColumn;
	@FXML private TableColumn<SituacaoCobertura, String> descricaoColumn;
	@FXML private TextField inputId;
	@FXML private UCTextField inputDescricao;

	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			idColumn.setCellValueFactory(new PropertyValueFactory<SituacaoCobertura, String>("id"));
			descricaoColumn.setCellValueFactory(new PropertyValueFactory<SituacaoCobertura, String>("descricao"));
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
		return "view/situacaoCobertura/SituacaoCoberturaForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Situação Cobertura";
	}
	
	@Override
	protected SituacaoCobertura getObject() {
		return (SituacaoCobertura) super.object;
	}

	@Override
	@Resource(name = "situacaoCoberturaService")
	protected void setService(IService<Integer, SituacaoCobertura> service) {
		super.setService(service);
	}

}
