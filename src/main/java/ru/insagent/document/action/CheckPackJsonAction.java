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

import com.opensymphony.xwork2.validator.annotations.*;
import ru.insagent.action.BaseAction;
import ru.insagent.document.dao.BsoArchivedDao;
import ru.insagent.document.dao.BsoNormalDao;
import ru.insagent.document.model.ActPack;
import ru.insagent.document.model.Bso;
import ru.insagent.document.model.BsoArchived;
import ru.insagent.document.model.BsoNormal;
import ru.insagent.util.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CheckPackJsonAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private ActPack pack;

	public ActPack getPack() {
		return pack;
	}

	@RequiredFieldValidator(message = "Пачка БСО не передана.", shortCircuit = true)
	public void setPack(ActPack pack) {
		this.pack = pack;
	}

	private Integer mustExists;

	public Integer getMustExists() {
		return mustExists;
	}

	@RequiredFieldValidator(message = "Отметка о существовании не передана.", shortCircuit = true)
	@ConversionErrorFieldValidator(message = "Невозможно преобразовать отметку о существовании.", shortCircuit = true)
	public void setMustExists(Integer mustExists) {
		this.mustExists = mustExists;
	}

	{
		ALLOW_ROLES = Arrays.asList("admin", "director", "manager", "sale");
		ALLOW_MSG = "У вас нет прав для проверки пачек БСО";
	}

	@Override
	@Validations(
			requiredFields = {
					@RequiredFieldValidator(fieldName = "pack.numberFrom", message = "Необходимо указать \"Номер с\".", shortCircuit = true),
					@RequiredFieldValidator(fieldName = "pack.numberTo", message = "Необходимо указать \"Номер по\".", shortCircuit = true),
					@RequiredFieldValidator(fieldName = "pack.amount", message = "Необходимо указать количество БСО.", shortCircuit = true)
			},
			requiredStrings = {
					@RequiredStringValidator(fieldName = "pack.series", message = "Необходимо указать серию пачки БСО.", shortCircuit = true)
			},
			conversionErrorFields = {
					@ConversionErrorFieldValidator(fieldName = "pack.numberFrom", message = "Невозможно преобразовать \"Номер с\".", shortCircuit = true),
					@ConversionErrorFieldValidator(fieldName = "pack.numberTo", message = "Невозможно преобразовать \"Номер по\".", shortCircuit = true),
					@ConversionErrorFieldValidator(fieldName = "pack.amount", message = "Невозможно преобразовать количество.", shortCircuit = true)
			},
			stringLengthFields = {
					@StringLengthFieldValidator(fieldName = "pack.series", maxLength = "8", message = "Серия пачки БСО должна содержать 8 и менее символов.")
			}
	)
	public String executeImpl() {
		//TODO Нужно проверять, можно ли передать выбранное БСО из одного подразделения в другое. Может в выбранном подразделении этого БСО вообще нет.
		BsoNormalDao bd = new BsoNormalDao();
		BsoArchivedDao bad = new BsoArchivedDao();

		Set<Long> numbers = null;
		String ranges = null;

		numbers = new TreeSet<Long>();
		if (mustExists != 0) {
			for (long i = pack.getNumberFrom(); i <= pack.getNumberTo(); i++) {
				numbers.add(i);
			}
		}

		List<BsoNormal> bsos = bd.listBySeriesAndNumbers(pack.getSeries(), pack.getNumberFrom(), pack.getNumberTo());
		List<BsoArchived> bsosArchived = bad.listBySeriesAndNumbers(pack.getSeries(), pack.getNumberFrom(), pack.getNumberTo());
		if (mustExists != 0) {
			for (Bso bso : bsos) {
				numbers.remove(bso.getNumber());
			}
			for (Bso bso : bsosArchived) {
				numbers.remove(bso.getNumber());
			}
		} else {
			for (Bso bso : bsos) {
				numbers.add(bso.getNumber());
			}
		}

		ranges = Utils.convertNumbersToRanges(numbers);
		if (ranges.length() > 0) {
			if (mustExists != 0) {
				addActionError("БСО с номерами " + ranges + " не заведены в системе.");
			} else {
				addActionError("БСО с номерами " + ranges + " уже заведены в системе.");
			}
			return ERROR;
		}

		numbers = new TreeSet<Long>();
		for (Bso bso : bsosArchived) {
			numbers.add(bso.getNumber());
		}

		ranges = Utils.convertNumbersToRanges(numbers);
		if (ranges.length() > 0) {
			if (mustExists == 0) {
				addActionError("БСО с номерами " + ranges + " уже проходили через систему.");
			} else {
				addActionError("БСО с номерами " + ranges + " перемещены в архив.");
			}
			return ERROR;
		}

		return SUCCESS;
	}

	@Override
	public void validate() {
		if (pack.getNumberTo() < pack.getNumberFrom()) {
			addActionError("Передан неправильный диапазон номеров.");
			return;
		}
		if (pack.getNumberTo() - pack.getNumberFrom() + 1 != pack.getAmount()) {
			addActionError("Передано неправильно количество БСО в пачке.");
		}
	}
}
