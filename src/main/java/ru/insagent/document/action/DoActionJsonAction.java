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
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import ru.insagent.action.BaseAction;
import ru.insagent.document.dao.BsoNormalDao;
import ru.insagent.document.model.BsoNormal;
import ru.insagent.workflow.dao.ActionDao;
import ru.insagent.workflow.model.Action;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

@Conversion(
		conversions = {
				@TypeConversion(key = "premium", converter = "ru.insagent.converter.StringToBigDecimalConverter")
		}
)
public class DoActionJsonAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private Integer bsoId;

	public Integer getBsoId() {
		return bsoId;
	}

	@RequiredFieldValidator(message = "Идентификатор БСО не передан.", shortCircuit = true)
	@ConversionErrorFieldValidator(message = "Невозможно преобразовать идентификатор БСО.", shortCircuit = true)
	public void setBsoId(Integer bsoId) {
		this.bsoId = bsoId;
	}

	private Integer actionId;

	public Integer getActionId() {
		return actionId;
	}

	@RequiredFieldValidator(message = "Идентификатор действия не передан.", shortCircuit = true)
	@ConversionErrorFieldValidator(message = "Невозможно преобразовать идентификатор действия.", shortCircuit = true)
	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	private String insured;

	public String getInsured() {
		return insured;
	}

	public void setInsured(String insured) {
		this.insured = insured;
	}

	private BigDecimal premium;

	public BigDecimal getPremium() {
		return premium;
	}

	@ConversionErrorFieldValidator(message = "Невозможно преобразовать страховую премию.", shortCircuit = true)
	public void setPremium(BigDecimal premium) {
		this.premium = premium;
	}

	{
		ALLOW_ROLES = Arrays.asList("admin", "director", "manager", "register");
		ALLOW_MSG = "У вас нет прав для выполнения действия над БСО.";
	}

	public String executeImpl() {
		BsoNormalDao bd = new BsoNormalDao();
		ActionDao ad = new ActionDao();

		BsoNormal bso = null;
		Action action = null;
		boolean result;

		result = false;
		for (BsoNormal b : bd.listByRoles(null)) {
			if (b.getId() == bsoId) {
				bso = b;
				result = true;
				break;
			}
		}
		if (!result) {
			addActionError("Вы не можете выполнять действия над выбранным БСО.");
			return ERROR;
		}

		result = false;
		for (Action a : ad.listByRoles(null)) {
			if (a.getId() == actionId) {
				action = a;
				result = true;
				break;
			}
		}
		if (!result) {
			addActionError("Вам запрещено выполнять выбранное действие.");
			return ERROR;
		}

		result = false;
		for (int id : ad.listNodeIdsByActionId(action.getId())) {
			if (id == bso.getNode().getId()) {
				result = true;
				break;
			}
		}
		if (!result) {
			addActionError("Над БСО нельзя выполнить данное действие в данном узле.");
			return ERROR;
		}

		if (action.getIdx().equals("issue")) {
			if (bso.isIssued()) {
				addActionError("БСО уже выдан страхователю.");
				return ERROR;
			}
			if (insured == null) {
				addFieldError("insured", "Страхователь не указан.");
				return ERROR;
			}
			if (premium == null) {
				addFieldError("premium", "Страховая премия не указана.");
				return ERROR;
			}

			bso.setIssued(true);
			bso.setIssuedBy(baseUser);
			bso.setIssuedUnit(bso.getUnit());
			bso.setIssuedDate(new Date());
			bso.setInsured(insured);
			bso.setPremium(premium);
		}

		if (action.getIdx().equals("corrupt")) {
			if (bso.isCorrupted()) {
				addActionError("БСО уже помечен испорченным.");
				return ERROR;
			}

			bso.setCorrupted(true);
			bso.setCorruptedBy(baseUser);
			bso.setCorruptedUnit(bso.getUnit());
			bso.setCorruptedDate(new Date());
		}

		if (action.getIdx().equals("register")) {
			if (bso.isRegistered()) {
				addActionError("БСО уже помечен зарегистрированным в системе.");
				return ERROR;
			}

			bso.setRegistered(true);
			bso.setRegisteredBy(baseUser);
			bso.setRegisteredUnit(bso.getUnit());
			bso.setRegisteredDate(new Date());
		}

		bd.update(bso);

		return SUCCESS;
	}
}
