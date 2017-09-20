package ru.insagent.document.model;

import ru.insagent.model.IdBase;

public class ActType extends IdBase {
	private static final long serialVersionUID = 4580145163020300664L;

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
	public String toString() {
		return "ActType [id=" + id + ", idx=" + idx + ", shortName=" + shortName + ", fullName=" + fullName + "]";
	}
}
