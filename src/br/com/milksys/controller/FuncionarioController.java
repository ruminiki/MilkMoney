package br.com.milksys.controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.PropertyDecimalValueFactory;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Funcionario;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;

@Controller
public class FuncionarioController extends AbstractController<Integer, Funcionario> {

	@FXML private TableColumn<Funcionario, String> nomeColumn;
	@FXML private TableColumn<Funcionario, String> telefoneColumn;
	@FXML private TableColumn<Funcionario, String> emailColumn;
	@FXML private TableColumn<Funcionario, String> jornadaTrabalhoColumn;
	@FXML private TableColumn<Funcionario, String> dataContratacaoColumn;
	@FXML private TableColumn<Funcionario, String> diaPagamentoColumn;
	@FXML private TableColumn<Funcionario, String> salarioColumn;
	
	@FXML private UCTextField inputNome;
	@FXML private UCTextField inputTelefone;
	@FXML private UCTextField inputEmail;
	@FXML private UCTextField inputJornadaTrabalho;
	@FXML private DatePicker  inputDataContratacao;
	@FXML private TextField   inputDiaPagamento;
	@FXML private TextField   inputSalario;

	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			nomeColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("nome"));
			telefoneColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("telefone"));
			emailColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("email"));
			jornadaTrabalhoColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("jornadaTrabalho"));
			dataContratacaoColumn.setCellFactory(new TableCellDateFactory<Funcionario,String>("dataContratacao"));
			diaPagamentoColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("diaPagamento"));
			salarioColumn.setCellValueFactory(new PropertyDecimalValueFactory<Funcionario,String>("salario"));
			super.initialize();
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			inputNome.textProperty().bindBidirectional(getObject().nomeProperty());
			inputTelefone.textProperty().bindBidirectional(getObject().telefoneProperty());
			inputEmail.textProperty().bindBidirectional(getObject().emailProperty());
			inputJornadaTrabalho.textProperty().bindBidirectional(getObject().jornadaTrabalhoProperty());
			inputDataContratacao.valueProperty().bindBidirectional(getObject().dataContratacaoProperty());
			inputDiaPagamento.textProperty().bindBidirectional(getObject().diaPagamentoProperty());
			inputSalario.textProperty().bindBidirectional(getObject().salarioProperty());
			MaskFieldUtil.telefone(inputTelefone);
			MaskFieldUtil.jornada(inputJornadaTrabalho);
			MaskFieldUtil.numeroInteiro(inputDiaPagamento);
			MaskFieldUtil.moeda(inputSalario);
		}
		
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
