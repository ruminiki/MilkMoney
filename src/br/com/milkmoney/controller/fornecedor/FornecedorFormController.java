package br.com.milkmoney.controller.fornecedor;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.Fornecedor;
import br.com.milkmoney.service.IService;

@Controller
public class FornecedorFormController extends AbstractFormController<Integer, Fornecedor> {

	@FXML private UCTextField inputNome;
	@FXML private UCTextField inputEmail;
	@FXML private UCTextField inputTelefonePrincipal;
	@FXML private UCTextField inputTelefoneSecundario;
	@FXML private UCTextField inputCpfCnpj;
	@FXML private UCTextField inputEndereco;
	@FXML private UCTextField inputSite;

	@FXML
	public void initialize() {
		
		inputNome.textProperty().bindBidirectional(getObject().nomeProperty());
		inputTelefonePrincipal.textProperty().bindBidirectional(getObject().telefonePrincipalProperty());
		inputTelefoneSecundario.textProperty().bindBidirectional(getObject().telefoneSecundarioProperty());
		inputEmail.textProperty().bindBidirectional(getObject().emailProperty());
		inputCpfCnpj.textProperty().bindBidirectional(getObject().cpfCnpfProperty());
		inputEndereco.textProperty().bindBidirectional(getObject().enderecoProperty());
		inputSite.textProperty().bindBidirectional(getObject().siteProperty());
		
		MaskFieldUtil.telefone(inputTelefonePrincipal);
		MaskFieldUtil.telefone(inputTelefoneSecundario);
		MaskFieldUtil.cpfCnpj(inputCpfCnpj);
	
	}

	@Override
	public String getFormName() {
		return "view/fornecedor/FornecedorForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Fornecedor";
	}

	@Override
	@Resource(name = "fornecedorService")
	protected void setService(IService<Integer, Fornecedor> service) {
		super.setService(service);
	}

}
