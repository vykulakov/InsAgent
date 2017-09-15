package ru.insagent.document.action;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

import ru.insagent.action.BaseAction;
import ru.insagent.document.dao.BsoArchivedDao;
import ru.insagent.document.dao.BsoDao;
import ru.insagent.document.model.ActPack;
import ru.insagent.document.model.Bso;
import ru.insagent.util.Utils;

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
			@RequiredFieldValidator(fieldName = "pack.numberTo",   message = "Необходимо указать \"Номер по\".", shortCircuit = true),
			@RequiredFieldValidator(fieldName = "pack.amount",     message = "Необходимо указать количество БСО.", shortCircuit = true)
		},
		requiredStrings = {
			@RequiredStringValidator(fieldName = "pack.series", message = "Необходимо указать серию пачки БСО.", shortCircuit = true)
		},
		conversionErrorFields = {
			@ConversionErrorFieldValidator(fieldName = "pack.numberFrom", message = "Невозможно преобразовать \"Номер с\".", shortCircuit = true),
			@ConversionErrorFieldValidator(fieldName = "pack.numberTo",   message = "Невозможно преобразовать \"Номер по\".", shortCircuit = true),
			@ConversionErrorFieldValidator(fieldName = "pack.amount",     message = "Невозможно преобразовать количество.", shortCircuit = true)
		},
		stringLengthFields = {
			@StringLengthFieldValidator(fieldName = "pack.series", maxLength = "8", message = "Серия пачки БСО должна содержать 8 и менее символов.")
		}
	)
	public String executeImpl() {
		//TODO Нужно проверять, можно ли передать выбранное БСО из одного подразделения в другое. Может в выбранном подразделении этого БСО вообще нет.
		BsoDao bd = new BsoDao(conn);
		BsoArchivedDao bad = new BsoArchivedDao(conn);

		Set<Long> numbers = null;
		String ranges = null;

		numbers = new TreeSet<Long>();
		if(mustExists != 0) {
			for(long i = pack.getNumberFrom(); i <= pack.getNumberTo(); i++) {
				numbers.add(i);
			}
		}

		List<Bso> bsos = bd.listBySeriesAndNumbers(pack.getSeries(), pack.getNumberFrom(), pack.getNumberTo());
		List<Bso> bsosArchived = bad.listBySeriesAndNumbers(pack.getSeries(), pack.getNumberFrom(), pack.getNumberTo());
		if(mustExists != 0) {
			for(Bso bso : bsos) {
				numbers.remove(bso.getNumber());
			}
			for(Bso bsoArchived : bsosArchived) {
				numbers.remove(bsoArchived.getNumber());
			}
		} else {
			for(Bso bso : bsos) {
				numbers.add(bso.getNumber());
			}
		}

		ranges = Utils.convertNumbersToRanges(numbers);
		if(ranges.length() > 0) {
			if(mustExists != 0) {
				addActionError("БСО с номерами " + ranges + " не заведены в системе.");
			} else {
				addActionError("БСО с номерами " + ranges + " уже заведены в системе.");
			}
			return ERROR;
		}

		numbers = new TreeSet<Long>();
		for(Bso bsoArchived : bsosArchived) {
			numbers.add(bsoArchived.getNumber());
		}

		ranges = Utils.convertNumbersToRanges(numbers);
		if(ranges.length() > 0) {
			if(mustExists == 0) {
				addActionError("БСО с номерами " + ranges + " уже проходили через систему.");
			} else {
				addActionError("БСО с номерами " + ranges + " перемещены в архив.");
			}
			return ERROR;
		}

		return SUCCESS;
	}

	@Override
	public void validateImpl() {
		if(pack.getNumberTo() < pack.getNumberFrom()) {
			addActionError("Передан неправильный диапазон номеров.");
			return;
		}
		if(pack.getNumberTo() - pack.getNumberFrom() + 1 != pack.getAmount()) {
			addActionError("Передано неправильно количество БСО в пачке.");
		}
	}
}
