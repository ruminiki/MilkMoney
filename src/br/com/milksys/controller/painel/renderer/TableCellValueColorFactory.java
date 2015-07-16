package br.com.milksys.controller.painel.renderer;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import br.com.milksys.model.Indicador;

@SuppressWarnings("hiding")
public class TableCellValueColorFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	String property;
	
	public TableCellValueColorFactory(String property) {
		this.property = property;
	}

	@Override
	public TableCell<S, String> call(TableColumn<S, String> param) {
		
	  TableCell<S, String> cell = new TableCell<S, String>() {
		  
	        @Override
	        protected void updateItem(String item, boolean empty) {
	        	if(item!=null){
					Label label = new Label();
					label.setText(item.toString());
					
					Indicador indicador = (Indicador) getTableView().getItems().get(getTableRow().getIndex());
					
					if ( indicador.getValorReferencia().compareTo(indicador.getValorApurado()) > 0 ){
						label.setStyle("-fx-text-fill: red");
					}else{
						label.setStyle("-fx-text-fill: green");
					}
					
					setGraphic(label);
				} 
	        }
	    };
	    
	    param.setCellValueFactory(new PropertyValueFactory<S, String>((java.lang.String) property));
	    return cell;
	    
	}
	
}
