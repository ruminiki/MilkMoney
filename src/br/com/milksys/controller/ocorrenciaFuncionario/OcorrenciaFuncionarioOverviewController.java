package br.com.milksys.controller.ocorrenciaFuncionario;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.controller.motivoOcorrenciaFuncionario.MotivoOcorrenciaFuncionarioOverviewController;
import br.com.milksys.model.Funcionario;
import br.com.milksys.model.MotivoOcorrenciaFuncionario;
import br.com.milksys.model.OcorrenciaFuncionario;
import br.com.milksys.service.FuncionarioService;
import br.com.milksys.service.IService;
import br.com.milksys.service.MotivoOcorrenciaFuncionarioService;
import br.com.milksys.service.OcorrenciaFuncionarioService;

@Controller
public class OcorrenciaFuncionarioOverviewController extends AbstractOverviewController<Integer, OcorrenciaFuncionario> {

	@FXML private TableColumn<Funcionario, String> funcionarioColumn;
	@FXML private TableColumn<MotivoOcorrenciaFuncionario, String> motivoColumn;
	@FXML private TableColumn<OcorrenciaFuncionario, LocalDate> dataColumn;
	@FXML private TableColumn<OcorrenciaFuncionario, String> descricaoColumn;
	
	@Autowired private FuncionarioService funcionarioService;
	@Autowired private OcorrenciaFuncionarioService ocorrenciaFuncionarioService;
	@Autowired private MotivoOcorrenciaFuncionarioService motivoOcorrenciaFuncionarioService;
	@Autowired private MotivoOcorrenciaFuncionarioOverviewController motivoOcorrenciaFuncionarioController;
	
	private Funcionario selectedFuncionario;
	
	@FXML
	public void initialize() {
		
		funcionarioColumn.setCellValueFactory(new PropertyValueFactory<Funcionario, String>("funcionario"));
		motivoColumn.setCellValueFactory(new PropertyValueFactory<MotivoOcorrenciaFuncionario, String>("motivoOcorrenciaFuncionario"));
		dataColumn.setCellFactory(new TableCellDateFactory<OcorrenciaFuncionario, LocalDate>("data"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<OcorrenciaFuncionario, String>("descricao"));
		super.initialize((OcorrenciaFuncionarioFormController) MainApp.getBean(OcorrenciaFuncionarioFormController.class));
	
	}
	
	public Funcionario getSelectedFuncionario() {
		return selectedFuncionario;
	}

	public void setSelectedFuncionario(Funcionario selectedFuncionario) {
		this.selectedFuncionario = selectedFuncionario;
	}
	
	@Override
	public void handleNew() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		super.handleNew();
	}
	
	@Override
	public void refreshTableOverview() {
		data.clear();
		data.addAll(ocorrenciaFuncionarioService.findByFuncionario(getSelectedFuncionario()));
		updateLabelNumRegistros();
	}

	@Override
	protected String getFormTitle() {
		return "Ocorrências Funcionário";
	}
	
	@Override
	public String getFormName() {
		return "view/ocorrenciaFuncionario/OcorrenciaFuncionarioOverview.fxml";
	}
	
	@Override
	@Resource(name = "ocorrenciaFuncionarioService")
	protected void setService(IService<Integer, OcorrenciaFuncionario> service) {
		super.setService(service);
	}
	
}
