package br.com.milkmoney.controller.configuracaoIndicador;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.ConfiguracaoIndicador;
import br.com.milkmoney.service.ConfiguracaoIndicadorService;
import br.com.milkmoney.service.IService;

@Controller
public class ConfiguracaoIndicadorOverviewController extends AbstractOverviewController<Integer, ConfiguracaoIndicador> {

	@FXML private TableColumn<ConfiguracaoIndicador, String> indicadorColumn;
	@FXML private TableColumn<ConfiguracaoIndicador, String> anoColumn;
	@FXML private TableColumn<ConfiguracaoIndicador, String> menorValorColumn;
	@FXML private TableColumn<ConfiguracaoIndicador, String> maiorValorColumn;
	@FXML private TableColumn<ConfiguracaoIndicador, String> objetivoColumn;
	
	@Autowired private ConfiguracaoIndicadorFormController formController;
	
	private int ano;
	
	@FXML
	public void initialize() {
		
		indicadorColumn.setCellValueFactory(new PropertyValueFactory<ConfiguracaoIndicador,String>("indicador"));
		anoColumn.setCellValueFactory(new PropertyValueFactory<ConfiguracaoIndicador,String>("ano"));
		menorValorColumn.setCellValueFactory(new PropertyValueFactory<ConfiguracaoIndicador,String>("menorValorEsperado"));
		maiorValorColumn.setCellValueFactory(new PropertyValueFactory<ConfiguracaoIndicador,String>("maiorValorEsperado"));
		objetivoColumn.setCellValueFactory(new PropertyValueFactory<ConfiguracaoIndicador,String>("objetivo"));
		
		super.initialize(formController);
		
	}
	
	@Override
	protected void refreshTableOverview() {
		
		this.data.clear();
		this.table.getItems().clear();
		
		if ( inputPesquisa != null && inputPesquisa.getText() != null &&
				inputPesquisa.getText().length() > 0){
			data.addAll(handleDefaultSearch());
		}else{
			data.addAll(((ConfiguracaoIndicadorService)service).findByYear(ano));
		}
		
		table.setItems(data);
		table.layout();
		updateLabelNumRegistros();
		
	}
	
	@Override
	public ObservableList<ConfiguracaoIndicador> handleDefaultSearch() {
		
		Object[] params = new Object[]{
				inputPesquisa.getText(),
				ano
		};
		return ((ConfiguracaoIndicadorService)service).defaultSearch(params);
		
	}
	
	public void setAno(int ano) {
		this.ano = ano;
	}

	@Override
	public String getFormTitle() {
		return "Configuração Indicador";
	}
	
	@Override
	public String getFormName() {
		return "view/configuracaoIndicador/ConfiguracaoIndicadorOverview.fxml";
	}
	
	@Override
	@Resource(name = "configuracaoIndicadorService")
	protected void setService(IService<Integer, ConfiguracaoIndicador> service) {
		super.setService(service);
	}

}
