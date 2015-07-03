package br.com.milksys.controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.CustomAlert;
import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.PropertyDecimalValueFactory;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Funcionario;
import br.com.milksys.model.OcorrenciaFuncionario;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;

@Controller
public class FuncionarioController extends AbstractController<Integer, Funcionario> {

	@FXML private TableColumn<Funcionario, String> nomeColumn;
	@FXML private TableColumn<Funcionario, String> telefonePrincipalColumn;
	@FXML private TableColumn<Funcionario, String> telefoneSecundarioColumn;
	@FXML private TableColumn<Funcionario, String> emailColumn;
	@FXML private TableColumn<Funcionario, String> jornadaTrabalhoColumn;
	@FXML private TableColumn<Funcionario, String> dataContratacaoColumn;
	@FXML private TableColumn<Funcionario, String> diaPagamentoColumn;
	@FXML private TableColumn<Funcionario, String> salarioColumn;
	
	@FXML private UCTextField inputNome;
	@FXML private UCTextField inputTelefonePrincipal;
	@FXML private UCTextField inputTelefoneSecundario;
	@FXML private UCTextField inputEmail;
	@FXML private UCTextField inputJornadaTrabalho;
	@FXML private DatePicker  inputDataContratacao;
	@FXML private UCTextField inputDiaPagamento;
	@FXML private UCTextField inputSalario;
	
	@Autowired private OcorrenciaFuncionarioController ocorrenciaFuncionarioController;

	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			nomeColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("nome"));
			telefonePrincipalColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("telefonePrincipal"));
			telefoneSecundarioColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("telefoneSecundario"));
			emailColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("email"));
			jornadaTrabalhoColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("jornadaTrabalho"));
			dataContratacaoColumn.setCellFactory(new TableCellDateFactory<Funcionario,String>("dataContratacao"));
			diaPagamentoColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("diaPagamento"));
			salarioColumn.setCellValueFactory(new PropertyDecimalValueFactory<Funcionario,String>("salario"));
			super.initialize();
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			inputNome.textProperty().bindBidirectional(getObject().nomeProperty());
			inputTelefonePrincipal.textProperty().bindBidirectional(getObject().telefonePrincipalProperty());
			inputTelefoneSecundario.textProperty().bindBidirectional(getObject().telefoneSecundarioProperty());
			inputEmail.textProperty().bindBidirectional(getObject().emailProperty());
			inputJornadaTrabalho.textProperty().bindBidirectional(getObject().jornadaTrabalhoProperty());
			inputDataContratacao.valueProperty().bindBidirectional(getObject().dataContratacaoProperty());
			inputDiaPagamento.textProperty().bindBidirectional(getObject().diaPagamentoProperty());
			inputSalario.textProperty().bindBidirectional(getObject().salarioProperty());
			MaskFieldUtil.telefone(inputTelefonePrincipal);
			MaskFieldUtil.telefone(inputTelefoneSecundario);
			MaskFieldUtil.jornada(inputJornadaTrabalho);
			MaskFieldUtil.numeroInteiro(inputDiaPagamento);
			MaskFieldUtil.moeda(inputSalario);
		}
		
	}
	
	@FXML
	private void openFormOcorrencias(){
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return;
		}
		ocorrenciaFuncionarioController.state = State.LIST;
		ocorrenciaFuncionarioController.setSelectedFuncionario(getObject());
		ocorrenciaFuncionarioController.object = new OcorrenciaFuncionario(getObject());
		ocorrenciaFuncionarioController.showForm(ocorrenciaFuncionarioController.getFormOverviewName());
	}

	@Override
	protected String getFormName() {
		return "view/funcionario/FuncionarioForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Funcionário";
	}
	
	@Override
	protected Funcionario getObject() {
		return (Funcionario)super.object;
	}

	@Override
	@Resource(name = "funcionarioService")
	protected void setService(IService<Integer, Funcionario> service) {
		super.setService(service);
	}

}
