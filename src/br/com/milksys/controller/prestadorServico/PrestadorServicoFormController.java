package br.com.milksys.controller.prestadorServico;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.model.PrestadorServico;
import br.com.milksys.service.IService;

@Controller
public class PrestadorServicoFormController extends AbstractFormController<Integer, PrestadorServico> {

	@FXML private UCTextField inputNome;
	@FXML private UCTextField inputTelefonePrincipal;
	@FXML private UCTextField inputTelefoneSecundario;
	@FXML private UCTextField inputEmail;
	@FXML private UCTextField inputCpf;
	@FXML private UCTextField inputEndereco;

	@FXML
	public void initialize() {
		inputNome.textProperty().bindBidirectional(getObject().nomeProperty());
		inputTelefonePrincipal.textProperty().bindBidirectional(getObject().telefonePrincipalProperty());
		inputTelefoneSecundario.textProperty().bindBidirectional(getObject().telefoneSecundarioProperty());
		inputEmail.textProperty().bindBidirectional(getObject().emailProperty());
		inputCpf.textProperty().bindBidirectional(getObject().cpfProperty());
		inputEndereco.textProperty().bindBidirectional(getObject().enderecoProperty());
		
		MaskFieldUtil.telefone(inputTelefonePrincipal);
		MaskFieldUtil.telefone(inputTelefoneSecundario);
		MaskFieldUtil.cpf(inputCpf);
	}

	@Override
	protected String getFormName() {
		return "view/prestadorServico/PrestadorServicoForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Prestador de Serviço";
	}

	@Override
	@Resource(name = "prestadorServicoService")
	protected void setService(IService<Integer, PrestadorServico> service) {
		super.setService(service);
	}

}
