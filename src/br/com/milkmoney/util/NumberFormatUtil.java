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
	
	public static String intFormat(BigDecimal number) {
		return number.setScale(0, BigDecimal.ROUND_HALF_EVEN).toString();
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

	public static String decimalFormat(BigDecimal number, int precision) {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
		df.applyPattern("###,##0.00");
		df.setMinimumFractionDigits(precision);
		DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		return number == null ? df.format(BigDecimal.ZERO) : df.format(number);
	}

	public static BigDecimal intFromString(String value) {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
		df.setMinimumFractionDigits(0);
		DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		try {
			return new BigDecimal(df.parse(value).doubleValue()).setScale(0, BigDecimal.ROUND_HALF_EVEN);
		} catch (ParseException | NullPointerException e) {
			return BigDecimal.ZERO;
		}
	}

}
