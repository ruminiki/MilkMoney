package br.com.milkmoney.controller.funcionario;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.controller.AbstractReducedOverviewController;
import br.com.milkmoney.model.Funcionario;
import br.com.milkmoney.service.IService;

@Controller
public class FuncionarioReducedController extends AbstractReducedOverviewController<Integer, Funcionario> {

	@FXML private TableColumn<Funcionario, String> nomeColumn;
	@FXML private TableColumn<Funcionario, String> telefoneColumn;
	@FXML private TableColumn<Funcionario, String> emailColumn;
	
	@Autowired private FuncionarioFormController funcionarioFormController;
	
	@FXML
	public void initialize() {
		
		nomeColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("nome"));
		telefoneColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("telefone"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("email"));
		
		super.initialize(funcionarioFormController);
		
	}

	@Override
	public String getFormName() {
		return "view/funcionario/FuncionarioReducedOverview.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Funcionário";
	}
	
	@Override
	@Resource(name = "funcionarioService")
	protected void setService(IService<Integer, Funcionario> service) {
		super.setService(service);
	}

}
