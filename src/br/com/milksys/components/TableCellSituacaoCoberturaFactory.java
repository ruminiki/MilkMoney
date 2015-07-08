package br.com.milksys.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import br.com.milksys.model.SituacaoCobertura;

@SuppressWarnings("hiding")
public class TableCellSituacaoCoberturaFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	String property;
	
	public TableCellSituacaoCoberturaFactory(String property) {
		this.property = property;
	}

	@Override
	public TableCell<S, String> call(TableColumn<S, String> param) {
		
	  TableCell<S, String> cell = new TableCell<S, String>() {
		  
	        @Override
	        protected void updateItem(String item, boolean empty) {
	        	if(item!=null){
					
					HBox cell = new HBox();
					cell.setAlignment(Pos.CENTER_LEFT);
					cell.setSpacing(2);
					
					HBox color= new HBox();
					color.setMinWidth(10);
					color.setMaxWidth(10);
					
					if ( item.equals(SituacaoCobertura.PARIDA) )
						color.setStyle("-fx-background-color: #CCFF99");
					
					if ( item.equals(SituacaoCobertura.VAZIA) || item.equals(SituacaoCobertura.REPETIDA) )
						color.setStyle("-fx-background-color: #FF6600");
					
					if ( item.equals(SituacaoCobertura.PRENHA) )
						color.setStyle("-fx-background-color: #FFCC00");
					
					if ( item.equals(SituacaoCobertura.INDEFINIDA) )
						color.setStyle("-fx-background-color: #7C867C");
					
					cell.getChildren().add(color);
					cell.getChildren().add(new Label((java.lang.String)item));
					
					setGraphic(cell);
				} 
	        }
	    };
	    
	    param.setCellValueFactory(new PropertyValueFactory<S, String>((java.lang.String) property));
	    return cell;
	    
	}
	
}
