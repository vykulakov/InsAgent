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
import ru.insagent.dao.UnitDao;
import ru.insagent.document.dao.ActDao;
import ru.insagent.document.dao.BsoArchivedDao;
import ru.insagent.document.dao.BsoDao;
import ru.insagent.document.model.Act;
import ru.insagent.document.model.ActPack;
import ru.insagent.document.model.Bso;
import ru.insagent.model.Unit;
import ru.insagent.util.Utils;
import ru.insagent.workflow.dao.LinkDao;
import ru.insagent.workflow.dao.NodeDao;
import ru.insagent.workflow.model.Item;
import ru.insagent.workflow.model.Link;
import ru.insagent.workflow.model.Node;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class UpdateActJsonAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private Act act;

	public Act getAct() {
		return act;
	}

	@RequiredFieldValidator(message = "Акт не передан.", shortCircuit = true)
	public void setAct(Act act) {
		this.act = act;
	}

	private Item itemFrom;

	public Item getItemFrom() {
		return itemFrom;
	}

	@RequiredFieldValidator(message = "Отправитель не передан.", shortCircuit = true)
	public void setItemFrom(Item itemFrom) {
		this.itemFrom = itemFrom;
	}

	private Item itemTo;

	public Item getItemTo() {
		return itemTo;
	}

	@RequiredFieldValidator(message = "Получатель не передан.", shortCircuit = true)
	public void setItemTo(Item itemTo) {
		this.itemTo = itemTo;
	}

	{
		ALLOW_ROLES = Arrays.asList("admin", "director", "manager", "register");
		ALLOW_MSG = "У вас нет прав для проводки документов.";
	}

	@Override
	@Validations(
			requiredFields = {
					@RequiredFieldValidator(fieldName = "act.id", message = "Идентификатор акта не указан.", shortCircuit = true),
					@RequiredFieldValidator(fieldName = "act.type.id", message = "Идентификатор типа акта не указан.", shortCircuit = true)
			},
			conversionErrorFields = {
					@ConversionErrorFieldValidator(fieldName = "act.id", message = "Невозможно преобразовать идентификатор акта.", shortCircuit = true),
					@ConversionErrorFieldValidator(fieldName = "act.type.id", message = "Невозможно преобразовать идентификатор типа акта.", shortCircuit = true)
			},
			intRangeFields = {
					@IntRangeFieldValidator(fieldName = "act.type.id", min = "0", max = "255", message = "Идентификатор типа акта имеет недопустимое значение.", shortCircuit = true)
			},
			stringLengthFields = {
					@StringLengthFieldValidator(fieldName = "act.comment", maxLength = "2048", message = "Комментарий к акту должен содержать менее 2048 символов.")
			}
	)
	public String executeImpl() {
		//TODO Добавить проверку текущих узла и подразделения для выбранных БСО.
		NodeDao nd = new NodeDao(conn);
		LinkDao ld = new LinkDao(conn);
		ActDao ad = new ActDao(conn);
		BsoDao bd = new BsoDao(conn);
		BsoArchivedDao bad = new BsoArchivedDao(conn);
		UnitDao ud = new UnitDao();

		List<Unit> units = ud.listByUser(baseUser);

		int index = 0;
		Node actNodeFrom = null;
		Node actNodeTo = null;
		Unit actUnitFrom = null;
		Unit actUnitTo = null;

		for (Link link : ld.listByUser(baseUser)) {
			if (link.getActType() != null && link.getActType().equals(act.getType())) {
				if (!itemFrom.getNodeIds().contains(link.getNodeFrom().getId())) {
					continue;
				}
				if (!itemTo.getNodeIds().contains(link.getNodeTo().getId())) {
					continue;
				}

				Node nodeFrom = nd.getById(link.getNodeFrom().getId());
				Node nodeTo = nd.getById(link.getNodeTo().getId());

				if (itemFrom.getNodeTypeId() != nodeFrom.getNodeType().getId()) {
					continue;
				}
				if (itemTo.getNodeTypeId() != nodeTo.getNodeType().getId()) {
					continue;
				}

				if (nodeFrom.getNodeType().getId() == 4) {
					boolean skip = true;
					for (Unit unit : units) {
						if (nodeFrom.getUnitType().equals(unit.getType()) && itemFrom.getId() == unit.getId()) {
							skip = false;
							actUnitFrom = unit;
							break;
						}
					}
					if (skip) {
						continue;
					}
				}
				if (nodeTo.getNodeType().getId() == 4) {
					boolean skipTo = true;
					for (Unit unit : units) {
						if (nodeTo.getUnitType().equals(unit.getType()) && itemTo.getId() == unit.getId()) {
							skipTo = false;
							actUnitTo = unit;
							break;
						}
					}
					if (skipTo) {
						continue;
					}
				}

				index++;
				actNodeFrom = nodeFrom;
				actNodeTo = nodeTo;
			}
		}
		if (index == 0) {
			addActionError("Переход между отправителем и получателем не найден");
			return ERROR;
		}
		if (index > 1) {
			addActionError("Найдено более одного перехода между отправителем и получателем");
			return ERROR;
		}
		if (actNodeFrom.getNodeType().getId() == 4 && actUnitFrom == null) {
			addActionError("Подразделение отправителя не найдено");
			return ERROR;
		}
		if (actNodeTo.getNodeType().getId() == 4 && actUnitTo == null) {
			addActionError("Подразделение получателя не найдено");
			return ERROR;
		}


		act.setNodeFrom(actNodeFrom);
		act.setNodeTo(actNodeTo);
		act.setUnitFrom(actUnitFrom);
		act.setUnitTo(actUnitTo);

		int amount = 0;
		for (ActPack pack : act.getPacks()) {
			amount += pack.getAmount();

			Set<Long> numbers;
			List<Bso> bsos = bd.listBySeriesAndNumbers(pack.getSeries(), pack.getNumberFrom(), pack.getNumberTo());
			List<Bso> bsosArchived = bad.listBySeriesAndNumbers(pack.getSeries(), pack.getNumberFrom(), pack.getNumberTo());

			// Проверяем, заведены или нет БСО в системе.
			numbers = new TreeSet<Long>();
			if (act.getType().getId() != 1) {
				for (long i = pack.getNumberFrom(); i <= pack.getNumberTo(); i++) {
					numbers.add(i);
				}
			}
			if (act.getType().getId() != 1) {
				for (Bso bso : bsos) {
					numbers.remove(bso.getNumber());
				}
				for (Bso bsoArchived : bsosArchived) {
					numbers.remove(bsoArchived.getNumber());
				}
			} else {
				for (Bso bso : bsos) {
					numbers.add(bso.getNumber());
				}
			}
			if (!numbers.isEmpty()) {
				String ranges = Utils.convertNumbersToRanges(numbers);
				if (ranges.length() > 0) {
					if (act.getType().getId() == 1) {
						addActionError("БСО с серией " + pack.getSeries() + " и номерами " + ranges + " уже заведены в системе.");
					} else {
						addActionError("БСО с серией " + pack.getSeries() + " и номерами " + ranges + " не заведены в системе.");
					}
					return ERROR;
				}
			}

			// Проверяем, что БСО в нужном узле и в нужном подразделении.
			if (act.getType().getId() != 1) {
				numbers = new TreeSet<Long>();
				for (Bso bso : bsos) {
					if (!actNodeFrom.equals(bso.getNode())) {
						numbers.add(bso.getNumber());
						continue;
					}
					if (actNodeFrom.getNodeType().getId() == 4 && (bso.getUnit() == null || !actUnitFrom.equals(bso.getUnit()))) {
						numbers.add(bso.getNumber());
						continue;
					}
				}
				if (!numbers.isEmpty()) {
					String ranges = Utils.convertNumbersToRanges(numbers);
					addActionError("БСО с серией " + pack.getSeries() + " и номерами " + ranges + " нельзя провести этим актом.");
					return ERROR;
				}
			}
		}

		// Проверяем БСО по архиву.
		for (ActPack pack : act.getPacks()) {
			Set<Long> numbers;
			List<Bso> bsosArchived = bad.listBySeriesAndNumbers(pack.getSeries(), pack.getNumberFrom(), pack.getNumberTo());

			// Проверяем, есть или нет БСО в архиве.
			numbers = new TreeSet<Long>();
			for (Bso bso : bsosArchived) {
				numbers.add(bso.getNumber());
			}
			if (!numbers.isEmpty()) {
				String ranges = Utils.convertNumbersToRanges(numbers);
				if (ranges.length() > 0) {
					if (act.getType().getId() == 1) {
						addActionError("БСО с серией " + pack.getSeries() + " и номерами " + ranges + " уже проходили через систему.");
					} else {
						addActionError("БСО с серией " + pack.getSeries() + " и номерами " + ranges + " перемещены в архив.");
					}
					return ERROR;
				}
			}
		}

		act.setAmount(amount);

		ad.update(act);

		return SUCCESS;
	}
}
