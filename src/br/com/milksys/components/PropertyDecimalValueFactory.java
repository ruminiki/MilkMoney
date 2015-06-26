package br.com.milksys.components;

import java.math.BigDecimal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import br.com.milksys.util.NumberFormatUtil;

public class PropertyDecimalValueFactory<S, T> extends
		PropertyValueFactory<S, T> {

	public PropertyDecimalValueFactory(String property) {
		super(property);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ObservableValue<T> call(CellDataFeatures<S, T> param) {

		ObservableValue<T> a = super.call(param);

		if (a.getValue() instanceof BigDecimal) {
			
			((ObjectProperty) a).setValue(NumberFormatUtil.decimalFormat((BigDecimal) a.getValue()));

		}

		return a;

	}

}
