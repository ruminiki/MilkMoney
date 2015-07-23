package br.com.milksys.controller.painel.renderer;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import br.com.milksys.model.Indicador;

@SuppressWarnings("hiding")
public class TableCellValueFactoryResultadoIndicador<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	String property;
	
	public TableCellValueFactoryResultadoIndicador(String property) {
		this.property = property;
	}

	@Override
	public TableCell<S, String> call(TableColumn<S, String> param) {
		
	  TableCell<S, String> cell = new TableCell<S, String>() {
		  
	        @Override
	        protected void updateItem(String item, boolean empty) {
	        	if(item!=null){
					Indicador indicador = (Indicador) getTableView().getItems().get(getTableRow().getIndex());
					/*if ( indicador.getResultado().compareTo(BigDecimal.valueOf(70)) < 0 ){
						setTextFill(Color.RED);
					}
					
					if ( indicador.getResultado().compareTo(BigDecimal.valueOf(100)) < 0 ){
						setTextFill(Color.YELLOW);
					}
					
					if ( indicador.getResultado().compareTo(BigDecimal.valueOf(100)) >= 0 ){
						setTextFill(Color.GREEN);
					}*/
					setText(indicador.getResultado().toString() + "%");
					setStyle("-fx-alignment: CENTER; -fx-font-weight: bold;");
				} 
	        }
	    };
	    
	    param.setCellValueFactory(new PropertyValueFactory<S, String>((java.lang.String) property));
	    return cell;
	    
	}
	
}
