package br.com.milkmoney.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateUtil {
	
	/** O padrão usado para conversão. Mude como quiser. */
	private static final String DATE_PATTERN = "dd/MM/yyyy";
	
	/** O formatador de data. */
	private static final DateTimeFormatter DATE_FORMATTER = 
	        DateTimeFormatter.ofPattern(DATE_PATTERN);
	
	/**
	 * Retorna os dados como String formatado. O 
	 * {@link DateUtil#DATE_PATTERN}  (padrão de data) que é utilizado.
	 * 
	 * @param item A data a ser retornada como String
	 * @return String formadado
	 */
	public static String format(LocalDate item) {
	    if (item == null) {
	        return "";
	    }
	    return DATE_FORMATTER.format(item);
	}
	
	/**
	 * Retorna os dados como String formatado. O 
	 * {@link DateUtil#DATE_PATTERN}  (padrão de data) que é utilizado.
	 * 
	 * @param date A data a ser retornada como String
	 * @return String formadado
	 */
	public static String format(Date date) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
	    if (date == null) {
	        return "";
	    }
	    return sdf.format(date);
	    
	}
	
	
    public static LocalDate asLocalDate(Date date) {
        if (date == null)
            return null;

        if (date instanceof java.sql.Date)
            return ((java.sql.Date) date).toLocalDate();
        else
            return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
    
    public static Date asDate(LocalDate localDate) {
        if (localDate == null)
            return null;
        else
        	return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

	
	/**
	 * Converte um String no formato definido {@link DateUtil#DATE_PATTERN} 
	 * para um objeto {@link LocalDate}.
	 * 
	 * Retorna null se o String não puder se convertido.
	 * 
	 * @param dateString a data como String
	 * @return o objeto data ou null se não puder ser convertido
	 */
	public static LocalDate parse(String dateString) {
	    try {
	        return DATE_FORMATTER.parse(dateString, LocalDate::from);
	    } catch (DateTimeParseException e) {
	        return null;
	    }
	}
	
	/**
	 * Checa se o String é uma data válida.
	 * 
	 * @param dateString A data como String
	 * @return true se o String é uma data válida
	 */
	public static boolean validDate(String dateString) {
	    // Tenta converter o String.
	    return DateUtil.parse(dateString) != null;
	}

	/**
	 * Checa se as datas são o mesmo dia, sem considerar as horas.
	 * @param data1
	 * @param data2
	 * @return
	 */
	public static boolean isSameDate(Date data1, Date data2) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMYYYY");
		if ( sdf.format(data1).equals(sdf.format(data2)) )
			return true;
		return false;
		
	}

	/**
	 * Verifica se a primeira data é maior que a segunda, sem considerar hora.
	 * @param data
	 * @param date
	 * @return
	 */
	public static boolean after(Date data1, Date data2) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
		
		try{
			return Integer.parseInt(sdf.format(data1)) > Integer.parseInt(sdf.format(data2));
		}catch(Exception e){
			return false;
		}
		
	}
	
	public static boolean before(Date data1, Date data2) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
		
		try{
			return Integer.parseInt(sdf.format(data1)) < Integer.parseInt(sdf.format(data2));
		}catch(Exception e){
			return false;
		}
		
	}
}

