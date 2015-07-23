package br.com.milkmoney.components;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import br.com.milkmoney.util.NumberFormatUtil;

public class TableCellDecimalFactory<S, BigDecimal> implements Callback<TableColumn<S ,BigDecimal>, TableCell<S, BigDecimal>>{

	@Override
	public TableCell<S, BigDecimal> call(TableColumn<S, BigDecimal> param) {
		 return new TableCell<S, BigDecimal>() {
		        @Override
		        protected void updateItem(BigDecimal item, boolean empty) {
		            super.updateItem(item, empty);

		            if (item == null || empty) {
		                setText(null);
		                setStyle("0,00");
		            } else {
		                setText(NumberFormatUtil.decimalFormat((java.math.BigDecimal) item));
		            }
		        }
		    };
	}
	
}
