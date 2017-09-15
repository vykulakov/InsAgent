package ru.insagent.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;

public class StringToDateTimeConverter extends StrutsTypeConverter {
	private static final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	@Override
	@SuppressWarnings("rawtypes")
	public Object convertFromString(Map context, String[] values, Class toClass) {
		if(values == null || values.length == 0 || values[0].trim().length() == 0) {
			return null;
		}

		try {
			return df.parse(values[0]);
		} catch(ParseException e) {
			throw new TypeConversionException("Unable to convert given object to date: " + values[0]);
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public String convertToString(Map context, Object date) {
		if(date != null && date instanceof Date) {         
			return df.format(date);
		} else {
			return null;
		}
	}
}
