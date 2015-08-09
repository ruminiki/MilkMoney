package br.com.milkmoney.controller.funcionario;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.PropertyDecimalValueFactory;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.ocorrenciaFuncionario.OcorrenciaFuncionarioOverviewController;
import br.com.milkmoney.model.Funcionario;
import br.com.milkmoney.model.OcorrenciaFuncionario;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.IService;

@Controller
public class FuncionarioOverviewController extends AbstractOverviewController<Integer, Funcionario> {

	@FXML private TableColumn<Funcionario, String> nomeColumn;
	@FXML private TableColumn<Funcionario, String> telefonePrincipalColumn;
	@FXML private TableColumn<Funcionario, String> telefoneSecundarioColumn;
	@FXML private TableColumn<Funcionario, String> emailColumn;
	@FXML private TableColumn<Funcionario, String> jornadaTrabalhoColumn;
	@FXML private TableColumn<Funcionario, String> dataContratacaoColumn;
	@FXML private TableColumn<Funcionario, String> diaPagamentoColumn;
	@FXML private TableColumn<Funcionario, String> salarioColumn;
	
	@Autowired private OcorrenciaFuncionarioOverviewController ocorrenciaFuncionarioOverviewController;

	@FXML
	public void initialize() {
		
		nomeColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("nome"));
		telefonePrincipalColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("telefonePrincipal"));
		telefoneSecundarioColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("telefoneSecundario"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("email"));
		jornadaTrabalhoColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("jornadaTrabalho"));
		dataContratacaoColumn.setCellFactory(new TableCellDateFactory<Funcionario,String>("dataContratacao"));
		diaPagamentoColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("diaPagamento"));
		salarioColumn.setCellValueFactory(new PropertyDecimalValueFactory<Funcionario,String>("salario"));
		super.initialize((FuncionarioFormController) MainApp.getBean(FuncionarioFormController.class));
		
	}
	
	@FXML
	private void openFormOcorrencias(){
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return;
		}
		ocorrenciaFuncionarioOverviewController.setState(State.LIST);
		ocorrenciaFuncionarioOverviewController.setSelectedFuncionario(getObject());
		ocorrenciaFuncionarioOverviewController.setObject(new OcorrenciaFuncionario(getObject()));
		ocorrenciaFuncionarioOverviewController.showForm();
	}

	@Override
	public String getFormName() {
		return "view/funcionario/FuncionarioOverview.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Funcionário";
	}
	
	@Override
	@Resource(name = "funcionarioService")
	protected void setService(IService<Integer, Funcionario> service) {
		super.setService(service);
	}

}
