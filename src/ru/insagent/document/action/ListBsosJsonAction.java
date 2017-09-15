package ru.insagent.document.action;

import java.util.Arrays;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import ru.insagent.action.GetBaseAction;
import ru.insagent.document.dao.BsoDao;
import ru.insagent.document.model.Bso;
import ru.insagent.document.model.BsoFilter;

import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

@Conversion(
		conversions = {
				@TypeConversion(key = "filter.premiumFrom", converter = "ru.insagent.converter.StringToBigDecimalConverter"),
				@TypeConversion(key = "filter.premiumTo", converter = "ru.insagent.converter.StringToBigDecimalConverter"),
				@TypeConversion(key = "filter.createdFrom", converter = "ru.insagent.converter.StringToDateTimeConverter"),
				@TypeConversion(key = "filter.createdTo", converter = "ru.insagent.converter.StringToDateTimeConverter"),
				@TypeConversion(key = "filter.issuedFrom", converter = "ru.insagent.converter.StringToDateTimeConverter"),
				@TypeConversion(key = "filter.issuedTo", converter = "ru.insagent.converter.StringToDateTimeConverter")
		}
)
public class ListBsosJsonAction extends GetBaseAction<Bso> {
	private static final long serialVersionUID = 1L;

	private BsoFilter filter;
	public void setFilter(BsoFilter filter) {
		this.filter = filter;
	}
	@JSON(serialize = false)
	public BsoFilter getFilter() {
		return filter;
	}

	public List<Bso> getRows() {
		return rows;
	}

	public Integer getTotal() {
		return total;
	}

	{
		ALLOW_ROLES = Arrays.asList("admin", "director", "manager", "register");
		ALLOW_MSG = "У вас нет прав для просмотра журнала БСО";
	}

	@Override
	public String executeImpl() {
		dao = new BsoDao(conn);

		if(filter == null) {
			rows = dao.listByUser(user, search, sort, order, limit, offset);
		} else {
			rows = dao.listByUser(user, filter, sort, order, limit, offset);
		}
		total = dao.getCount();

		return SUCCESS;
	}

	@Override
	public void validateImpl() {
	}
}
