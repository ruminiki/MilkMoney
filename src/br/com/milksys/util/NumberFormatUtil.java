package br.com.milksys.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

public class NumberFormatUtil {

	public static String decimalFormat(BigDecimal number) {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		df.applyPattern("###,##0.00");
		DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		return df.format(number);
	}

	public static BigDecimal fromString(String value) {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		df.applyPattern("###,##0.00");
		DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		try {
			return new BigDecimal(df.parse(value).doubleValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}