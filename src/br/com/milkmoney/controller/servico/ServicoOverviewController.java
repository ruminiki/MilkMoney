package br.com.milkmoney.controller.servico;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.components.WaitReport;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.lancamentoFinanceiro.LancamentoFinanceiroFormController;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.controller.servico.renderer.TableCellSituacaoLancamentoFinanceiroFactory;
import br.com.milkmoney.model.LancamentoFinanceiro;
import br.com.milkmoney.model.PrestadorServico;
import br.com.milkmoney.model.Servico;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.service.ServicoService;

@Controller
public class ServicoOverviewController extends AbstractOverviewController<Integer, Servico> {

	@FXML private TableColumn<Servico, String> integradoFinanceiroColumn;
	@FXML private TableColumn<Servico, String> descricaoColumn;
	@FXML private TableColumn<Servico, String> dataColumn;
	@FXML private TableColumn<Servico, String> dataVencimentoColumn;
	@FXML private TableColumn<PrestadorServico, String> prestadorServicoColumn;
	@FXML private TableColumn<Servico, String> valorColumn;
	@FXML private TableColumn<Servico, String> historicoColumn;

	@FXML private Button btnRemoverFinanceiro, btnGerarFinanceiro;
	
	@Autowired private RelatorioService relatorioService;
	@Autowired private LancamentoFinanceiroFormController lancamentoFinanceiroFormController;
	
	@FXML
	public void initialize() {
		
		integradoFinanceiroColumn.setCellFactory(new TableCellSituacaoLancamentoFinanceiroFactory<Servico,String>("descricao"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<Servico,String>("descricao"));
		dataColumn.setCellFactory(new TableCellDateFactory<Servico,String>("data"));
		dataVencimentoColumn.setCellFactory(new TableCellDateFactory<Servico,String>("dataVencimento"));
		valorColumn.setCellValueFactory(new PropertyValueFactory<Servico,String>("valor"));
		prestadorServicoColumn.setCellValueFactory(new PropertyValueFactory<PrestadorServico,String>("prestadorServico"));
		historicoColumn.setCellValueFactory(new PropertyValueFactory<Servico,String>("historico"));
		
		super.initialize((ServicoFormController) MainApp.getBean(ServicoFormController.class));
		
	}
	
	@Override
	protected void selectRowTableHandler(Servico servico) {
		super.selectRowTableHandler(servico);
		if ( servico != null ){
			btnRemoverFinanceiro.setDisable(servico.getLancamentoFinanceiro() == null);
			btnGerarFinanceiro.setDisable(servico.getLancamentoFinanceiro() != null);
		}
	}
	
	@FXML
	private void emitirRecibo(){
		
		Servico servico = table.getSelectionModel().getSelectedItem();
		
		if ( servico != null ){
			
			//emitir recibo de pagamento
			Object[] params = new Object[]{
					servico.getDescricao(),
					servico.getValor(),
					servico.getPrestadorServico().toString()
				};
				
			WaitReport.wait(relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
						RelatorioService.RECIBO_SERVICO, params), MainApp.primaryStage);
			
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		
	}
	
	@FXML
	private void gerarLancamentoFinanceiro(){
		
		LancamentoFinanceiro lancamentoFinanceiro = new LancamentoFinanceiro();
		lancamentoFinanceiro.setDataEmissao(getObject().getData());
		lancamentoFinanceiro.setDataVencimento(getObject().getDataVencimento());
		lancamentoFinanceiro.setDescricao(getObject().getDescricao());
		lancamentoFinanceiro.setValor(getObject().getValor());
		
		lancamentoFinanceiroFormController.setObject(lancamentoFinanceiro);
		lancamentoFinanceiroFormController.showForm();
		
		if( lancamentoFinanceiroFormController.getObject() != null && 
				lancamentoFinanceiroFormController.getObject().getId() > 0 ){
			getObject().setLancamentoFinanceiro(lancamentoFinanceiroFormController.getObject());
			service.save(getObject());
			refreshTableOverview();
		}
		
	}
	
	@FXML
	private void removerLancamentoFinanceiro(){
		
		Optional<ButtonType> result = CustomAlert.confirmar("Remover Lançamento Financeiro", "Confirma a exclusão do lançamento financeiro do respectivo serviço?");
		if (result.get() == ButtonType.OK) {
			
			try {
				Servico servico = table.getSelectionModel().getSelectedItem();
				((ServicoService)service).removerLancamentoFinanceiroIntegrado(servico);
				refreshTableOverview();
			} catch (Exception e) {
				CustomAlert.mensagemAlerta("", e.getMessage());
				return;
			}
			
		}

	}
	
	@Override
	public String getFormTitle() {
		return "Serviço";
	}
	
	@Override
	public String getFormName() {
		return "view/servico/ServicoOverview.fxml";
	}
	
	@Override
	@Resource(name = "servicoService")
	protected void setService(IService<Integer, Servico> service) {
		super.setService(service);
	}

}
