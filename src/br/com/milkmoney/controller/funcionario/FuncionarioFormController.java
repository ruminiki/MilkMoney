package br.com.milkmoney.controller.funcionario;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.Funcionario;
import br.com.milkmoney.service.IService;

@Controller
public class FuncionarioFormController extends AbstractFormController<Integer, Funcionario> {

	@FXML private UCTextField inputNome;
	@FXML private UCTextField inputTelefonePrincipal;
	@FXML private UCTextField inputTelefoneSecundario;
	@FXML private UCTextField inputEmail;
	@FXML private UCTextField inputJornadaTrabalho;
	@FXML private DatePicker  inputDataContratacao;
	@FXML private UCTextField inputDiaPagamento;
	@FXML private UCTextField inputSalario;
	
	@FXML
	public void initialize() {
		
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
		MaskFieldUtil.decimal(inputSalario);
	
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
	@Resource(name = "funcionarioService")
	protected void setService(IService<Integer, Funcionario> service) {
		super.setService(service);
	}

}
