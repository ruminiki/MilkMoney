package br.com.milkmoney.controller.servico.renderer;

import java.util.Date;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import br.com.milkmoney.model.LancamentoFinanceiro;
import br.com.milkmoney.model.Servico;
import br.com.milkmoney.util.DateUtil;

@SuppressWarnings("hiding")
public class TableCellSituacaoLancamentoFinanceiroFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	String property;
	
	public TableCellSituacaoLancamentoFinanceiroFactory(String property) {
		this.property = property;
	}

	@Override
	public TableCell<S, String> call(TableColumn<S, String> param) {
		
	  TableCell<S, String> cell = new TableCell<S, String>() {
		  
	        @Override
	        protected void updateItem(String item, boolean empty) {
	        	if(item!=null){
                	Servico servico = (Servico) tableViewProperty().get().getItems().get(tableRowProperty().get().getIndex());
                	LancamentoFinanceiro lancamento = servico.getLancamentoFinanceiro();
                	//não integrado
                	if ( lancamento == null ){
	                    setStyle("-fx-background-color: #CCC;-fx-alignment: CENTER;");
                	}
                	
                	//pago
                	if ( lancamento != null && lancamento.getDataPagamento() != null ){
	                    setStyle("-fx-background-color: #CCFF99;-fx-alignment: CENTER;");
                	}
        	
                	//vencido
                	if ( lancamento != null && lancamento.getDataPagamento() == null && 
                			DateUtil.after(new Date(), lancamento.getDataVencimento()) ){
                		setStyle("-fx-background-color: #FF8585;-fx-alignment: CENTER;");
                	}
                	
                	//não vencido
                	if ( lancamento != null && lancamento.getDataPagamento() == null && 
                			( DateUtil.after(lancamento.getDataVencimento(), new Date()) || DateUtil.isSameDate(lancamento.getDataVencimento(), new Date())) ){
                		setStyle("-fx-background-color: #B2CCFF;-fx-alignment: CENTER;");
                	}
					
                }else{
                	setStyle(null);
                }
	        }
	    };
	    param.setCellValueFactory(new PropertyValueFactory<S, String>((java.lang.String) property));
	    return cell;
	    
	}
	
}
