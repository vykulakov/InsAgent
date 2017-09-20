package ru.insagent.action;

import java.util.List;

import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;

import ru.insagent.action.BaseAction;
import ru.insagent.dao.SimpleDao;
import ru.insagent.model.IdBase;

public class GetBaseAction<E extends IdBase> extends BaseAction {
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

	protected Integer limit = 0;
	@ConversionErrorFieldValidator(message = "Невозможно преобразовать значение количества строк.", shortCircuit = true)
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	
	protected Integer offset = 0;
	@ConversionErrorFieldValidator(message = "Невозможно преобразовать смещение.", shortCircuit = true)
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	protected List<E> rows;
	public List<E> getRows() {
		return rows;
	}

	protected Integer total;
	public Integer getTotal() {
		return total;
	}

	protected SimpleDao<E> dao;

	@Override
	public String executeImpl() {

		rows = dao.list(search, sort, order, limit, offset);
		total = dao.getCount();

		return SUCCESS;
	}

	@Override
	public void validateImpl() {
	}
}
