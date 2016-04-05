package br.com.milkmoney.controller.lote;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.finalidadeLote.FinalidadeLoteReducedOverviewController;
import br.com.milkmoney.model.FinalidadeLote;
import br.com.milkmoney.model.Lote;
import br.com.milkmoney.model.SimNao;
import br.com.milkmoney.service.IService;

@Controller
public class LoteFormController extends AbstractFormController<Integer, Lote>  {
	
	@FXML private UCTextField inputPesquisa, inputDescricao, inputFinalidade;
	@FXML private ComboBox<String> inputAtivo;
	@Autowired protected FinalidadeLoteReducedOverviewController finalidadeLoteReducedController;

	@FXML
	public void initialize() {
		
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputAtivo.setItems(SimNao.getItems());
		inputAtivo.valueProperty().bindBidirectional(getObject().ativoProperty());
		if ( getObject() != null && getObject().getFinalidadeLote() != null ){
			inputFinalidade.setText(getObject().getFinalidadeLote().getDescricao());
		}
		
	}
	
	@FXML
	private void handleSelecionarFinalidade(){
		
		finalidadeLoteReducedController.setObject(new FinalidadeLote());
		finalidadeLoteReducedController.showForm();
		
		if ( finalidadeLoteReducedController.getObject() != null && finalidadeLoteReducedController.getObject().getId() > 0 ){
			getObject().setFinalidadeLote(finalidadeLoteReducedController.getObject());
		}
		
		if ( getObject().getFinalidadeLote() != null ){
			inputFinalidade.textProperty().set(getObject().getFinalidadeLote().toString());	
		}else{
			inputFinalidade.textProperty().set("");
		}
		
	}
	
	@Override
	public String getFormName() {
		return "view/lote/LoteForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Lote";
	}
	
	@Override
	@Resource(name = "loteService")
	protected void setService(IService<Integer, Lote> service) {
		super.setService(service);
	}

	
}
