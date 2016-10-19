package br.com.milkmoney.controller.procedimento.renderer;

import br.com.milkmoney.model.SimNao;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

@SuppressWarnings("hiding")
public class TableCellCarenciaFactory<S, Boolean> implements Callback<TableColumn<S, Boolean>, TableCell<S, Boolean>>{
	
	String property;
	
	public TableCellCarenciaFactory(String property) {
		this.property = property;
	}

	@Override
	public TableCell<S, Boolean> call(TableColumn<S, Boolean> param) {
		TableCell<S, Boolean> cell = new TableCell<S, Boolean>() {
			  
	        @Override
	        protected void updateItem(Boolean item, boolean empty) {
	          
	            if ( getTableRow()!= null && getTableRow().getItem() != null ){
		            if ( (item == null || empty) ) {
	            		setText("--");
		            } else {
		                try{
		                	if ( (java.lang.Boolean)item ){
		                		 setStyle("-fx-text-fill: RED;-fx-alignment: CENTER;");
		                		setText(SimNao.SIM);
		                	}else{
		                		setStyle("-fx-text-fill: GREEN;-fx-alignment: CENTER;");
		                		setText(SimNao.NAO);
		                	}
		                }catch(Exception e){
		                	System.out.println(e);
		                }
		            }
	            }else{
	            	setText(null);
	            	setStyle("");
	            }
	            
	            super.updateItem(item, empty);
	        }
	        
	        
	    };
	    
	    param.setCellValueFactory(new PropertyValueFactory<S, Boolean>(property));
	    
	    return cell;
	}
	
}
