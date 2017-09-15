package ru.insagent.management.city.action;

import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

import ru.insagent.action.BaseAction;
import ru.insagent.management.dao.CityDao;

public class RemoveCityJsonAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private Integer cityId;
	public Integer getCityId() {
		return cityId;
	}
	@RequiredFieldValidator(message = "Город не передано.", shortCircuit = true)
	@ConversionErrorFieldValidator(message = "Невозможно преобразовать идентификатор города.", shortCircuit = true)
	@IntRangeFieldValidator(message = "Идентификатор города имеет недопустимое значение.", min = "1", shortCircuit = true)
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	@Override
	public String executeImpl() {
		CityDao cd = new CityDao(conn);

		cd.remove(cityId);

		return SUCCESS;
	}

	@Override
	public void validateImpl() {
	}
}
