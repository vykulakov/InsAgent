package ru.insagent.management.model;

import ru.insagent.model.Base;

public class Role extends Base {
	private static final long serialVersionUID = 8524333312590934661L;

	private String idx;
	private String name;

	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", idx=" + idx + ", name=" + name + "]";
	}
}
