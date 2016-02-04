package br.com.milkmoney.components;

import java.util.function.Function;

import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

@SuppressWarnings("hiding")
public class TableCellOptionSelectFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	private Function<Object, Boolean> function;
	
	public TableCellOptionSelectFactory(Function<Object, Boolean> function){
		this.function = function;
	}

	@Override
	public TableCell<S, String> call(TableColumn<S, String> param) {
		
		  TableCell<S, String> cell = new TableCell<S, String>() {
			  
		        @Override
		        protected void updateItem(String item, boolean empty) {
		        	if ( tableRowProperty().getValue().getItem() != null ){
		        		if(item!=null){
		        			
		        			/*Button btn = new Button("Selecionar");
		        			btn.setOnAction(e -> {
		        				tableViewProperty().get().getSelectionModel().clearAndSelect(tableRowProperty().get().getIndex());
		        				function.apply(null);
		        			});*/
		        			
		        			Hyperlink btn = new Hyperlink("Selecionar");
		        			btn.setFocusTraversable(false);
		        			btn.setOnAction(e -> {
		        				tableViewProperty().get().getSelectionModel().clearAndSelect(tableRowProperty().get().getIndex());
		        				function.apply(null);
		        			});
							
							setGraphic(btn);
							
						}else{
							setGraphic(null);
						}
					}else{
						setGraphic(null);
					}
		            setStyle("-fx-alignment: CENTER");
		        }
		    };
		    return cell;
	}
	
}
