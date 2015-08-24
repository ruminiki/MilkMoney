package br.com.milkmoney.controller.root;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.propriedade.PropriedadeReducedOverviewController;
import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Propriedade;
import br.com.milkmoney.service.PropriedadeService;
import br.com.milkmoney.validation.Validator;


@Controller
public class RootLayoutController {
	
	@FXML private Label lblHeader, lblMessage;
	@FXML private Hyperlink hlPropriedade;
	
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
		
		lblHeader.setText("Milk Money® - Gestão de Rebanhos Leiteiros");
		
	}
	
	public void setMessage(String message){
		
		Platform.runLater(() -> {
            try {
            	Thread.sleep(5000);
                lblMessage.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
		
		if ( lblMessage != null )
			lblMessage.setText(message);
	}
	
	public void setTitle(String title){
		if ( lblHeader != null )
			lblHeader.setText(title);
	}
	
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
    protected void handleCadastroFinalidadeLote() {
    	openFormAsPopUp("view/finalidadeLote/FinalidadeLoteOverview.fxml", "Finalidade Lote");
    }
       
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

    @FXML
    protected void handleCadastroSemen() {
    	openFormAsPopUp("view/semen/SemenOverview.fxml", "Sêmen");
    }
    
    @FXML
    protected void handleCadastroTouro() {
    	openFormAsPopUp("view/touro/TouroOverview.fxml", "Touro");
    }
    
    @FXML
    protected void handleCadastroCobertura() {
    	openForm("view/cobertura/CoberturaOverview.fxml");
    	setTitle("Rebanho > Reprodução");
    }
    
    @FXML
    protected void handleCadastroFuncionario() {
    	openFormAsPopUp("view/funcionario/FuncionarioOverview.fxml", "Funcionário");
    }
    
    @FXML
    protected void handleCadastroServico() {
    	openFormAsPopUp("view/servico/ServicoOverview.fxml", "Serviço");
    }
    
    @FXML
    protected void handleCadastroFornecedor() {
    	openFormAsPopUp("view/fornecedor/FornecedorOverview.fxml", "Fornecedor");
    }
    
    @FXML
    protected void handlePainel() {
    	openForm("view/painel/PainelOverview.fxml");
    	setTitle("Miscelânea > Indicadores");
    }
    
    @FXML
    protected void handleCadastroParametro() {
    	openFormAsPopUp("view/parametro/ParametroOverview.fxml", "Parâmetros");
    }
    
    @FXML
    protected void  handleCadastroPropriedade() {
    	openFormAsPopUp("view/propriedade/PropriedadeOverview.fxml", "Propriedade");
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
    
    @FXML
    protected void handleProjecao() {
    	openForm("view/projecao/ProjecaoOverview.fxml");
    	setTitle("Miscelânea > Projeção");
    }
    
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
    protected void handleCadastroCategoriaDespesa() {
    	openFormAsPopUp("view/categoriaDespesa/CategoriaDespesaOverview.fxml", "Categoria de Despesa");
    }
    //-----reports------
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