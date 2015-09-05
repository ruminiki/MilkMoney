package br.com.milkmoney.controller.categoriaLancamentoFinanceiro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.model.CategoriaLancamentoFinanceiro;
import br.com.milkmoney.service.CategoriaLancamentoFinanceiroService;

@Controller
public class CategoriaLancamentoFinanceiroReducedOverviewController {

	@FXML private TreeView<CategoriaLancamentoFinanceiro> treeView;
	@Autowired private CategoriaLancamentoFinanceiroFormController formController;
	@Autowired private CategoriaLancamentoFinanceiroService service;
	
	private TreeItem<CategoriaLancamentoFinanceiro> rootNode = new TreeItem<CategoriaLancamentoFinanceiro>(new CategoriaLancamentoFinanceiro("Categorias"));
	
	private CategoriaLancamentoFinanceiro selectedCategoria;
	
	@FXML
	public void initialize() {
		
		rootNode.setExpanded(true);
		configuraTreeView();
		
		// captura o evento de double click da tree
		treeView.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 1) {
					if ( treeView.getSelectionModel().getSelectedItem() != null ){
						selectedCategoria = treeView.getSelectionModel().getSelectedItem().getValue();
					}
				}
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					selectedCategoria = treeView.getSelectionModel().getSelectedItem().getValue();
					closeForm();
				}
			}

		});
	}
	
	private void configuraTreeView(){
		
		rootNode.getChildren().clear();
		List<TreeItem<CategoriaLancamentoFinanceiro>> itensSemPai = new ArrayList<TreeItem<CategoriaLancamentoFinanceiro>>();
		
		for ( CategoriaLancamentoFinanceiro categoria : service.findAllAsObservableList() ) {
			
			TreeItem<CategoriaLancamentoFinanceiro> newItem = new TreeItem<CategoriaLancamentoFinanceiro>(categoria);
			newItem.setExpanded(true);
			
			for ( TreeItem<CategoriaLancamentoFinanceiro> itemSemPai : itensSemPai ){
				if ( itemSemPai.getValue().getCategoriaSuperiora().getId() == newItem.getValue().getId() ){
					newItem.getChildren().add(itemSemPai);
					break;
				}
			}
            
            if (!percorreArvore(rootNode, newItem)) {
            	//caso n�o tenha encontrado o item pai, armazena para continuar procurando
            	if ( newItem.getValue().getCategoriaSuperiora() != null ){
            		itensSemPai.add(newItem);
            	}else{
            		rootNode.getChildren().add(newItem);	
            	}
            	
            }
            
        }
		
		treeView.setRoot(rootNode);
		
	}
	
	private boolean percorreArvore(TreeItem<CategoriaLancamentoFinanceiro> root, TreeItem<CategoriaLancamentoFinanceiro> newItem){
		
        for (TreeItem<CategoriaLancamentoFinanceiro> i : root.getChildren()) {
        	
        	//verifica se o novo item � filho do item sendo verificado
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
	private void selecionar(){
		if ( treeView != null && treeView.getSelectionModel().getSelectedItem() != null ){
			closeForm();
		}else{
			CustomAlert.mensagemInfo("Por favor, selecione um registro na tabela.");
		}
	}
	
	private void closeForm(){
		if ( treeView != null ){
			Stage stage = (Stage)treeView.getScene().getWindow();
			// se for popup
			if ( stage.getModality().equals(Modality.APPLICATION_MODAL) ){
				((Stage)treeView.getScene().getWindow()).close();	
			}else{
				MainApp.resetLayout();
			}
		}
	}
	
	public CategoriaLancamentoFinanceiro getObject(){
		return selectedCategoria;
	}
	
	public void showForm(){
    	
    	AnchorPane form = (AnchorPane) MainApp.load(getFormName());
    	
		Stage dialogStage = new Stage();
		dialogStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream(MainApp.APPLICATION_ICON)));
		
		dialogStage.setTitle(getFormTitle());
		
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);

		Scene scene = new Scene(form);
		dialogStage.setScene(scene);

		dialogStage.setResizable(false);
		dialogStage.showAndWait();
		
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
		return "Categoria de Lan�amento";
	}
	
	public String getFormName() {
		return "view/categoriaLancamentoFinanceiro/CategoriaLancamentoFinanceiroReducedOverview.fxml";
	}
	
}