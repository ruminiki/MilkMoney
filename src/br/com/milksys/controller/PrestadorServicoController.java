package br.com.milksys.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.PrestadorServico;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;

@Controller
public class PrestadorServicoController extends AbstractController<Integer, PrestadorServico> {

	@FXML private TableColumn<PrestadorServico, String> nomeColumn;
	@FXML private TableColumn<PrestadorServico, String> telefonePrincipalColumn;
	@FXML private TableColumn<PrestadorServico, String> telefoneSecundarioColumn;
	@FXML private TableColumn<PrestadorServico, String> emailColumn;
	@FXML private TableColumn<PrestadorServico, String> enderecoColumn;
	
	@FXML private UCTextField inputNome;
	@FXML private UCTextField inputTelefonePrincipal;
	@FXML private UCTextField inputTelefoneSecundario;
	@FXML private UCTextField inputEmail;
	@FXML private UCTextField inputCpf;
	@FXML private UCTextField inputEndereco;

	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			nomeColumn.setCellValueFactory(new PropertyValueFactory<PrestadorServico,String>("nome"));
			telefonePrincipalColumn.setCellValueFactory(new PropertyValueFactory<PrestadorServico,String>("telefonePrincipal"));
			telefoneSecundarioColumn.setCellValueFactory(new PropertyValueFactory<PrestadorServico,String>("telefoneSecundario"));
			emailColumn.setCellValueFactory(new PropertyValueFactory<PrestadorServico,String>("email"));
			enderecoColumn.setCellValueFactory(new PropertyValueFactory<PrestadorServico,String>("endereco"));
			super.initialize();
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
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
		
	}

	@Override
	protected boolean isInputValid() {
		return true;
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
	protected PrestadorServico getObject() {
		return (PrestadorServico)super.object;
	}

	@Override
	@Resource(name = "prestadorServicoService")
	protected void setService(IService<Integer, PrestadorServico> service) {
		super.setService(service);
	}

}
