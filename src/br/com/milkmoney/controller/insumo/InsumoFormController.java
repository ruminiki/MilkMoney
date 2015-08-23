package br.com.milkmoney.controller.insumo;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.tipoInsumo.TipoInsumoReducedOverviewController;
import br.com.milkmoney.controller.unidadeMedida.UnidadeMedidaReducedOverviewController;
import br.com.milkmoney.model.Insumo;
import br.com.milkmoney.model.TipoInsumo;
import br.com.milkmoney.model.UnidadeMedida;
import br.com.milkmoney.service.IService;

@Controller
public class InsumoFormController extends AbstractFormController<Integer, Insumo> {

	@FXML private UCTextField inputDescricao, inputTipoInsumo, inputUnidadeMedida;
	@FXML private TextArea inputObservacao;

	@Autowired private TipoInsumoReducedOverviewController tipoInsumoReducedOverviewController;
	@Autowired private UnidadeMedidaReducedOverviewController unidadeMedidaReducedOverviewController;
	
	@FXML
	public void initialize() {
		
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		
		if ( getObject().getTipoInsumo() != null ){
			inputTipoInsumo.setText(getObject().getTipoInsumo().toString());
		}
		
		if ( getObject().getUnidadeMedida() != null ){
			inputUnidadeMedida.setText(getObject().getUnidadeMedida().toString());
		}
		
	}
	
	@FXML
	protected void handleSelecionarTipoInsumo() {
		
		tipoInsumoReducedOverviewController.setObject(new TipoInsumo());
		tipoInsumoReducedOverviewController.showForm();
		if ( tipoInsumoReducedOverviewController.getObject() != null && tipoInsumoReducedOverviewController.getObject().getId() > 0 ){
			getObject().setTipoInsumo(tipoInsumoReducedOverviewController.getObject());
		}
		
		if ( getObject().getTipoInsumo() != null ) {
			inputTipoInsumo.setText(getObject().getTipoInsumo().toString());
		}else{
			inputTipoInsumo.setText(null);
		}
		
	}
	
	@FXML
	protected void handleSelecionarUnidadeMedida() {
		
		unidadeMedidaReducedOverviewController.setObject(new UnidadeMedida());
		unidadeMedidaReducedOverviewController.showForm();
		if ( unidadeMedidaReducedOverviewController.getObject() != null && unidadeMedidaReducedOverviewController.getObject().getId() > 0 ){
			getObject().setUnidadeMedida(unidadeMedidaReducedOverviewController.getObject());
		}
		
		if ( getObject().getUnidadeMedida() != null ) {
			inputUnidadeMedida.setText(getObject().getUnidadeMedida().toString());
		}else{
			inputUnidadeMedida.setText(null);
		}
		
	}
	
	@Override
	public String getFormName() {
		return "view/insumo/InsumoForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Insumo";
	}
	
	@Override
	@Resource(name = "insumoService")
	protected void setService(IService<Integer, Insumo> service) {
		super.setService(service);
	}
	
}
