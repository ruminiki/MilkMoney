package br.com.milkmoney.controller.lancamentoFinanceiro.renderer;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import br.com.milkmoney.model.LancamentoFinanceiro;
import br.com.milkmoney.model.TipoLancamentoFinanceiro;

@SuppressWarnings("hiding")
public class TableCellValorLancamentoFinanceiroFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	String property;
	
	public TableCellValorLancamentoFinanceiroFactory(String property) {
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
	                	setGraphic(null);
	                } else {
	                	
	                	LancamentoFinanceiro lancamento = (LancamentoFinanceiro) param.getTableView().getItems().get(tableRowProperty().get().getIndex());
	                	
	                	Label label = new Label("R$ " + lancamento.getValorTotal());
	                	if ( lancamento.getTipoLancamento().equals(TipoLancamentoFinanceiro.RECEITA) ){
	                		label.setTextFill(Color.GREEN);
	                	}else{
	                		label.setTextFill(Color.RED);
	                	}
	                	
	                	setGraphic(label);
            	
	                }
				}else{
					setGraphic(null);
				}
	        }
	    };
	    param.setCellValueFactory(new PropertyValueFactory<S, String>((java.lang.String) property));
	    return cell;
	    
	}
	
}
