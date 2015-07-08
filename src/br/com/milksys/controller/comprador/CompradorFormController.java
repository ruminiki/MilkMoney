package br.com.milksys.controller.comprador;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.model.Comprador;
import br.com.milksys.service.IService;

@Controller
public class CompradorFormController extends AbstractFormController<Integer, Comprador> {

	@FXML private UCTextField inputNome;
	@FXML private UCTextField inputEmail;
	@FXML private UCTextField inputTelefonePrincipal;
	@FXML private UCTextField inputTelefoneSecundario;
	@FXML private UCTextField inputCpfCnpj;
	@FXML private UCTextField inputEndereco;
	@FXML private UCTextField inputSite;
	@FXML private UCTextField inputObservacao;

	@FXML
	public void initialize() {
		
		inputNome.textProperty().bindBidirectional(getObject().nomeProperty());
		inputTelefonePrincipal.textProperty().bindBidirectional(getObject().telefonePrincipalProperty());
		inputTelefoneSecundario.textProperty().bindBidirectional(getObject().telefoneSecundarioProperty());
		inputEmail.textProperty().bindBidirectional(getObject().emailProperty());
		inputCpfCnpj.textProperty().bindBidirectional(getObject().cpfCnpfProperty());
		inputEndereco.textProperty().bindBidirectional(getObject().enderecoProperty());
		inputSite.textProperty().bindBidirectional(getObject().siteProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		
		MaskFieldUtil.telefone(inputTelefonePrincipal);
		MaskFieldUtil.telefone(inputTelefoneSecundario);
		MaskFieldUtil.cpfCnpj(inputCpfCnpj);
	
	}

	@Override
	protected String getFormName() {
		return "view/comprador/CompradorForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Comprador";
	}

	@Override
	@Resource(name = "compradorService")
	protected void setService(IService<Integer, Comprador> service) {
		super.setService(service);
	}

}
