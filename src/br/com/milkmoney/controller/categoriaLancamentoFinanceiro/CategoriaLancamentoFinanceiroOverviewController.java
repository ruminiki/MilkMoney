package br.com.milkmoney.controller.categoriaLancamentoFinanceiro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.model.CategoriaLancamentoFinanceiro;
import br.com.milkmoney.service.CategoriaLancamentoFinanceiroService;

@Controller
public class CategoriaLancamentoFinanceiroOverviewController {

	@FXML private TreeView<CategoriaLancamentoFinanceiro> treeView;
	@Autowired private CategoriaLancamentoFinanceiroFormController formController;
	@Autowired private CategoriaLancamentoFinanceiroService service;
	@FXML protected TextField inputPesquisa;
	private ObservableList<CategoriaLancamentoFinanceiro> data = FXCollections.observableArrayList();
	
	private TreeItem<CategoriaLancamentoFinanceiro> rootNode = new TreeItem<CategoriaLancamentoFinanceiro>(new CategoriaLancamentoFinanceiro("Categorias"));
	
	@FXML
	public void initialize() {
		
		rootNode.setExpanded(true);

		// captura o evento de double click da tree
		treeView.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					handleEdit();
				}
			}

		});
		
		
		if ( inputPesquisa != null ){
			inputPesquisa.textProperty().addListener((observable, oldValue, newValue) -> refreshListOverview());
		}
		
		data = service.findAllAsObservableList();
		configuraTreeView();
	}
	
	private void refreshListOverview(){
		this.data.clear();
		
		if ( inputPesquisa != null && inputPesquisa.getText() != null &&
				inputPesquisa.getText().length() > 0){
			data.addAll(service.defaultSearch(inputPesquisa.getText()));
		}else{
			data = service.findAllAsObservableList();
		}
		configuraTreeView();
	}
	
	private void configuraTreeView(){
		
		rootNode.getChildren().clear();
		List<TreeItem<CategoriaLancamentoFinanceiro>> itensSemPai = new ArrayList<TreeItem<CategoriaLancamentoFinanceiro>>();
		
		for ( CategoriaLancamentoFinanceiro categoria : data ) {
			
			TreeItem<CategoriaLancamentoFinanceiro> newItem = new TreeItem<CategoriaLancamentoFinanceiro>(categoria);
			newItem.setExpanded(true);
			
			for ( TreeItem<CategoriaLancamentoFinanceiro> itemSemPai : itensSemPai ){
				if ( itemSemPai.getValue().getCategoriaSuperiora().getId() == newItem.getValue().getId() ){
					newItem.getChildren().add(itemSemPai);
					itensSemPai.remove(itemSemPai);
					break;
				}
			}
            
            if (!percorreArvore(rootNode, newItem)) {
            	//caso não tenha encontrado o item pai, armazena para continuar procurando
            	if ( newItem.getValue().getCategoriaSuperiora() != null ){
            		itensSemPai.add(newItem);
            	}else{
            		rootNode.getChildren().add(newItem);	
            	}
            	
            }
            
        }
		
		if ( itensSemPai.size() > 0 ){
			rootNode.getChildren().addAll(itensSemPai);	
		}
		
		treeView.setRoot(rootNode);
		
	}
	
	private boolean percorreArvore(TreeItem<CategoriaLancamentoFinanceiro> root, TreeItem<CategoriaLancamentoFinanceiro> newItem){
		
        for (TreeItem<CategoriaLancamentoFinanceiro> i : root.getChildren()) {
        	
        	//verifica se o novo item é filho do item sendo verificado
            if ( newItem.getValue().getCategoriaSuperiora() != null && 
            		newItem.getValue().getCategoriaSuperiora().getId() == i.getValue().getId() ){
            	
            	i.getChildren().add(newItem);
                return true;
                
            }else{
            	if ( i.getValue().getCategoriaSuperiora() != null && 
                		i.getValue().getCategoriaSuperiora().getId() == newItem.getValue().getId() ){
                	
            		root.getChildren().remove(i);
            		newItem.getChildren().add(i);
                    return true;
            	}else{
            		//entra dentro do ramo de uma arvore
            		if ( i.getChildren() != null && i.getChildren().size() > 0 ){
            			percorreArvore(i, newItem);
            		}
            	}
            }
        }
        
        return false;
        
	}
	
	@FXML
	private void handleNew(){
		formController.setObject(new CategoriaLancamentoFinanceiro());
		formController.showForm();
		configuraTreeView();
	}
	
	@FXML 
	private void handleEdit(){
		if ( treeView.getSelectionModel().getSelectedItem() != null && treeView.getSelectionModel().getSelectedItem().getValue().getId() > 0 ){
			formController.setObject(treeView.getSelectionModel().getSelectedItem().getValue());
			formController.showForm();
			configuraTreeView();
		}
	}
	
	@FXML
	private void handleDelete(){
		if ( treeView.getSelectionModel().getSelectedItem() != null && treeView.getSelectionModel().getSelectedItem().getValue().getId() > 0 ){
			
			Optional<ButtonType> result = CustomAlert.confirmarExclusao();
			if (result.get() == ButtonType.OK) {
				
				try {
					service.remove(treeView.getSelectionModel().getSelectedItem().getValue());
					configuraTreeView();
				} catch (Exception e) {
					CustomAlert.mensagemAlerta("", e.getMessage());
					return;
				}
				
			}
			
		}
	}

	public String getFormTitle() {
		return "Categoria de Lançamento";
	}
	
	public String getFormName() {
		return "view/categoriaLancamentoFinanceiro/CategoriaLancamentoFinanceiroReducedOverview.fxml";
	}
	
}
