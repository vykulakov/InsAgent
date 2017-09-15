package ru.insagent.management.unit.action;

import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

import ru.insagent.action.BaseAction;
import ru.insagent.management.dao.UnitDao;

public class RemoveUnitJsonAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private Integer unitId;
	public Integer getUnitId() {
		return unitId;
	}
	@RequiredFieldValidator(message = "Подразделение не передано.", shortCircuit = true)
	@ConversionErrorFieldValidator(message = "Невозможно преобразовать идентификатор подразделения.", shortCircuit = true)
	@IntRangeFieldValidator(message = "Идентификатор подразделения имеет недопустимое значение.", min = "1", shortCircuit = true)
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	@Override
	public String executeImpl() {
		UnitDao ud = new UnitDao(conn);

		ud.remove(unitId);

		return SUCCESS;
	}

	@Override
	public void validateImpl() {
	}
}
