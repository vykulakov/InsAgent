package ru.insagent.converter;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;

public class StringToBigDecimalConverter extends StrutsTypeConverter {
	@Override
	@SuppressWarnings("rawtypes")
	public Object convertFromString(Map context, String[] values, Class toClass) {
		if(values == null || values.length == 0 || values[0].trim().length() == 0) {
			return null;
		}

		try {
			return new BigDecimal(values[0].replace(",", "."));
		} catch(Exception e) {
			throw new TypeConversionException("Unable to convert given object to BigDecimal: " + values[0]);
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public String convertToString(Map context, Object o) {
		if(o != null && o instanceof BigDecimal) {         
			return ((BigDecimal) o).toPlainString();
		} else {
			return null;
		}
	}
}
