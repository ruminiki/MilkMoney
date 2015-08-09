package br.com.milkmoney.controller.precoLeite;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.NumberTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.PrecoLeite;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.PrecoLeiteService;

@Controller
public class PrecoLeiteFormController extends AbstractFormController<Integer, PrecoLeite> {

	@FXML private NumberTextField inputValorMaximoPraticado;
	@FXML private NumberTextField inputValorRecebido;
	@FXML private TextField inputMesReferencia;
	@FXML private TextField inputAnoReferencia;
	
	@Autowired private PrecoLeiteService service;
	
	@FXML
	public void initialize() {
		
		inputMesReferencia.textProperty().bindBidirectional(getObject().mesReferenciaProperty());
		inputAnoReferencia.textProperty().bindBidirectional(getObject().anoReferenciaProperty());
		inputValorMaximoPraticado.textProperty().bindBidirectional(getObject().valorMaximoPraticadoProperty());
		inputValorRecebido.textProperty().bindBidirectional(getObject().valorRecebidoProperty());
		
		MaskFieldUtil.decimal(inputValorMaximoPraticado);
		MaskFieldUtil.decimal(inputValorRecebido);
		
	}

	@Override
	public String getFormName() {
		return "view/precoLeite/PrecoLeiteForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Preço Leite";
	}
	
	@Override
	@Resource(name = "precoLeiteService")
	protected void setService(IService<Integer, PrecoLeite> service) {
		super.setService(service);
	}

}
