/*
 * InsAgent - https://github.com/vykulakov/InsAgent
 *
 * Copyright 2017 Vyacheslav Kulakov
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

package ru.insagent.action;

import java.util.List;

import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;

import ru.insagent.model.IdBase;

/**
 * The abstract action for getting entities to fill
 * bootstrap tables.
 * @param <E> - entity type.
 */
public abstract class GetBaseAction<E extends IdBase> extends BaseAction {
	private static final long serialVersionUID = 1L;

	protected String search;
	public void setSearch(String search) {
		this.search = search;
	}

	protected String sort;
	public void setSort(String sort) {
		this.sort = sort;
	}

	protected String order;
	public void setOrder(String order) {
		this.order = order;
	}

	protected int limit = 0;
	@ConversionErrorFieldValidator(message = "Невозможно преобразовать значение количества строк.", shortCircuit = true)
	public void setLimit(int limit) {
		this.limit = limit;
	}

	protected int offset = 0;
	@ConversionErrorFieldValidator(message = "Невозможно преобразовать смещение.", shortCircuit = true)
	public void setOffset(int offset) {
		this.offset = offset;
	}

	protected List<E> rows;
	public List<E> getRows() {
		return rows;
	}

	protected long total;
	public long getTotal() {
		return total;
	}

	@Override
	abstract public String executeImpl();
}
