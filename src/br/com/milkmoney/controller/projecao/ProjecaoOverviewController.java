package br.com.milkmoney.controller.projecao;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.model.Projecao;
import br.com.milkmoney.service.ProjecaoService;

@Controller
public class ProjecaoOverviewController {

	@FXML private TableView<Projecao> table;
	@FXML private TableColumn<Projecao, String> periodoColumn, animaisLactacaoColumn, percentualVariacaoAnimaisLactacaoColumn;
	@FXML private TableColumn<Projecao, String> animaisSecosColumn, percentualVariacaoAnimaisSecosColumn, producaoDiariaColumn;
	@FXML private TableColumn<Projecao, String> percentualVariacaoProducaoDiariaColumn, producaoMensalColumn, faturamentoColumn; 
	@FXML private TableColumn<Projecao, String> percentualVariacaoFaturamentoColumn;
	
	@Autowired private ProjecaoService projecaoService;
	
	@FXML
	public void initialize() {
		
		periodoColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("periodo"));
		animaisLactacaoColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("numeroAnimaisLactacao"));
		percentualVariacaoAnimaisLactacaoColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("percentualVariacaoNumeroAnimaisLactacao"));
		animaisSecosColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("numeroAnimaisSecos"));
		percentualVariacaoAnimaisSecosColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("percentualVariacaoNumeroAnimaisSecos"));
		producaoDiariaColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("producaoDiaria"));
		percentualVariacaoProducaoDiariaColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("percentualVariacaoProducaoDiaria"));
		producaoMensalColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("producaoMensal"));
		faturamentoColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("faturamentoMensal"));
		percentualVariacaoFaturamentoColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("percentualVariacaFaturamentoMensal"));
		
		int mesInicio = LocalDate.now().getMonthValue();
		int anoInicio = LocalDate.now().getYear();
		int mesFim = LocalDate.now().plusMonths(9).getMonthValue();
		int anoFim = LocalDate.now().plusMonths(9).getYear();
		
		table.setItems(FXCollections.observableArrayList(
				projecaoService.getProjecaoPeriodo(mesInicio, mesFim, anoInicio, anoFim)));
		
	}

	protected String getFormTitle() {
		return "Projecao";
	}
	
	public String getFormName() {
		return "view/projecao/ProjecaoOverview.fxml";
	}
	
}
