/*
 * InsAgent - https://github.com/vykulakov/InsAgent
 *
 * Copyright 2018 Vyacheslav Kulakov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.insagent.document.action;

import org.apache.struts2.json.annotations.JSON;
import ru.insagent.action.GetBaseAction;
import ru.insagent.document.dao.BsoNormalDao;
import ru.insagent.document.model.BsoNormal;
import ru.insagent.document.model.BsoFilter;
import ru.insagent.util.ExportToExcel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ExportBsoJournalAction extends GetBaseAction<BsoNormal> {
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
		BsoNormalDao dao = new BsoNormalDao();

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
		for (BsoNormal bso : dao.listByRoles(null, filter, sort, order, 0, 0)) {
			List<Object> cells = new LinkedList<Object>();
			cells.add(String.valueOf(index++));
			cells.add(bso.getSeries());
			cells.add(bso.getNumber());
			if (bso.getIssuedDate() == null) {
				cells.add("");
			} else {
				cells.add(df.format(bso.getIssuedDate()));
			}
			if (bso.getIssuedBy() == null) {
				cells.add("");
			} else {
				cells.add(bso.getIssuedBy().getFirstName() + " " + bso.getIssuedBy().getLastName());
			}
			if (bso.getIssuedUnit() == null) {
				cells.add("");
			} else {
				cells.add(bso.getIssuedUnit().getName());
			}
			if (bso.getCorruptedDate() == null) {
				cells.add("");
			} else {
				cells.add(df.format(bso.getCorruptedDate()));
			}
			if (bso.getCorruptedBy() == null) {
				cells.add("");
			} else {
				cells.add(bso.getCorruptedBy().getFirstName() + " " + bso.getCorruptedBy().getLastName());
			}
			if (bso.getCorruptedUnit() == null) {
				cells.add("");
			} else {
				cells.add(bso.getCorruptedUnit().getName());
			}
			if (bso.getRegisteredDate() == null) {
				cells.add("");
			} else {
				cells.add(df.format(bso.getRegisteredDate()));
			}
			if (bso.getRegisteredBy() == null) {
				cells.add("");
			} else {
				cells.add(bso.getRegisteredBy().getFirstName() + " " + bso.getRegisteredBy().getLastName());
			}
			if (bso.getRegisteredUnit() == null) {
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
