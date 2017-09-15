package ru.insagent.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.insagent.exception.AppException;

/**
 * <p>Класс для экспорта отчётов в Excel</p>
 * <p>Для каждого нового экспорта нужно использовать новый экземпляр класса. Повторно использовать объект не стоит!</p>
 * @author "Vyacheslav Kulakov <vkulakov@joxnet.ru>"
 */
public class ExportToExcel {
	private Workbook wb;
	private List<String> headers;
	private List<Integer> headerWidths;
	private List<List<Object>> rows;
	
	static Logger logger = LoggerFactory.getLogger(ExportToExcel.class);	

	public ExportToExcel() {
		headers = new LinkedList<String>();
		rows = new LinkedList<List<Object>>();
		headerWidths = new LinkedList<Integer>();
	}

	/**
	 * <p>Устанавливает заголовки для таблицы</p>
	 * @param headers Список заголовков для таблицы в Excel
	 */
	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	/**
	 * <p>Добавляет к списку заголовков таблицы заголовок конкретного поля<p>
	 * <p>Заголовки в Excel будут идти в том порядке, в котором они были добавлены с помощью этого метода.</p>
	 * @param header Заголовок поля для таблицы
	 */
	public void addHeader(String header) {
		this.headers.add(header);
	}
	
	/**
	 * <p>Устанавливает ширину ячеек для заголовков и соответственно для всех столбцов таблицы</p>
	 * @param headerWidths Список размеров ячеек для заголовков
	 */
	public void setHeaderWidths(List<Integer> headerWidths) {
		this.headerWidths = headerWidths;
	}

	/**
	 * <p>Добавляет к списку размеров заголовков таблицы ширину конкретного поля<p>
	 * <p>Ширина заголовка в Excel будет идти в том порядке, в котором она была добавлена с помощью этого метода.</p>
	 * @param headerWidth Ширина ячейки для заголовка
	 */
	public void addHeaderWidth(int headerWidth) {
		this.headerWidths.add(headerWidth);
	}

	/**
	 * <p>Устанавливает строки для таблицы</p>
	 * <p>Каждая строка таблицы представляет собой список объектов класса String. Т. е. таблица - это двумерный список строк.</p>
	 * @param rows Список строк таблицы
	 */
	public void setRows(List<List<Object>> rows) {
		this.rows = rows;
	}

	/**
	 * <p>Добавляет к списку строк таблицы новую строку<p>
	 * <p>Строки в Excel будут идти в том порядке, в котором они были добавлены с помощью этого метода.</p>
	 * @param row Строка в таблице
	 */
	public void addRow(List<Object> row) {
		this.rows.add(row);
	}

	/**
	 * <p>Гененрирование отчёта в Excel</p>
	 * <p>На основе добавленных заголовка и строк, генерирует отчёт в Excel и запоминает его до последующего вызова метода {@link ru.joxnet.bss.web.ExportToExcel#write(OutputStream) write}.</p>
	 * <p>Во время генерирования отчёта проверяется, что заголовок и строки не пустые и что заголовок и все строки имеют одинаковую длину.</p>
	 * @throws Exception 
	 */
	public void generate() {
		if(wb != null) {
			throw new AppException("WorkBook is not null!");
		}

		wb = new XSSFWorkbook();
				
		Font headerFont = wb.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		
		CellStyle headerCellStyle = wb.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		CellStyle rowCellStyle = wb.createCellStyle();
		rowCellStyle.setVerticalAlignment(CellStyle.VERTICAL_JUSTIFY);
		
		DataFormat numberFormat = wb.createDataFormat();
		CellStyle numberCellStyle = wb.createCellStyle();
		numberCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		numberCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		numberCellStyle.setDataFormat(numberFormat.getFormat("0.00"));
		
		int numberCellType = HSSFCell.CELL_TYPE_NUMERIC;
		
		Sheet sheet = wb.createSheet("Отчёт");
		
		short rowNumber = 0;

		Row rowHeader = sheet.createRow(rowNumber);
		Cell cellHeader;

		if(headers.size() != headerWidths.size()) {
			throw new AppException("Количество заголовков таблицы не соответствует количеству ширин заголовков");
		}
		for(int i = 0; i < headers.size(); i++){
			cellHeader = rowHeader.createCell(i);
			cellHeader.setCellValue(headers.get(i));
			cellHeader.setCellStyle(headerCellStyle);
			sheet.setColumnWidth(i, 256*headerWidths.get(i));
		}
		
		sheet.createFreezePane(0, 1);
		
		for(List<Object> row : rows) {
			rowNumber++;

			Row rowRow = sheet.createRow(rowNumber);
			int ir = 0;
			for(Object value : row) {
				Cell cellRow = rowRow.createCell(ir);
				if(value != null) {
					if(value instanceof BigDecimal) {
						cellRow.setCellType(numberCellType);
						cellRow.setCellValue(((BigDecimal) value).setScale(2, RoundingMode.HALF_UP).doubleValue());
						cellRow.setCellStyle(numberCellStyle);
					} else if(value instanceof Integer) {
						cellRow.setCellValue((Integer) value);
					} else  {
						cellRow.setCellValue(value.toString());
						cellRow.setCellStyle(rowCellStyle);
					}
				}

				ir++;
			}
			if(ir < headers.size()) {
				throw new AppException("Количество заголовков больше количества ячеек в строке " + rowNumber);
			}
			if(ir > headers.size()) {
				throw new AppException("Количество заголовков меньше количества ячеек в строке " + rowNumber);
			}
		}
	}


	/**
	 * <p>Запись отчёта в Excel в выходной поток</p>
	 * <p>Записывает отчёт, сформированный методом {@link ru.insagent.util.ExportToExcel#generate() generate}, в выходной поток.</p>
	 * <p>Удобно для записи в файл или для передачи отчёта сразу в браузер клиента.</p>
	 * @param writer Выходной поток для записи отчёта
	 * @throws Exception 
	 */
	public void write(OutputStream writer) {

		if(wb == null) {
			throw new AppException("WorkBook is null!");
		}

		try {
			wb.write(writer);
		} catch (FileNotFoundException e) {
			logger.error("Невозможно открыть выходной поток: " + e.getMessage());
			throw new AppException("Невозможно открыть выходной поток", e);
		} catch (IOException e) {
			logger.error("Невозможно сохранить рабочую книгу в файл: " + e.getMessage());
			throw new AppException("Невозможно сохранить рабочую книгу в файл", e);
		}
	}
}
