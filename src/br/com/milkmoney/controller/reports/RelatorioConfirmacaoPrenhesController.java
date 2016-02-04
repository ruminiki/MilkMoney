package br.com.milkmoney.controller.reports;

import java.time.LocalDate;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.model.Lote;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.service.LoteService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class RelatorioConfirmacaoPrenhesController extends AbstractReport{

	@FXML private DatePicker inputDataDe, inputDataAte;
	@FXML private UCTextField inputTouroInseminacaoArtificial, inputTouroMontaNatural;
	@FXML private ChoiceBox<String> inputSituacaoCobertura;
	@FXML private ChoiceBox<Lote> inputLote;
	@Autowired private LoteService loteService;
	
	@FXML
	public void initialize(){
		
		inputDataDe.setValue(LocalDate.now().minusDays(365));
		inputDataAte.setValue(LocalDate.now());
		inputSituacaoCobertura.setItems(SituacaoCobertura.getItems());
		inputLote.setItems(loteService.findAllAsObservableList());
		
		super.initialize();
	}
	
	@Override
	protected void handleExecutar(){
		
		Object[] params = new Object[]{
				inputDataDe.getValue() == null ? new Date() : DateUtil.asDate(inputDataDe.getValue()),
				inputDataAte.getValue() == null ? new Date() : DateUtil.asDate(inputDataAte.getValue()),
				inputSituacaoCobertura.getValue() == null ? "" : inputSituacaoCobertura.getValue(),
				inputLote.getValue() == null ? 0 : inputLote.getValue().getId(),
		};
		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_CONFIMACAO_PRENHES, params);
		}else{
			relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RELATORIO_CONFIMACAO_PRENHES, params);
		}
		
		super.handleClose();
		
		rootLayoutController.setMessage("O relatório está sendo executado...");
		
		
	}

}
