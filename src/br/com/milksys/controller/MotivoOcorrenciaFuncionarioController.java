package br.com.milksys.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.model.MotivoOcorrenciaFuncionario;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;

@Controller
public class MotivoOcorrenciaFuncionarioController extends AbstractController<Integer, MotivoOcorrenciaFuncionario> {

	@FXML private TableColumn<MotivoOcorrenciaFuncionario, String> idColumn;
	@FXML private TableColumn<MotivoOcorrenciaFuncionario, String> descricaoColumn;
	@FXML private UCTextField inputDescricao;

	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			idColumn.setCellValueFactory(new PropertyValueFactory<MotivoOcorrenciaFuncionario,String>("id"));
			descricaoColumn.setCellValueFactory(new PropertyValueFactory<MotivoOcorrenciaFuncionario,String>("descricao"));
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
		return "view/motivoOcorrenciaFuncionario/MotivoOcorrenciaFuncionarioForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Motivo Ocorrência Funcionário";
	}
	
	@Override
	protected MotivoOcorrenciaFuncionario getObject() {
		return (MotivoOcorrenciaFuncionario) super.object;
	}

	@Override
	@Resource(name = "motivoOcorrenciaFuncionarioService")
	protected void setService(IService<Integer, MotivoOcorrenciaFuncionario> service) {
		super.setService(service);
	}

}
