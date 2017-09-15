package ru.insagent.model;

public class Menu extends Base {
	private static final long serialVersionUID = -7397532883347253226L;

	private int level;
	private String name;
	private String action;
	private boolean active;

	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String firstName) {
		this.name = firstName;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Menu: id=");
		sb.append(id);
		sb.append("; level=");
		sb.append(level);
		sb.append("; name=");
		sb.append(name);
		sb.append("; action=");
		sb.append(action);

		return sb.toString();
	}
}
