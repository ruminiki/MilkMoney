package br.com.milkmoney.controller.cobertura.renderer;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import br.com.milkmoney.model.SituacaoCobertura;

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
	        		super.updateItem(item, empty);

	                if (item == null || empty) {
	                    setText(null);
	                    setStyle("");
	                } else {
	                	
					     if ( item.equals(SituacaoCobertura.PARIDA) )
		                    setStyle("-fx-background-color: #CCFF99;-fx-alignment: CENTER;");
							
						if ( item.equals(SituacaoCobertura.VAZIA) )
							setStyle("-fx-background-color: #FF6600;-fx-alignment: CENTER;");
						
						if ( item.equals(SituacaoCobertura.PRENHA) )
							setStyle("-fx-background-color: #FFCC00;-fx-alignment: CENTER;");
						
						if ( item.equals(SituacaoCobertura.NAO_CONFIRMADA) )
							setStyle("-fx-background-color: #7C867C;-fx-alignment: CENTER;");
						
	                }
				} 
	        }
	    };
	    
	    param.setCellValueFactory(new PropertyValueFactory<S, String>((java.lang.String) property));
	    return cell;
	    
	}
	
}
