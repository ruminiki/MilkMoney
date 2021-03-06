package br.com.milkmoney.controller.categoriaLancamentoFinanceiro;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.controller.AbstractReducedOverviewController;
import br.com.milkmoney.model.CategoriaLancamentoFinanceiro;
import br.com.milkmoney.model.Limit;
import br.com.milkmoney.service.IService;

@Controller
public class CategoriaLancamentoFinanceiroReducedOverviewController extends AbstractReducedOverviewController<Integer, CategoriaLancamentoFinanceiro>  {

	@FXML private ListView<CategoriaLancamentoFinanceiro> listCategorias;
	@Autowired private CategoriaLancamentoFinanceiroFormController formController;
	
	@FXML
	public void initialize() {
		
		listCategorias.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					handleSelecionar();
				}
				
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 1) {
					handleSelectItemTable();
				}
			}

		});
		
		listCategorias.setContextMenu(getContextMenu());
		super.initialize(formController);
		
	}

	@Override
	protected void refreshTableOverview() {
		this.data.clear();
		
		if ( inputPesquisa != null && inputPesquisa.getText() != null &&
				inputPesquisa.getText().length() > 0){
			data.addAll(service.defaultSearch(inputPesquisa.getText(), Limit.UNLIMITED));
		}else{
			data = service.findAllAsObservableList();
		}
		
		listCategorias.setItems(data);
		
	}
	
	@Override
	protected void handleDelete() {
		int selectedIndex = listCategorias.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			CustomAlert.confirmarExclusao();
			if ( CustomAlert.response == ButtonType.OK ) {
				
				try {
					service.remove(data.get(selectedIndex));
				} catch (Exception e) {
					CustomAlert.mensagemAlerta("", e.getMessage());
					return;
				}
				
				data.remove(selectedIndex);
				updateLabelNumRegistros();
			}
		} else {
			CustomAlert.nenhumRegistroSelecionado();		
		}
	}
	
	@Override
	protected void handleSelectItemTable() {
		setObject(listCategorias.getSelectionModel().getSelectedItem());
	}
	
	@Override
	protected void handleSelecionar(){
		if ( listCategorias != null && listCategorias.getSelectionModel().getSelectedItem() != null ){
			closeForm();
		}else{
			CustomAlert.mensagemInfo("Por favor, selecione um registro.");
		}
	}
	
	@Override
	public String getFormTitle() {
		return "Categoria de Lanšamento";
	}
	
	@Override
	public String getFormName() {
		return "view/categoriaLancamentoFinanceiro/CategoriaLancamentoFinanceiroReducedOverview.fxml";
	}
	
	@Override
	@Resource(name = "categoriaLancamentoFinanceiroService")
	protected void setService(IService<Integer, CategoriaLancamentoFinanceiro> service) {
		super.setService(service);
	}
	
}
