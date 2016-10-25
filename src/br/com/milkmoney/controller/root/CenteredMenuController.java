package br.com.milkmoney.controller.root;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.controller.atividadesSemana.AtividadesSemanaOverviewController;
import br.com.milkmoney.controller.root.renderer.VBoxOption;


@Controller
public class CenteredMenuController {

	@FXML private VBox vbProducao, vbGraficosColumn1, vbGraficosColumn2;
	@FXML private HBox hbRebanho, hbFinanceiro, hbRelatoriosLine1, hbRelatoriosLine2, hbRelatoriosLine3;
	
	@Autowired private RootLayoutController rootLayoutController;
	@Autowired private AtividadesSemanaOverviewController atividadeSemanaOverviewController;
	
	@FXML
	private void initialize(){
		
		//===== rebanho ===
		VBoxOption vBoxAnimais = new VBoxOption("img/menu/animais_48.png", "Animais");
		//vBoxAnimais.setWidth(150);
		vBoxAnimais.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroAnimal();
        	}
        });
        hbRebanho.getChildren().add(vBoxAnimais);
        
		VBoxOption vBoxCoberturas = new VBoxOption("img/menu/coberturas_48.png", "Coberturas Insemina��es");
		//vBoxCoberturas.setWidth(150);
		vBoxCoberturas.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroCoberturas();
        	}
        });
        hbRebanho.getChildren().add(vBoxCoberturas);
        
        VBoxOption vBoxLote = new VBoxOption("img/menu/lotes_48.png", "Lotes");
        //vBoxLote.setWidth(150);
        vBoxLote.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroLote();
        	}
        });
        hbRebanho.getChildren().add(vBoxLote);
        
        VBoxOption vBoxProcedimentos = new VBoxOption("img/menu/procedimentos_sanitarios_48.png", "Procedimentos Sanit�rios");
        //vBoxProcedimentos.setWidth(150);
        vBoxProcedimentos.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroProcedimento();
        	}
        });
        hbRebanho.getChildren().add(vBoxProcedimentos);
        
        VBoxOption vBoxVenda = new VBoxOption("img/menu/venda_animais_48.png", "Vendas de Animais");
        //vBoxVenda.setWidth(150);
        vBoxVenda.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroVendaAnimal();
        	}
        });
        hbRebanho.getChildren().add(vBoxVenda);
        
      //===== produ��o ===
        VBoxOption vBoxPrecoLeite = new VBoxOption("img/menu/preco_leite_48.png", "Pre�o do Leite");
        vBoxPrecoLeite.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroPrecoLeite();
        	}
        });
        vbProducao.getChildren().add(vBoxPrecoLeite);
        
        VBoxOption vBoxProducao = new VBoxOption("img/menu/producao_48.png", "Produ��o");
        vBoxProducao.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroProducaoLeite();
        	}
        });
        vbProducao.getChildren().add(vBoxProducao);
        
        VBoxOption vBoxEntregaLeite = new VBoxOption("img/menu/entrega_leite_48.png", "Entrega de Leite");
        vBoxEntregaLeite.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroEntregaLeite();
        	}
        });
        vbProducao.getChildren().add(vBoxEntregaLeite);
        
       //===== financeiro ===
        
        VBoxOption vBoxFinanceiro = new VBoxOption("img/menu/lancamentos_financeiros_48.png", "Lan�amentos Financeiros");
        vBoxFinanceiro.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroLancamentoFinanceiro();
        	}
        });
        hbFinanceiro.getChildren().add(vBoxFinanceiro);
        
        VBoxOption vBoxServicos = new VBoxOption("img/menu/servicos_48.png", "Servi�os");
        vBoxServicos.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroServico();
        	}
        });
        hbFinanceiro.getChildren().add(vBoxServicos);
        
        VBoxOption vBoxCentroCusto = new VBoxOption("img/menu/centro_custo_48.png", "Centro de Custo");
        vBoxCentroCusto.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroCentroCusto();
        	}
        });
        hbFinanceiro.getChildren().add(vBoxCentroCusto);
        
        VBoxOption vBoxCategoriaLancamento = new VBoxOption("img/menu/categoria_lancamento_financeiro_48.png", "Categoria Lan�amento");
        vBoxCategoriaLancamento.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroCategoriaLancamentoFinanceiro();
        	}
        });
        hbFinanceiro.getChildren().add(vBoxCategoriaLancamento);
        
        //====== gr�ficos ======
        
        VBoxOption vBoxIndicadores = new VBoxOption("img/menu/indicadores_48.png", "Indicadores");
        vBoxIndicadores.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroIndicadores();
        	}
        });
        vbGraficosColumn1.getChildren().add(vBoxIndicadores);
		
		VBoxOption vBoxEficienciaIndividual = new VBoxOption("img/menu/eficiencia_individual_48.png", "Efici�ncia Reprodutiva (c�lculo individual)");
		vBoxEficienciaIndividual.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleGraficoEficienciaReprodutivaIndividual();
        	}
        });
		vbGraficosColumn2.getChildren().add(vBoxEficienciaIndividual);
		
		VBoxOption vbEficienciaGeral= new VBoxOption("img/menu/eficiencia_geral_48.png", "Efici�ncia Reprodutiva (vis�o geral rebanho)");
		vbEficienciaGeral.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleGraficoEficienciaReprodutivaMap();
        	}
        });
		vbGraficosColumn1.getChildren().add(vbEficienciaGeral);
		
		VBoxOption vbGraficoFinanceiro = new VBoxOption("img/menu/resumo_financeiro_48.png", "Financeiro");
		vbGraficoFinanceiro.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleGraficoFinanceiro();
        	}
        });
		vbGraficosColumn2.getChildren().add(vbGraficoFinanceiro);
		
		VBoxOption vbGraficoCausaMorte= new VBoxOption("img/menu/causa_morte_48.png", "Causa Morte");
		vbGraficoCausaMorte.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleGraficoCausaMorteAnimais();
        	}
        });
		vbGraficosColumn1.getChildren().add(vbGraficoCausaMorte);
		
		VBoxOption vbGraficoProjecao = new VBoxOption("img/menu/indicadores_48.png", "Proje��es");
		vbGraficoProjecao.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleProjecao();
        	}
        });
		vbGraficosColumn2.getChildren().add(vbGraficoProjecao);
		
        //====== relat�rios linha 1 ======
        
        VBoxOption vBoxRelatorioFichaAnimais = new VBoxOption("img/menu/ficha_animais_48.png", "Ficha Animais");
        vBoxRelatorioFichaAnimais.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportFichaAnimal();
        	}
        });
		hbRelatoriosLine1.getChildren().add(vBoxRelatorioFichaAnimais);
		
		VBoxOption vBoxRelatorioRankingAnimais = new VBoxOption("img/menu/ranking_animais_48.png", "Ranking Animais");
		vBoxRelatorioRankingAnimais.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportRankingAnimais();
        	}
        });
		hbRelatoriosLine1.getChildren().add(vBoxRelatorioRankingAnimais);
		
		VBoxOption vBoxRelatorioIndicadores = new VBoxOption("img/menu/indicadores_48.png", "Indicadores");
		vBoxRelatorioIndicadores.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportIndicadores();
        	}
        });
		hbRelatoriosLine1.getChildren().add(vBoxRelatorioIndicadores);
		
		VBoxOption vBoxRelatorioResumoAtividade = new VBoxOption("img/menu/resumo_atividade_48.png", "Resumo da Atividade");
		vBoxRelatorioResumoAtividade.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportResumoAtividade();
        	}
        });
		hbRelatoriosLine1.getChildren().add(vBoxRelatorioResumoAtividade);
		
		VBoxOption vBoxRelatorioProducao = new VBoxOption("img/menu/producao_48.png", "Produ��o");
		vBoxRelatorioProducao.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportProducao();
        	}
        });
		hbRelatoriosLine1.getChildren().add(vBoxRelatorioProducao);
		
		VBoxOption vBoxRelatorioCoberturas = new VBoxOption("img/menu/coberturas_48.png", "Coberturas");
		vBoxRelatorioCoberturas.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportCobertura();
        	}
        });
		hbRelatoriosLine1.getChildren().add(vBoxRelatorioCoberturas);
		
		VBoxOption vBoxRelatorioConfirmacaoPrenhez = new VBoxOption("img/menu/confirmacao_prenhez_48.png", "Confirma��es de Prenhez");
		vBoxRelatorioConfirmacaoPrenhez.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportConfirmacaoPrenhez();
        	}
        });
		hbRelatoriosLine1.getChildren().add(vBoxRelatorioConfirmacaoPrenhez);
		
		//====== relat�rios linha 2 ======
		
		VBoxOption vBoxRelatorioPartosPrevistos = new VBoxOption("img/menu/partos_previstos_48.png", "Partos Previstos");
		vBoxRelatorioPartosPrevistos.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportPartosPrevistos();
        	}
        });
		hbRelatoriosLine2.getChildren().add(vBoxRelatorioPartosPrevistos);
		
		VBoxOption vBoxRelatorioPartosRealizados = new VBoxOption("img/menu/partos_realizados_48.png", "Partos Realizados");
		vBoxRelatorioPartosRealizados.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportPartosRealizados();
        	}
        });
		hbRelatoriosLine2.getChildren().add(vBoxRelatorioPartosRealizados);
		
		VBoxOption vBoxRelatorioAbortos = new VBoxOption("img/menu/abortos_48.png", "Abortos");
		vBoxRelatorioAbortos.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportAbortos();
        	}
        });
		hbRelatoriosLine2.getChildren().add(vBoxRelatorioAbortos);
		
		VBoxOption vBoxRelatorioEncerramentoLactacao = new VBoxOption("img/menu/encerramento_lactacao_48.png", "Encerramentos Lacta��o");
		vBoxRelatorioEncerramentoLactacao.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportEncerramentoLactacao();
        	}
        });
		hbRelatoriosLine2.getChildren().add(vBoxRelatorioEncerramentoLactacao);
		
		VBoxOption vBoxRelatorioProximosEncerramentosLactacao = new VBoxOption("img/menu/proximos_encerramentos_48.png", "Pr�ximos Encerramentos Lacta��o");
		vBoxRelatorioProximosEncerramentosLactacao.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportProximosEncerramentosLactacao();
        	}
        });
		hbRelatoriosLine2.getChildren().add(vBoxRelatorioProximosEncerramentosLactacao);
		
		VBoxOption vBoxRelatorioControleLeiteiro = new VBoxOption("img/menu/controle_leiteiro_48.png", "Controle Leiteiro");
		vBoxRelatorioControleLeiteiro.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportControleLeiteiro();
        	}
        });
		hbRelatoriosLine2.getChildren().add(vBoxRelatorioControleLeiteiro);
		
		VBoxOption vBoxRelatorioProcedimentosSanitarios = new VBoxOption("img/menu/procedimentos_sanitarios_48.png", "Procedimentos Sanit�rios");
		vBoxRelatorioProcedimentosSanitarios.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportProcedimentosSanitarios();
        	}
        });
		hbRelatoriosLine2.getChildren().add(vBoxRelatorioProcedimentosSanitarios);
		
		//====== relat�rios linha 3 ======
		
		VBoxOption vBoxRelatorioServicos = new VBoxOption("img/menu/servicos_48.png", "Servi�os");
		vBoxRelatorioServicos.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportServicos();
        	}
        });
		hbRelatoriosLine3.getChildren().add(vBoxRelatorioServicos);
		
		VBoxOption vBoxRelatorioLancamentosFinanceiros = new VBoxOption("img/menu/lancamentos_financeiros_48.png", "Lan�amentos Financeiros");
		vBoxRelatorioLancamentosFinanceiros.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportLancamentosFinanceiros();
        	}
        });
		hbRelatoriosLine3.getChildren().add(vBoxRelatorioLancamentosFinanceiros);
		
		VBoxOption vBoxRelatorioResumoFinanceiro = new VBoxOption("img/menu/resumo_financeiro_48.png", "Resumo Financeiro");
		vBoxRelatorioResumoFinanceiro.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleReportResumoFinanceiro();
        	}
        });
		hbRelatoriosLine3.getChildren().add(vBoxRelatorioResumoFinanceiro);
		
		VBoxOption vBoxFormularioCampoRegistroParto = new VBoxOption("img/menu/registro_partos_48.png", "Formul�rio Campo Registro Parto");
		vBoxFormularioCampoRegistroParto.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleFormularioCampoRegistroParto();
        	}
        });
		hbRelatoriosLine3.getChildren().add(vBoxFormularioCampoRegistroParto);
		
		VBoxOption vBoxFormularioCampoRegistroCobertura = new VBoxOption("img/menu/registro_coberturas_48.png", "Formul�rio Campo Registro Coberturas");
		vBoxFormularioCampoRegistroCobertura.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleFormularioCampoRegistroCobertura();
        	}
        });
		hbRelatoriosLine3.getChildren().add(vBoxFormularioCampoRegistroCobertura);
		
		VBoxOption vBoxFormularioCampoRegistroProducao = new VBoxOption("img/menu/registro_producao_48.png", "Formul�rio Campo Registro Produ��o");
		vBoxFormularioCampoRegistroProducao.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleFormularioCampoRegistroProducao();
        	}
        });
		hbRelatoriosLine3.getChildren().add(vBoxFormularioCampoRegistroProducao);
		
	}
	
	@FXML
	private void handleShowAtividadesSemana(){
		atividadeSemanaOverviewController.showForm();
	}
	

}