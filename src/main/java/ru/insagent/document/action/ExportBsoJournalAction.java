package ru.insagent.document.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import ru.insagent.action.GetBaseAction;
import ru.insagent.document.dao.BsoDao;
import ru.insagent.document.model.Bso;
import ru.insagent.document.model.BsoFilter;
import ru.insagent.util.ExportToExcel;

public class ExportBsoJournalAction extends GetBaseAction<Bso> {
	private static final long serialVersionUID = 1L;

	private BsoFilter filter;
	public void setFilter(BsoFilter filter) {
		this.filter = filter;
	}
	@JSON(serialize = false)
	public BsoFilter getFilter() {
		return filter;
	}

	private long contentLength;
	public long getContentLength() {
		return contentLength;
	}

	private InputStream inputStream;
	public InputStream getInputStream() {
		return inputStream;
	}

	{
		ALLOW_ROLES = Arrays.asList("admin", "director", "manager", "register");
		ALLOW_MSG = "У вас нет прав для экспорта журнала БСО";
	}

	@Override
	public String executeImpl() {
		dao = new BsoDao(conn);

		ExportToExcel excel = new ExportToExcel();
		excel.setHeaders(Arrays.asList(
				"№",
				"Серия",
				"Номер",
				"Выдан",
				"Выдал",
				"Выдал",
				"Испорчен",
				"Испортил",
				"Испортил",
				"Зарегистрирован",
				"Зарегистрировал",
				"Зарегистрировал",
				"Страхователь",
				"Страховая премия"));
		excel.setHeaderWidths(Arrays.asList(
				4,
				15,
				15,
				17,
				30,
				30,
				17,
				30,
				30,
				17,
				30,
				30,
				45,
				20));

		int index = 1;
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		for(Bso bso : dao.listByUser(user, filter, sort, order, 0, 0)) {
			List<Object> cells = new LinkedList<Object>();
			cells.add(String.valueOf(index++));
			cells.add(bso.getSeries());
			cells.add(bso.getNumber());
			if(bso.getIssuedDate() == null) {
				cells.add("");
			} else {
				cells.add(df.format(bso.getIssuedDate()));
			}
			if(bso.getIssuedBy() == null) {
				cells.add("");
			} else {
				cells.add(bso.getIssuedBy().getFirstName() + " " + bso.getIssuedBy().getLastName());
			}
			if(bso.getIssuedUnit() == null) {
				cells.add("");
			} else {
				cells.add(bso.getIssuedUnit().getName());
			}
			if(bso.getCorruptedDate() == null) {
				cells.add("");
			} else {
				cells.add(df.format(bso.getCorruptedDate()));
			}
			if(bso.getCorruptedBy() == null) {
				cells.add("");
			} else {
				cells.add(bso.getCorruptedBy().getFirstName() + " " + bso.getCorruptedBy().getLastName());
			}
			if(bso.getCorruptedUnit() == null) {
				cells.add("");
			} else {
				cells.add(bso.getCorruptedUnit().getName());
			}
			if(bso.getRegisteredDate() == null) {
				cells.add("");
			} else {
				cells.add(df.format(bso.getRegisteredDate()));
			}
			if(bso.getRegisteredBy() == null) {
				cells.add("");
			} else {
				cells.add(bso.getRegisteredBy().getFirstName() + " " + bso.getRegisteredBy().getLastName());
			}
			if(bso.getRegisteredUnit() == null) {
				cells.add("");
			} else {
				cells.add(bso.getRegisteredUnit().getName());
			}
			cells.add(bso.getInsured());
			cells.add(bso.getPremium());

			excel.addRow(cells);
		}

		excel.generate();

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		excel.write(os);

		inputStream = new ByteArrayInputStream(os.toByteArray());
		contentLength = os.size();

		return SUCCESS;
	}
}
