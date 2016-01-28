package br.com.milkmoney.controller.root;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.animal.AcessoRapidoAnimalController;
import br.com.milkmoney.controller.propriedade.PropriedadeReducedOverviewController;
import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Propriedade;
import br.com.milkmoney.service.ApplicationUpdateService;
import br.com.milkmoney.service.PropriedadeService;
import br.com.milkmoney.validation.Validator;


@Controller
public class RootLayoutController {
	
	@FXML private Label lblHeader, lblMessage, lblSistema;
	@FXML private Hyperlink hlPropriedade;
	@FXML private UCTextField inputNumeroAnimal;
	
	private StringProperty message = new SimpleStringProperty();
	
	@FXML
	protected void initialize(){
		
		PropriedadeReducedOverviewController propriedadeReducedOverviewController = 
				(PropriedadeReducedOverviewController)MainApp.getBean(PropriedadeReducedOverviewController.class);
		
		try{
			PropriedadeService propriedadeService = (PropriedadeService)MainApp.getBean(PropriedadeService.class);
			Propriedade propriedade = propriedadeService.findAll().get(0);
			hlPropriedade.setText("PROPRIEDADE "+propriedade.getDescricao());
		}catch(Exception e){
			throw new ValidationException(Validator.VALIDACAO_FORMULARIO, "Houve um erro ao recuperar a propriedade. Por favor, verifique se foi executa a inicialização do banco de dados.");
		}
		
		hlPropriedade.setOnAction(action -> {
			propriedadeReducedOverviewController.showForm();
			if ( propriedadeReducedOverviewController.getObject() != null ){
				hlPropriedade.setText("PROPRIEDADE "+propriedadeReducedOverviewController.getObject().getDescricao());
			}
		});
		
		String versao = new ApplicationUpdateService().getVersaoSistema();
		
		lblSistema.setText("Milk Money® - Gestão de Rebanhos Leiteiros - v" + versao);
		lblMessage.textProperty().bind(message);
		
		inputNumeroAnimal.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode().equals(KeyCode.ENTER)) {
					handleAcessoRapidoAnimal(inputNumeroAnimal.getText());
					inputNumeroAnimal.setText("");
					event.consume();
				}

			}

		});
		
	}
	
	public void setMessage(String msg){
		
		Task <Void> task = new Task<Void>() {
		      @Override public Void call() throws InterruptedException {
		        updateMessage(msg);
		        // some actions
		        Thread.sleep(5000);
		        updateMessage(""); 
		        return null;
		      }
		    };

		    message.bind(task.messageProperty());

		    // java 8 construct, replace with java 7 code if using java 7.
		    task.setOnSucceeded(e -> {
		    	message.unbind();
		      // this message will be seen.
		    	message.set("");
		    });

		    Thread thread = new Thread(task);
		    thread.setDaemon(true);
		    thread.start();
		
	}
	
	public void setTitle(String title){
		if ( lblHeader != null )
			lblHeader.setText(title);
	}
	
	//REBANHO
    @FXML
    protected void handleCadastroAnimal() {
    	openForm("view/animal/AnimalOverview.fxml");
    	setTitle("Rebanho > Animais");
    }

    @FXML
    protected void handleCadastroVendaAnimal() {
    	openForm("view/vendaAnimal/VendaAnimalOverview.fxml");
    	setTitle("Rebanho > Venda Animal");
    }
    
    @FXML
    protected void handleCadastroLote() {
    	openFormAsPopUp("view/lote/LoteOverview.fxml", "Lote Animal");
    }
    
    @FXML
    protected void handleCadastroProcedimento() {
    	openForm("view/procedimento/ProcedimentoOverview.fxml");
    	setTitle("Rebanho > Procedimento");
    }
    
    //PRODUÇÃO
    @FXML
    protected void handleCadastroPrecoLeite() {
    	openFormAsPopUp("view/precoLeite/PrecoLeiteOverview.fxml", "Preço Leite");
    }
    
    @FXML
    protected void handleCadastroProducaoLeite() {
    	openForm("view/producaoLeite/ProducaoLeiteOverview.fxml");
    	setTitle("Produção > Produção de Leite");
    }
       
    @FXML
    protected void handleCadastroEntregaLeite() {
    	openForm("view/entregaLeite/EntregaLeiteOverview.fxml");
    	setTitle("Produção > Entrega de Leite");
    }

    //FINANCEIRO
    
    @FXML
    protected void handleCadastroInsumo() {
    	openFormAsPopUp("view/insumo/InsumoOverview.fxml", "Insumo");
    }
    
    @FXML
    protected void handleCadastroCompra() {
    	openForm("view/compra/CompraOverview.fxml");
    	setTitle("Financeiro > Compra Insumos");
    }
    
    @FXML
    protected void handleCadastroLancamentoFinanceiro() {
    	openForm("view/lancamentoFinanceiro/LancamentoFinanceiroOverview.fxml");
    	setTitle("Financeiro > Lançamentos");
    }
    
    @FXML
    protected void handleCadastroServico() {
    	openForm("view/servico/ServicoOverview.fxml");
    	setTitle("Financeiro > Serviços");    	
    }
        
    
    //RELATÓRIOS
    @FXML
    protected void handleReportFichaAnimal(){
    	openFormAsPopUp("view/reports/RelatorioFichaAnimal.fxml", "Relatório Ficha Animal");
    }
    
    @FXML
    protected void handleFormularioCampoRegistroParto(){
    	openFormAsPopUp("view/reports/FormularioRegistroParto.fxml", "Formulário Registro Parto");
    }
    
    @FXML
    protected void handleFormularioCampoRegistroCobertura(){
    	openFormAsPopUp("view/reports/FormularioRegistroCobertura.fxml", "Formulário Registro Cobertura");
    }
    
    @FXML
    protected void handleFormularioCampoRegistroProducao(){
    	openFormAsPopUp("view/reports/FormularioRegistroProducao.fxml", "Formulário Registro Produção");
    }
    
    @FXML
    protected void handleReportPartosPrevistos(){
    	openFormAsPopUp("view/reports/RelatorioPartosPrevistos.fxml", "Relatório de Partos Previstos");
    }
    
    @FXML
    protected void handleReportCobertura(){
    	openFormAsPopUp("view/reports/RelatorioCobertura.fxml", "Relatório de Coberturas");
    }
    
    @FXML
    protected void handleReportPartosRealizados(){
    	openFormAsPopUp("view/reports/RelatorioPartos.fxml", "Relatório de Partos");
    }
    
    @FXML
    protected void handleReportResumoFinanceiro(){
    	openFormAsPopUp("view/reports/ResumoFinanceiro.fxml", "Relatório Resumo Financeiro");
    }
    
    @FXML
    protected void handleReportProcedimentosSanitarios(){
    	openFormAsPopUp("view/reports/RelatorioProcedimentos.fxml", "Relatório Procedimentos Sanitários");
    }
    
    @FXML
    protected void handleReportProducao(){
    	openFormAsPopUp("view/reports/RelatorioProducao.fxml", "Relatório Produção");
    }
    
    @FXML
    protected void handleReportLancamentosFinanceiros(){
    	openFormAsPopUp("view/reports/RelatorioFinanceiro.fxml", "Relatório Lançamentos Financeiros");
    }
    
    @FXML
    protected void handleReportRankingAnimais(){
    	openFormAsPopUp("view/reports/RelatorioRankingAnimais.fxml", "Relatório Ranking Animais");
    }
    
    @FXML
    protected void handleReportNumerosRebanho(){
    	openFormAsPopUp("view/reports/RelatorioNumerosRebanho.fxml", "Relatório Números do Rebanho");
    }
    
    @FXML
    protected void handleReportResumoAtividade(){
    	openFormAsPopUp("view/reports/RelatorioResumoAtividade.fxml", "Relatório Resumo Atividade");
    }
    
    @FXML
    protected void handleReportAbortos(){
    	openFormAsPopUp("view/reports/RelatorioAbortos.fxml", "Relatório de Abortos");
    }
    
    @FXML
    protected void handleReportEncerramentoLactacao(){
    	openFormAsPopUp("view/reports/RelatorioEncerramentoLactacao.fxml", "Relatório Encerramento Lactações");
    }
    
    @FXML
    protected void handleReportControleLeiteiro(){
    	openFormAsPopUp("view/reports/RelatorioControleLeiteiro.fxml", "Relatório Controle Leiteiro");
    }
    
    @FXML
    protected void handleReportServicos(){
    	openFormAsPopUp("view/reports/RelatorioServicos.fxml", "Relatório de Serviços");
    }
    
    //MISCELÂNEA - GRÁFICOS
    @FXML
    protected void handlePainel() {
    	openForm("view/painel/PainelOverview.fxml");
    	setTitle("Miscelânea > Gráficos");
    }
    
    @FXML
    protected void handleProjecao() {
    	openForm("view/projecao/ProjecaoOverview.fxml");
    	setTitle("Miscelânea > Projeção");
    }
    
    //MISCELÂNEA - CADASTROS
    
    @FXML
    protected void handleCadastroRaca() {
    	openFormAsPopUp("view/raca/RacaOverview.fxml", "Raça");
    }
    
    @FXML
    protected void handleCadastroTouro() {
    	openFormAsPopUp("view/touro/TouroOverview.fxml", "Touro");
    }
    
    @FXML
    protected void handleCadastroFinalidadeLote() {
    	openFormAsPopUp("view/finalidadeLote/FinalidadeLoteOverview.fxml", "Finalidade Lote");
    }

    @FXML
    protected void handleCadastroComplicacaoParto() {
    	openFormAsPopUp("view/complicacaoParto/ComplicacaoPartoOverview.fxml", "Complicação Parto");
    }
    
    @FXML
    protected void handleCadastroMotivoEncerramentoLactacao() {
    	openFormAsPopUp("view/motivoEncerramentoLactacao/MotivoEncerramentoLactacaoOverview.fxml", "Motivo Encerramento Lactação");
    }
    
    @FXML
    protected void handleCadastroCausaMorte() {
    	openFormAsPopUp("view/causaMorteAnimal/CausaMorteAnimalOverview.fxml", "Causa Morte");
    }
    
    @FXML
    protected void handleCadastroTipoProcedimento() {
    	openFormAsPopUp("view/tipoProcedimento/TipoProcedimentoOverview.fxml", "Tipo Procedimento");
    }
    
    @FXML
    protected void handleCadastroComprador() {
    	openFormAsPopUp("view/comprador/CompradorOverview.fxml", "Comprador");
    }
    
    @FXML
    protected void handleCadastroMotivoVendaAnimal() {
    	openFormAsPopUp("view/motivoVendaAnimal/MotivoVendaAnimalOverview.fxml", "Motivo Venda Animal");
    }
    
    @FXML
    protected void handleCadastroCategoriaLancamentoFinanceiro() {
    	openFormAsPopUp("view/categoriaLancamentoFinanceiro/CategoriaLancamentoFinanceiroOverview.fxml", "Categoria de Lançamento Financeiro");
    }   

    @FXML
    protected void handleCadastroCentroCusto() {
    	openFormAsPopUp("view/centroCusto/CentroCustoOverview.fxml", "Centro Custo");
    }

    @FXML
    protected void handleCadastroTipoInsumo() {
    	openFormAsPopUp("view/tipoInsumo/TipoInsumoOverview.fxml", "Centro Custo");
    }
    
    @FXML
    protected void handleCadastroUnidadeMedida() {
    	openFormAsPopUp("view/unidadeMedida/UnidadeMedidaOverview.fxml", "Unidade de Medida");
    }
    
    @FXML
    protected void handleCadastroFornecedor() {
    	openFormAsPopUp("view/fornecedor/FornecedorOverview.fxml", "Fornecedor");
    }

    @FXML
    protected void handleCadastroPrestadorServico() {
    	openFormAsPopUp("view/prestadorServico/PrestadorServicoOverview.fxml", "Prestador de Serviço");
    }
    
    @FXML
    protected void handleCadastroFuncionario() {
    	openFormAsPopUp("view/funcionario/FuncionarioOverview.fxml", "Funcionário");
    }
    
    @FXML
    protected void handleCadastroMotivoOcorrenciaFuncionario() {
    	openFormAsPopUp("view/motivoOcorrenciaFuncionario/MotivoOcorrenciaFuncionarioOverview.fxml", "Motivo Ocorrência Funcionário");
    }
    
    @FXML
    protected void  handleCadastroPropriedade() {
    	openFormAsPopUp("view/propriedade/PropriedadeOverview.fxml", "Propriedade");
    }

    @FXML
    protected void handleCadastroParametro() {
    	openFormAsPopUp("view/parametro/ParametroOverview.fxml", "Parâmetros");
    }
    
    //SISTEMA
    
    @FXML
    protected void handleAcessoRapidoAnimal(String numero){
    	AcessoRapidoAnimalController controller = (AcessoRapidoAnimalController) MainApp.getBean(AcessoRapidoAnimalController.class);
    	controller.setNumeroDigitado(numero);
    	openFormAsPopUp("view/animal/AcessoRapidoAnimal.fxml", "Dados do Animal");
    }
    
    @FXML
    protected void handleBackupBancoDados() {
    	openFormAsPopUp("view/databaseBackup/DatabaseBackupForm.fxml", "Cópia do Banco de Dados");
    }
  
    
    
 
    
	public void openFormAsPopUp(String formPath, String title){
    	
    	AnchorPane form = (AnchorPane) MainApp.load(formPath);
    	
		Stage dialogStage = new Stage();
		dialogStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream(MainApp.APPLICATION_ICON)));
		
		dialogStage.setTitle(title);
		
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);

		Scene scene = new Scene(form);
		dialogStage.setScene(scene);

		dialogStage.setResizable(false);
		dialogStage.showAndWait();
		
    }
    
    public void openForm(String formPath){
    	AnchorPane form = (AnchorPane) MainApp.load(formPath);
    	MainApp.rootLayout.setCenter(form);
    }
    
    /**
     * Abre uma janela Sobre.
     */
    @FXML
    protected void handleAbout() {
    	
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Milk Money 1.0");
		alert.setHeaderText("Sobre");
		alert.setContentText("Autor: Ruminiki Schmoeller");
		alert.showAndWait();
		
    }

    @FXML
    protected void handleExit() {
        System.exit(0);
    }
}