package ru.insagent.util;

import java.util.Collection;

public class Utils {
	/**
	 * Приватный конструктор, чтобы класс нельзя было инициализировать.
	 */
	private Utils() {
	}

	public static String convertNumbersToRanges(Collection<Long> numbers) {
		long numberFrom = 0, numberTo = 0;
		StringBuilder sb = new StringBuilder();
		for(long number : numbers) {
			if(numberFrom == 0) {
				numberFrom = number;
				numberTo = number;
				continue;
			}
			if(numberTo + 1 < number) {
				appendRange(sb, numberFrom, numberTo);
				numberFrom = number;
				numberTo = number;
				continue;
			}
			
			numberTo = number;
		}
		appendRange(sb, numberFrom, numberTo);
		if(sb.length() > 0) {
			sb.setLength(sb.length() - 2);
		}

		return sb.toString();
	}


	private static void appendRange(StringBuilder sb, long numberFrom, long numberTo) {
		if(numberFrom > 0 && numberTo > 0) {
			if(numberFrom == numberTo) {
				sb.append(numberFrom);
				sb.append(", ");
			}
			if(numberFrom + 1 == numberTo) {
				sb.append(numberFrom);
				sb.append(", ");
				sb.append(numberTo);
				sb.append(", ");
			}
			if(numberFrom + 1 < numberTo) {
				sb.append(numberFrom);
				sb.append(" - ");
				sb.append(numberTo);
				sb.append(", ");
			}
		}
	}
}
