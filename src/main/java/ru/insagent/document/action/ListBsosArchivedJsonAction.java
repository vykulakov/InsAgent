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

import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import org.apache.struts2.json.annotations.JSON;
import ru.insagent.action.GetBaseAction;
import ru.insagent.document.dao.BsoArchivedDao;
import ru.insagent.document.model.Bso;
import ru.insagent.document.model.BsoFilter;

import java.util.Arrays;
import java.util.List;

@Conversion(
		conversions = {
				@TypeConversion(key = "filter.premiumFrom", converter = "ru.insagent.converter.StringToBigDecimalConverter"),
				@TypeConversion(key = "filter.premiumTo", converter = "ru.insagent.converter.StringToBigDecimalConverter"),
				@TypeConversion(key = "filter.createdFrom", converter = "ru.insagent.converter.StringToDateTimeConverter"),
				@TypeConversion(key = "filter.createdTo", converter = "ru.insagent.converter.StringToDateTimeConverter"),
				@TypeConversion(key = "filter.issuedFrom", converter = "ru.insagent.converter.StringToDateTimeConverter"),
				@TypeConversion(key = "filter.issuedTo", converter = "ru.insagent.converter.StringToDateTimeConverter"),
				@TypeConversion(key = "filter.corruptedFrom", converter = "ru.insagent.converter.StringToDateTimeConverter"),
				@TypeConversion(key = "filter.corruptedTo", converter = "ru.insagent.converter.StringToDateTimeConverter"),
				@TypeConversion(key = "filter.registerFrom", converter = "ru.insagent.converter.StringToDateTimeConverter"),
				@TypeConversion(key = "filter.registerTo", converter = "ru.insagent.converter.StringToDateTimeConverter")
		}
)
public class ListBsosArchivedJsonAction extends GetBaseAction<Bso> {
	private static final long serialVersionUID = 1L;

	private BsoFilter filter;

	public void setFilter(BsoFilter filter) {
		this.filter = filter;
	}

	@JSON(serialize = false)
	public BsoFilter getFilter() {
		return filter;
	}

	@Override
	public List<Bso> getRows() {
		return rows;
	}

	@Override
	public long getTotal() {
		return total;
	}

	{
		ALLOW_ROLES = Arrays.asList("admin", "director", "manager", "register");
		ALLOW_MSG = "У вас нет прав для просмотра журнала БСО";
	}

	@Override
	public String executeImpl() {
		BsoArchivedDao dao = new BsoArchivedDao(conn);

		if (filter == null) {
			rows = dao.listByUser(baseUser, search, sort, order, limit, offset);
		} else {
			rows = dao.listByUser(baseUser, filter, sort, order, limit, offset);
		}
		total = dao.getCount();

		return SUCCESS;
	}
}
