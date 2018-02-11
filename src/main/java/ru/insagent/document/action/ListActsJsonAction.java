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

import ru.insagent.action.GetBaseAction;
import ru.insagent.document.dao.ActDao;
import ru.insagent.document.dao.ActPackDao;
import ru.insagent.document.model.Act;

import java.util.Arrays;
import java.util.List;

public class ListActsJsonAction extends GetBaseAction<Act> {
	private static final long serialVersionUID = 1L;

	@Override
	public List<Act> getRows() {
		return rows;
	}

	@Override
	public long getTotal() {
		return total;
	}

	{
		ALLOW_ROLES = Arrays.asList("admin", "director", "manager", "sale");
		ALLOW_MSG = "У вас нет прав для просмотра журнала актов";
	}

	@Override
	public String executeImpl() {
		ActDao dao = new ActDao(conn);

		rows = dao.listByUser(baseUser, sort, order, limit, offset);
		total = dao.getCount();

		ActPackDao pd = new ActPackDao(conn);
		for (Act act : rows) {
			act.setPacks(pd.listByActId(act.getId()));
		}

		return SUCCESS;
	}
}
