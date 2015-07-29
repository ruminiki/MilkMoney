package br.com.milkmoney.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class NumberFormatUtil {

	public static String decimalFormat(BigDecimal number) {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
		df.applyPattern("###,##0.00");
		df.setMinimumFractionDigits(2);
		DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		return number == null ? df.format(BigDecimal.ZERO) : df.format(number);
	}
	
	public static BigDecimal intFormat(BigDecimal number) {
		return number.setScale(0, BigDecimal.ROUND_HALF_EVEN);
	}
	
	public static BigDecimal fromString(String value) {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
		df.applyPattern("###,##0.00");
		df.setMinimumFractionDigits(2);
		DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		try {
			return new BigDecimal(df.parse(value).doubleValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		} catch (ParseException | NullPointerException e) {
			return BigDecimal.ZERO;
		}
	}

}
