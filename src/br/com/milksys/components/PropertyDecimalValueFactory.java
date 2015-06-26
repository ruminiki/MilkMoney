package br.com.milksys.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import br.com.milksys.util.NumberFormatUtil;

public class PropertyDecimalValueFactory<S, T> extends PropertyValueFactory<S, T> {

	public PropertyDecimalValueFactory(String property) {
		super(property);
	}
	
	@Override@SuppressWarnings({ "rawtypes", "unchecked" })
	public ObservableValue<T> call(CellDataFeatures<S, T> param) {
		
		ObservableValue<T> a = super.call(param);
		
		if ( a instanceof ObjectProperty ){
			try {
				
				((ObjectProperty) a).setValue(NumberFormatUtil.decimalFormat(NumberFormatUtil.fromString(String.valueOf(a.getValue()))));
				
			} catch (Exception e) {
				
			}
			
		}
		
		return a;
		
	}

}
