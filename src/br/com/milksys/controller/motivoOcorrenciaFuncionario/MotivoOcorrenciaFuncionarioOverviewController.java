package br.com.milksys.controller.motivoOcorrenciaFuncionario;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.model.MotivoOcorrenciaFuncionario;
import br.com.milksys.service.IService;

@Controller
public class MotivoOcorrenciaFuncionarioOverviewController extends AbstractOverviewController<Integer, MotivoOcorrenciaFuncionario> {

	@FXML private TableColumn<MotivoOcorrenciaFuncionario, String> idColumn;
	@FXML private TableColumn<MotivoOcorrenciaFuncionario, String> descricaoColumn;

	@FXML
	public void initialize() {
		
		idColumn.setCellValueFactory(new PropertyValueFactory<MotivoOcorrenciaFuncionario,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<MotivoOcorrenciaFuncionario,String>("descricao"));
		super.initialize((MotivoOcorrenciaFuncionarioFormController) MainApp.getBean(MotivoOcorrenciaFuncionarioFormController.class));
		
	}

	@Override
	protected String getFormTitle() {
		return "Motivo Ocorrência Funcionário";
	}
	
	@Override
	protected String getFormName() {
		return "view/motivoOcorrenciaFuncionario/MotivoOcorrenciaFuncionarioOverview.fxml";
	}

	@Override
	@Resource(name = "motivoOcorrenciaFuncionarioService")
	protected void setService(IService<Integer, MotivoOcorrenciaFuncionario> service) {
		super.setService(service);
	}

}
