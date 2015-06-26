package br.com.milksys.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import br.com.milksys.util.DateUtil;

public class PropertyDateValueFactory<S, T> extends PropertyValueFactory<S, T> {

	public PropertyDateValueFactory(String property) {
		super(property);
	}
	
	@Override@SuppressWarnings({ "rawtypes", "unchecked" })
	public ObservableValue<T> call(CellDataFeatures<S, T> param) {
		
		ObservableValue<T> a = super.call(param);
		
		if ( a instanceof ObjectProperty ){
			try {
				if ( a.getValue() != null ){
					((ObjectProperty) a).setValue(DateUtil.format(DateUtil.parse(String.valueOf(a.getValue()))));
				}else{
					((ObjectProperty) a).setValue("--");
				}
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
		}
		
		return a;
		
	}

}
