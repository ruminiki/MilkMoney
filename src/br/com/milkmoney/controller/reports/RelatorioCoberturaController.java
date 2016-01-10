package br.com.milkmoney.controller.reports;

import java.time.LocalDate;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.animal.AnimalReducedOverviewController;
import br.com.milkmoney.controller.touro.TouroReducedOverviewController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Lote;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.model.TipoCobertura;
import br.com.milkmoney.model.Touro;
import br.com.milkmoney.service.LoteService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class RelatorioCoberturaController extends AbstractReport{

	@FXML private DatePicker inputDataDe, inputDataAte;
	@FXML private UCTextField inputTouroInseminacaoArtificial, inputTouroMontaNatural;
	@FXML private TextField inputIdadeDe, inputIdadeAte;
	@FXML private ChoiceBox<String> inputTipoCobertura, inputSituacaoCobertura;
	@FXML private ChoiceBox<Lote> inputLote;
	
	@Autowired private TouroReducedOverviewController touroReducedOverviewController;
	@Autowired private AnimalReducedOverviewController animalReducedOverviewController;
	@Autowired private LoteService loteService;
	
	private Animal touroMontaNatural;
	private Touro touroInseminacaoArtificial;
	
	@FXML
	public void initialize(){
		
		touroMontaNatural = null;
		touroInseminacaoArtificial = null;
		
		inputDataDe.setValue(LocalDate.now().minusDays(365));
		inputDataAte.setValue(LocalDate.now());
		
		inputSituacaoCobertura.setItems(SituacaoCobertura.getItems());
		inputTipoCobertura.setItems(TipoCobertura.getItems());
		
		inputLote.setItems(loteService.findAllAsObservableList());
		
		MaskFieldUtil.numeroInteiro(inputIdadeDe);
		MaskFieldUtil.numeroInteiro(inputIdadeAte);
		
		super.initialize();
	}
	
	@FXML
	private void handleSelecionarTouroInseminacaoArtificial(){
	    	
		touroReducedOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, false);
		touroReducedOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, false);
		touroReducedOverviewController.getFormConfig().put(AbstractOverviewController.REMOVE_DISABLED, false);
		touroReducedOverviewController.showForm();
		
		touroInseminacaoArtificial = touroReducedOverviewController.getObject();
		
		if ( touroInseminacaoArtificial != null ){
			inputTouroInseminacaoArtificial.setText(touroInseminacaoArtificial.toString());	
		}else{
			inputTouroInseminacaoArtificial.setText("");
		}
		
	}
	
	@FXML
	private void handleSelecionarTouroMontaNatural(){
			
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, false);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, false);
		animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.REMOVE_DISABLED, false);
		animalReducedOverviewController.showForm();
		
		touroMontaNatural = animalReducedOverviewController.getObject();
		
		if ( touroMontaNatural != null ){
			inputTouroMontaNatural.setText(touroMontaNatural.toString());	
		}else{
			inputTouroMontaNatural.setText("");
		}
		
	}
	
	@FXML
	private void handleExecutar(){
		
		Object[] params = new Object[]{
				inputDataDe.getValue() == null ? new Date() : DateUtil.asDate(inputDataDe.getValue()),
				inputDataAte.getValue() == null ? new Date() : DateUtil.asDate(inputDataAte.getValue()),
				inputSituacaoCobertura.getValue() == null ? "" : inputSituacaoCobertura.getValue(),
				inputLote.getValue() == null ? 0 : inputLote.getValue().getId(),
				(inputIdadeDe.getText() == null || inputIdadeDe.getText().isEmpty()) ? 0 : inputIdadeDe.getText(),
				(inputIdadeAte.getText() == null || inputIdadeAte.getText().isEmpty()) ? 0 : inputIdadeAte.getText(),
				inputTipoCobertura.getValue() == null ? "" : inputTipoCobertura.getValue(),
				touroInseminacaoArtificial == null ? 0 : touroInseminacaoArtificial.getId(),
				touroMontaNatural == null ? 0 : touroMontaNatural.getId()
		};
		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_COBERTURA, params);
		}else{
			relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RELATORIO_COBERTURA, params);
		}
		
		super.handleClose();
		
		rootLayoutController.setMessage("O relatório está sendo executado...");
		
		
	}

}
