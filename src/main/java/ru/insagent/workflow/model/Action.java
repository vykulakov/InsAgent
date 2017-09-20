package ru.insagent.workflow.model;

import ru.insagent.model.IdBase;

public class Action extends IdBase {
	private static final long serialVersionUID = -3709167442225090544L;

	private String idx;
	private String shortName;
	private String fullName;

	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((shortName == null) ? 0 : shortName.hashCode());
		return result;
	}

	@Override
	public String toString() {
		//TODO Написать метод
		StringBuilder sb = new StringBuilder();
		sb.append("Action: id=");
		sb.append(id);

		return sb.toString();
	}
}
