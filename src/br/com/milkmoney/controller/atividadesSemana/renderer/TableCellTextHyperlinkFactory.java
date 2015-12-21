package br.com.milkmoney.controller.atividadesSemana.renderer;

import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

@SuppressWarnings("hiding")
public class TableCellTextHyperlinkFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	private Function<Integer, Boolean> function;
	private String label;
	
	public TableCellTextHyperlinkFactory(String label, Function<Integer, Boolean> function) {
		this.function = function;
		this.label = label;
	}

	@Override
	public TableCell<S, String> call(TableColumn<S, String> param) {
		
		  TableCell<S, String> cell = new TableCell<S, String>() {
			  
		        @Override
		        protected void updateItem(String item, boolean empty) {
		        	if ( tableRowProperty().getValue().getItem() != null ){
						Hyperlink hp = new Hyperlink();
						hp.setText((java.lang.String)label);
						hp.setFocusTraversable(false);
						hp.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								function.apply(getTableRow().getIndex());
							}
						});
						setGraphic(hp);
					}else{
						setGraphic(null);
					}
		            setStyle("-fx-alignment: CENTER");
		        }
		    };
		    return cell;
	}
	
}
