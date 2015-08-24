package br.com.milkmoney.controller.categoriaDespesa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.model.CategoriaDespesa;
import br.com.milkmoney.service.CategoriaDespesaService;

@Controller
public class CategoriaDespesaOverviewController {

	@FXML private TreeView<CategoriaDespesa> treeView;
	@Autowired private CategoriaDespesaFormController formController;
	@Autowired private CategoriaDespesaService service;
	
	private TreeItem<CategoriaDespesa> rootNode = new TreeItem<CategoriaDespesa>(new CategoriaDespesa("Categorias"));
	
	@FXML
	public void initialize() {
		
		rootNode.setExpanded(true);
		configuraTreeView();
		
		// captura o evento de double click da tree
		treeView.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					handleEdit();
				}
			}

		});
	}
	
	private void configuraTreeView(){
		
		rootNode.getChildren().clear();
		List<TreeItem<CategoriaDespesa>> itensSemPai = new ArrayList<TreeItem<CategoriaDespesa>>();
		
		for ( CategoriaDespesa categoria : service.findAllAsObservableList() ) {
			
			TreeItem<CategoriaDespesa> newItem = new TreeItem<CategoriaDespesa>(categoria);
			newItem.setExpanded(true);
			
			for ( TreeItem<CategoriaDespesa> itemSemPai : itensSemPai ){
				if ( itemSemPai.getValue().getCategoriaSuperiora().getId() == newItem.getValue().getId() ){
					newItem.getChildren().add(itemSemPai);
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
		
		treeView.setRoot(rootNode);
		
	}
	
	private boolean percorreArvore(TreeItem<CategoriaDespesa> root, TreeItem<CategoriaDespesa> newItem){
		
        for (TreeItem<CategoriaDespesa> i : root.getChildren()) {
        	
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
            		
            		if ( i.getChildren() != null && i.getChildren().size() > 0 ){
            			return percorreArvore(i, newItem);
            		}
            		
            	}
                    
            }
            
        }
        
        return false;
        
	}
	
	@FXML
	private void handleNew(){
		formController.setObject(new CategoriaDespesa());
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
		return "Categoria de Despesa";
	}
	
	public String getFormName() {
		return "view/categoriaDespesa/CategoriaDespesaOverview.fxml";
	}
	
}
