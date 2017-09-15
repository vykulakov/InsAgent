package ru.insagent.management.city.action;

import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

import ru.insagent.action.BaseAction;
import ru.insagent.management.dao.CityDao;
import ru.insagent.management.model.City;

public class UpdateCityJsonAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private City city;
	public City getCity() {
		return city;
	}
	@RequiredFieldValidator(message = "Город не передан.", shortCircuit = true)
	public void setCity(City city) {
		this.city = city;
	}

	@Override
	@Validations(
		requiredFields = {
			@RequiredFieldValidator(fieldName = "city.id", message = "Идентификатор города не указан.", shortCircuit = true)
		},
		requiredStrings = {
			@RequiredStringValidator(fieldName = "city.name", message = "Название города не указано.", shortCircuit = true)
		},
		conversionErrorFields = {
			@ConversionErrorFieldValidator(fieldName = "city.id", message = "Невозможно преобразовать идентификатор города.", shortCircuit = true)
		},
		intRangeFields = {
			@IntRangeFieldValidator(fieldName = "city.id", min = "0", max = "255", message = "Идентификатор города имеет недопустимое значение.", shortCircuit = true),
		},
		stringLengthFields = {
			@StringLengthFieldValidator(fieldName = "city.name",    maxLength = "255",  message = "Название города должно содержать менее 255 символов."),
			@StringLengthFieldValidator(fieldName = "city.comment", maxLength = "2048", message = "Комментарий к городу должен содержать менее 2048 символов.")
		}
	)
	public String executeImpl() {
		CityDao cd = new CityDao(conn);

		cd.update(city);

		return SUCCESS;
	}

	@Override
	public void validateImpl() {
	}
}
