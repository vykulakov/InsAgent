package ru.insagent.model;

import java.io.Serializable;

public abstract class Base implements Serializable {
	private static final long serialVersionUID = -7939086479093714662L;

	protected int id;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		// Если вместо getId() использовать id или this.id, то в hashCode всегда будет 0. Пока не знаю, почему именно так.
		result = prime * result + getId();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}

		if(obj == null) {
			return false;
		}

		if(getClass() != obj.getClass()) {
			return false;
		}

		Base other = (Base) obj;
		if(getId() != other.id) {
			return false;
		}

		return true;
	}

	public abstract String toString();
}
